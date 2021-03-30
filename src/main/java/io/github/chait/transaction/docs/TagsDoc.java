package io.github.chait.transaction.docs;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


public class TagsDoc {


    private String tagName;

    private boolean isRecurring = false;

}
