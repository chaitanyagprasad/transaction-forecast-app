package io.github.chait.transaction.service;

import io.github.chait.transaction.docs.TransactionDoc;
import io.github.chait.transaction.dto.InsightPreferenceDTO;
import io.github.chait.transaction.dto.InsightResponseDTO;
import io.github.chait.transaction.dto.TransactionDTO;
import io.github.chait.transaction.dto.TransactionFlow;
import io.github.chait.transaction.mapper.TransactionMapper;
import io.github.chait.transaction.repo.TransactionEsRepo;
import io.github.chait.transaction.repo.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service@Slf4j
public class SimpleTransactionServiceImpl implements TransactionService {

    private List<String> knownIncomeTags = Arrays.asList("salary", "deposit");
    private List<String> knownExpenseTags = Arrays.asList("bill", "electricity", "telephone", "grocery", "rent");

    private final Comparator<TransactionFlow> transactionFlowComparator = Comparator.comparing(TransactionFlow::getDate);

    private final String INCOME_KEY = "INCOME";
    private final String EXPENDITURE_KEY = "EXPNDTR";


    @Autowired
    TransactionMapper mapper;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionEsRepo transactionEsRepo;

    @Autowired
    AccountPreferenceService accountPreferenceService;

    @Override
    public void putTransactionEs(TransactionDTO dto) {
        TransactionDoc doc = mapper.mapToDoc(dto);
        doc.setId(UUID.randomUUID().toString());
        transactionEsRepo.save(doc);
    }

    @Override
    public Page<TransactionDTO> getTransactionByTransactionType(String transactionType) {
        String transactionTypeVal = transactionType.equalsIgnoreCase("income") ? "INCOME" : "EXPNDTR";

        return transactionEsRepo.findAllByTransactionType(transactionTypeVal, PageRequest.of(0,5))
                .map(mapper::mapDocToDto);
    }

    @Override
    public List<TransactionFlow> getTransactionFlowByAccountIdAndTransactionType(String accountId, String txType) {
        String transactionTypeVal = txType.equalsIgnoreCase("income") ? "INCOME" : "EXPNDTR";
        accountId = accountId.trim();
        Set<TransactionDoc> transactionDocs = transactionEsRepo.findAllByAccountIdAndTransactionType(accountId, transactionTypeVal);
        return parseTransactionFlow(transactionDocs);

    }

    @Override
    public InsightResponseDTO getInsightForAccount(String accountId, LocalDate date, InsightPreferenceDTO preferenceDTO) {
        /*
        * 1. save the preference
        * 2. get all txn from first working day of last month to date
        * 3. forecast from date to last day of current month
        * 4. forecast for the entire next month
        * */
//        accountPreferenceService.saveAccountPreference(preferenceDTO, date);
        LocalDate firstDayOfPreviousMonth = date.minusMonths(1).withDayOfMonth(1);
        Set<TransactionDoc> total = this.transactionEsRepo.findAllByAccountIdAndTransactionDateBetween(accountId,
                firstDayOfPreviousMonth,
                date);
        Comparator<TransactionDoc> transactionDocComparator = Comparator.comparing(TransactionDoc::getTransactionDate);

        BigDecimal closingBal = total.stream()
                .max(transactionDocComparator)
                .map(TransactionDoc::getBalance)
                .orElse(BigDecimal.ZERO);

        InsightResponseDTO res = InsightResponseDTO.builder()
                .withCurrentMonthLowestBalance(closingBal)
                .withIncomeFlows(new ArrayList<>())
                .withExpenditureFlows(new ArrayList<>())
                .build();
        res = this.forecastForRemainderOfCurrentMonth(date, total, res, preferenceDTO, true); /* res at this point will have remainder of current month */
        res = this.forecastForRemainderOfCurrentMonth(date, total, res, preferenceDTO, false);

        return res;
    }

    @Override
    public List<TransactionDTO> getTransactionByAccountId(String accountId) {
        Comparator<TransactionDTO> dateComparator = new Comparator<TransactionDTO>() {
            @Override
            public int compare(TransactionDTO o1, TransactionDTO o2) {
                return o1.getTransactionDate().compareTo(o2.getTransactionDate());
            }
        };
        return transactionEsRepo.findAllByAccountId(accountId)
                .stream()
                .map(mapper::mapDocToDto)
                .sorted(dateComparator)
                .collect(Collectors.toList() );
    }

    private List<TransactionFlow> parseTransactionFlow(Set<TransactionDoc> transactionDocs) {
        List<TransactionFlow> result = new ArrayList<>();
        transactionDocs.forEach(doc -> {
            TransactionFlow flow = new TransactionFlow(doc.getTransactionAmount(), doc.getTransactionDate().plusMonths(1), doc.getTags().stream().findAny().get());
            result.add(flow);
        });
        return result;
    }

