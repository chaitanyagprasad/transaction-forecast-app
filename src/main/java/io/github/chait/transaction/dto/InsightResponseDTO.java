package io.github.chait.transaction.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

@Getter@Setter@Builder(setterPrefix = "with")
public class InsightResponseDTO {

    private BigDecimal currentMonthLowestBalance;
    private List<TransactionFlow> incomeFlows;
    private List<TransactionFlow> expenditureFlows;
    private BigDecimal nextMonthLowestBalance;
    private BigDecimal totalIncomeInCurrentMonth;
    private BigDecimal totalIncomeInNextMonth;
    private BigDecimal totalExpensesInCurrentMonth;
    private BigDecimal totalExpensesInNextMonth;

}
