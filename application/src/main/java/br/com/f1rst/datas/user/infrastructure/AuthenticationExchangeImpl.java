package br.com.f1rst.datas.user.infrastructure;

import br.com.f1rst.datas.user.Utils.EncryptionPwUtil;
import br.com.f1rst.datas.user.dto.UserDto;
import br.com.f1rst.datas.user.entity.UserEntity;
import br.com.f1rst.datas.user.exchange.AuthenticationExchange;
import br.com.f1rst.datas.user.repository.AuthenticationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationExchangeImpl implements AuthenticationExchange {

    private final AuthenticationRepository authenticationRepository;

    @Override
    public Optional<UserEntity> login(UserDto userDto) {
        String encryptPassword = EncryptionPwUtil.encrypt(userDto.getPassword());
        return authenticationRepository.findByEmailAndPassword(userDto.getEmail(), encryptPassword);
    }
}
