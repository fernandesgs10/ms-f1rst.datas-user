package br.com.f1rst.datas.user.enums;

public enum TypeEmailEnum {
    WELCOME,
    RESET;

    public static TypeEmailEnum fromString(String type) {
        try {
            return TypeEmailEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown operation type: " + type, e);
        }
    }
}

