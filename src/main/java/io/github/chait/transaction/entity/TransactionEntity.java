package io.github.chait.transaction.entity;

import io.github.chait.transaction.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "transaction")
@Getter@Setter
public class TransactionEntity {




    private String accountId;


    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;


    private LocalDate transactionDate;


    private String transactionSource;


    private String transactionDestination;


    private BigDecimal transactionAmount;


    private BigDecimal balance;


    private String tags;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