    private List<TransactionFlow> parseTransactionFlow(List<TransactionFlow> transactionFlows) {
        List<TransactionFlow> result = new ArrayList<>();
        transactionFlows.forEach(doc -> {
            TransactionFlow flow = new TransactionFlow(doc.getAmount(), doc.getDate().plusMonths(1), doc.getDescription());
            result.add(flow);
        });
        return result;
    }

    private List<TransactionFlow> getFilteredSet(Set<TransactionDoc> input, Collection<String> tags) {
        Set<TransactionDoc> res = new HashSet<>();
        input.forEach(transactionDoc -> {
            if( !Collections.disjoint(transactionDoc.getTags(), tags) ) {
                res.add(transactionDoc);
            }
        });
        return parseTransactionFlow(res);
    }



    private BigDecimal getCurrentMonthClosingBal(BigDecimal previousClosingBal, BigDecimal incomes, BigDecimal expenses) {
        return previousClosingBal.add(incomes).subtract(expenses);
    }

    /**
     * @param currentDate
     * @param transactions => this will contain transaction from first day of currentDate.minusMonths(1) to currentDate
     * @return
     */
    private InsightResponseDTO forecastForRemainderOfCurrentMonth(LocalDate currentDate,
                                                                  Set<TransactionDoc> transactions,
                                                                  InsightResponseDTO res,
                                                                  InsightPreferenceDTO preferenceDTO,
                                                                  boolean isCurrentMonth) {

        LocalDate cutoffDate;
        Predicate<TransactionDoc> incomePredicate;
        Predicate<TransactionDoc> expensePredicate;
        if( isCurrentMonth ) {
            cutoffDate= currentDate.minusMonths(1);
        }else {
            cutoffDate = currentDate.withDayOfMonth(1);
        }
        incomePredicate = e -> e.getTransactionType().equalsIgnoreCase(INCOME_KEY) &&
                !e.getTransactionDate().isBefore(cutoffDate.withDayOfMonth(1)) &&
                !e.getTransactionDate().isAfter(cutoffDate.withDayOfMonth(cutoffDate.lengthOfMonth()));
        expensePredicate= e -> e.getTransactionType().equalsIgnoreCase(EXPENDITURE_KEY) &&
                !e.getTransactionDate().isBefore(cutoffDate) &&
                !e.getTransactionDate().isAfter(cutoffDate.withDayOfMonth(cutoffDate.lengthOfMonth()));


        Set<TransactionDoc> incomes = transactions.stream()
                .filter(incomePredicate)
                .collect(Collectors.toSet());
        Set<TransactionDoc> expenses = transactions.stream()
                .filter(expensePredicate)
                .collect(Collectors.toSet());

        List<TransactionFlow> incomesForecast = parseTransactionFlow(incomes);
        incomesForecast.addAll( isCurrentMonth ? preferenceDTO.getIncomeFlows() : parseTransactionFlow(res.getIncomeFlows()));
        List<TransactionFlow> expenseForecast = parseTransactionFlow(expenses);
        expenseForecast.addAll( isCurrentMonth ? preferenceDTO.getExpenditureFlows() : parseTransactionFlow(res.getExpenditureFlows()));

        BigDecimal totalIncomes = incomesForecast
                .stream()
                .filter(e -> !e.getDate().isBefore(currentDate))
                .map(TransactionFlow::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenseForecast
                .stream()
                .filter(e -> !e.getDate().isBefore(currentDate))
                .map(TransactionFlow::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentMonthLowestBalance = res.getCurrentMonthLowestBalance().add(totalIncomes).subtract(totalExpenses);


        res.getIncomeFlows().addAll(incomesForecast.stream().filter(e -> !e.getDate().isBefore(currentDate)).collect(Collectors.toList()));
        res.getExpenditureFlows().addAll(expenseForecast.stream().filter(e -> !e.getDate().isBefore(currentDate)).collect(Collectors.toList()));
        if(isCurrentMonth) {
            res.setCurrentMonthLowestBalance(currentMonthLowestBalance);
            res.setTotalIncomeInCurrentMonth(totalIncomes);
            res.setTotalExpensesInCurrentMonth(totalExpenses);
        }else{
            BigDecimal nextMonthLowestBalance = res.getCurrentMonthLowestBalance().add( totalIncomes ).subtract(totalExpenses);
            res.setNextMonthLowestBalance(nextMonthLowestBalance);
            res.setTotalIncomeInNextMonth(totalIncomes);
            res.setTotalExpensesInNextMonth(totalExpenses);
        }
        Collections.sort(res.getIncomeFlows(), transactionFlowComparator);
        Collections.sort(res.getExpenditureFlows(), transactionFlowComparator);
        return res;
    }





}
