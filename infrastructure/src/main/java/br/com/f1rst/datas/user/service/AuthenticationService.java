package br.com.f1rst.datas.user.service;

import br.com.f1rst.datas.user.dto.UserDto;
import br.com.f1rst.datas.user.entity.UserEntity;

import java.util.Optional;

public interface AuthenticationService {

    Optional<UserEntity> login(UserDto userDto);




}
