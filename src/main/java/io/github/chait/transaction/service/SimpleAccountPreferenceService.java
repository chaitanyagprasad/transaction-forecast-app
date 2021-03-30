package io.github.chait.transaction.service;

import io.github.chait.transaction.docs.AccountMonthlyPreferenceDoc;
import io.github.chait.transaction.dto.InsightPreferenceDTO;
import io.github.chait.transaction.mapper.AccountPreferenceMapper;
import io.github.chait.transaction.repo.AccountPreferenceEsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SimpleAccountPreferenceService implements AccountPreferenceService {

    @Autowired
    AccountPreferenceEsRepo accountPreferenceEsRepo;

    @Autowired
    AccountPreferenceMapper accountPreferenceMapper;

    @Override
    public void saveAccountPreference(InsightPreferenceDTO dto, LocalDate date) {
        Optional<AccountMonthlyPreferenceDoc> accountMonthlyPreferenceDocOptional = accountPreferenceEsRepo.findById(dto.getAccountId());
        AccountMonthlyPreferenceDoc accountMonthlyPreferenceDoc;
        if( accountMonthlyPreferenceDocOptional.isPresent() ) {
            accountMonthlyPreferenceDoc = accountMonthlyPreferenceDocOptional.get();
        }else {
            accountMonthlyPreferenceDoc = accountPreferenceMapper.mapDtoToDoc(dto);
        }
        accountMonthlyPreferenceDoc.setLastPreferenceSetDate(date);
        accountMonthlyPreferenceDoc.setExpenditureFlows(dto.getExpenditureFlows());
        accountMonthlyPreferenceDoc.setIncomeFlows(dto.getIncomeFlows());
        this.accountPreferenceEsRepo.save(accountMonthlyPreferenceDoc);

    }

    @Override
    public InsightPreferenceDTO getPreference(String accountId) {
        Optional<AccountMonthlyPreferenceDoc> accountMonthlyPreferenceDocOptional = accountPreferenceEsRepo.findById(accountId);
        if(accountMonthlyPreferenceDocOptional.isPresent()) {
            return accountPreferenceMapper.mapDocToDto(accountMonthlyPreferenceDocOptional.get());
        }
        return null;
    }
}
