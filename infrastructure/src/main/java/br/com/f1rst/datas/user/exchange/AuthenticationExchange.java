package br.com.f1rst.datas.user.exchange;

import br.com.f1rst.datas.user.dto.UserDto;
import br.com.f1rst.datas.user.entity.UserEntity;

import java.util.Optional;

public interface AuthenticationExchange {

    Optional<UserEntity> login(UserDto userDto);






}
