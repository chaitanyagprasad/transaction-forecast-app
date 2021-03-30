package io.github.chait.transaction.service;

import io.github.chait.transaction.dto.InsightPreferenceDTO;
import io.github.chait.transaction.dto.InsightResponseDTO;
import io.github.chait.transaction.dto.TransactionDTO;
import io.github.chait.transaction.dto.TransactionFlow;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.*;

public interface TransactionService {

//    void putTransaction(TransactionDTO dto);

    void putTransactionEs(TransactionDTO dto);

    Page<TransactionDTO> getTransactionByTransactionType(String transactionType);

    List<TransactionFlow> getTransactionFlowByAccountIdAndTransactionType(String accountId, String txType);

    InsightResponseDTO getInsightForAccount(String accountId, LocalDate date, InsightPreferenceDTO dto);

    List<TransactionDTO> getTransactionByAccountId(String accountId);

}
