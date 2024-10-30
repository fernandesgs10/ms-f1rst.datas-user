package br.com.f1rst.datas.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeAccountEnum {

    CURRENT(1, "Current"),
    SAVE(2, "Save");

    private final Integer code;
    private final String desc;

}
