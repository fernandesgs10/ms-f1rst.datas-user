package br.com.f1rst.datas.user.mapper;

import br.com.f1rst.datas.user.dto.AddressDto;
import br.com.f1rst.datas.user.entity.LogAddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@Component
@AllArgsConstructor
public class AddressMapper implements Serializable {

    protected final ModelMapper modelMapper;

    public LogAddressEntity converterDtoObjectToEntity(AddressDto addressDto) {
        return modelMapper.map(addressDto, LogAddressEntity.class);
    }

    public AddressDto converterEntityObjectToDto(LogAddressEntity logAddressEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(logAddressEntity, AddressDto.class);
    }
}