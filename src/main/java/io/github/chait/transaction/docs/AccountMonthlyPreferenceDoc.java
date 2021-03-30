package io.github.chait.transaction.docs;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.chait.transaction.dto.TransactionFlow;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.Set;

@Document(indexName = "account_preference_chait")
@Getter@Setter
public class AccountMonthlyPreferenceDoc {

    @Id
    private String accountId;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private LocalDate lastPreferenceSetDate;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<TransactionFlow> incomeFlows;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<TransactionFlow> expenditureFlows;

}
