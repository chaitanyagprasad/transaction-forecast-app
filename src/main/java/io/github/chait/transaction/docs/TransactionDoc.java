package io.github.chait.transaction.docs;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Document(indexName = "transaction_chait")
@Getter@Setter
public class TransactionDoc {


    @Id
    private String id;
    private String accountId;
    private String transactionType;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private LocalDate transactionDate;

    private String transactionSource;
    private String transactionDestination;
    private BigDecimal transactionAmount;
    private BigDecimal balance;
    private Set<String> tags;
}
