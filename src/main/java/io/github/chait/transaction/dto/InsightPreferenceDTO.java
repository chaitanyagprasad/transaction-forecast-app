package io.github.chait.transaction.dto;

import lombok.*;

import java.util.Set;

@Getter@Setter
public class InsightPreferenceDTO {

    private String accountId;
    private Set<TransactionFlow> incomeFlows;
    private Set<TransactionFlow> expenditureFlows;

}
