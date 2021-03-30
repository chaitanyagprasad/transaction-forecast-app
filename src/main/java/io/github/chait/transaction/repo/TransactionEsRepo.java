package io.github.chait.transaction.repo;

import io.github.chait.transaction.docs.TransactionDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;
import java.util.*;

public interface TransactionEsRepo extends ElasticsearchRepository<TransactionDoc, String> {

    Page<TransactionDoc> findAllByTransactionType(String transactionType, Pageable pageable);

    Set<TransactionDoc> findAllByAccountIdAndTransactionType(String accountId, String transactionType);

    Set<TransactionDoc> findAllByAccountIdAndTransactionDateBetween(String accountId, LocalDate d1, LocalDate d2);

    List<TransactionDoc> findAllByAccountId(String accountId);

}
