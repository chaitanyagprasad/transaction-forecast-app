package io.github.chait.transaction.dto;

import io.github.chait.transaction.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data@EqualsAndHashCode
public class TransactionDTO {

    private String accountId;
    private TransactionType transactionType;
    private LocalDate transactionDate;
    private String transactionSource;
    private String transactionDestination;
    private BigDecimal transactionAmount;
    private BigDecimal balance;
    private Set<String> tags;

}
