package br.com.f1rst.datas.user.service;

import br.com.f1rst.datas.user.dto.AddressDto;

public interface AddressService {


    AddressDto findAddressByCep(String cep);

    void saveLog(Object[] objDatas);


}
