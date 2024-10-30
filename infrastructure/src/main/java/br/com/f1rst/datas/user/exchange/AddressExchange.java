package br.com.f1rst.datas.user.exchange;

import br.com.f1rst.datas.user.dto.AddressDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AddressExchange {


    AddressDto findAddressByCep(String cep) throws JsonProcessingException;

    void saveLog(Object[] obj) throws JsonProcessingException;




}
