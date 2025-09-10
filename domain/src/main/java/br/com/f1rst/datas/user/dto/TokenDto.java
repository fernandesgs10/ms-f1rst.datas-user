package br.com.f1rst.datas.user.dto;

import lombok.Data;

@Data
public class TokenDto {

    private String token;

    private String expiresIn;


}