package com.be.base.email;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailTemplate {
    private String receiver;
    private String subject;
    private String content;

}
