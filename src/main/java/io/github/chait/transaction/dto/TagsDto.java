package io.github.chait.transaction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class TagsDto {

    private String name;
    private boolean isRecurring = false;

}
