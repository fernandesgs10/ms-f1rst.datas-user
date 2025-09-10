package br.com.f1rst.datas.user.infrastructure;

import br.com.f1rst.datas.user.config.MensagemComponent;
import br.com.f1rst.datas.user.dto.AddressDto;
import br.com.f1rst.datas.user.entity.LogAddressEntity;
import br.com.f1rst.datas.user.exchange.AddressExchange;
import br.com.f1rst.datas.user.repository.LogAddressRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressExchangeImpl implements AddressExchange {

    private final MensagemComponent mensagemComponent;

    private final ObjectMapper objectMapper;

    @Value("${url-client-cep}")
    private String urlClientCep;
    private final LogAddressRepository logAddressRepository;

    @Override
    public AddressDto findAddressByCep(String cep) throws JsonProcessingException {
        Preconditions.checkArgument(cep != null,
                mensagemComponent.getMessage(
                        "accountpayment.id.notnull"
                ));

        return objectMapper
                .readValue(buscarCep(cep), AddressDto.class);
    }

    @Override
    public void saveLog(Object[] obj) throws JsonProcessingException {
        log.info("Iniciando saveLog com objeto: {}", Arrays.toString(obj));

        AddressDto addressDto;
        if (obj[0] instanceof LinkedHashMap) {
            log.debug("Convertendo LinkedHashMap para AddressDto");
            addressDto = objectMapper.convertValue(obj[0], AddressDto.class);
            log.debug("Conversão concluída: {}", addressDto);
        } else if (obj[0] instanceof AddressDto) {
            log.debug("Objeto já é do tipo AddressDto");
            addressDto = (AddressDto) obj[0];
            log.debug("AddressDto: {}", addressDto);
        } else {
            log.error("Objeto não é do tipo esperado. Tipo recebido: {}", obj[0].getClass().getName());
            throw new IllegalArgumentException("Objeto não é do tipo esperado AddressDto ou LinkedHashMap.");
        }

        log.debug("Convertendo AddressDto para JSON");
        String json = objectMapper.writeValueAsString(addressDto);
        log.debug("JSON gerado (tamanho: {} caracteres): {}", json.length(), json);

        String user = (String) obj[1];
        String ip = (String) obj[2];
        log.debug("Usuário: {}, IP: {}", user, ip);

        log.debug("Criando entidade LogAddressEntity");
        LogAddressEntity logAddressEntity = new LogAddressEntity();
        logAddressEntity.setData(json);
        logAddressEntity.setNmCreated(user);
        logAddressEntity.setIp(ip);
        log.debug("Entidade criada: {}", logAddressEntity);

        try {
            log.info("Salvando log no banco de dados");
            logAddressRepository.save(logAddressEntity);
            log.info("Log salvo com sucesso. ID: {}", logAddressEntity.getId());
        } catch (Exception e) {
            log.error("Erro ao salvar log no banco de dados", e);
            log.error("Dados que causaram o erro - JSON: {}", json);
            log.error("Tamanho do JSON: {}", json.length());
            log.error("Usuário: {}, IP: {}", user, ip);
            throw e;
        }
    }

    public String buscarCep(String cep) {
        String json;
        try {
            URL url = new URL(urlClientCep  + cep + "/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonSb = new StringBuilder();
            br.lines().forEach(l -> jsonSb.append(l.trim()));
            json = jsonSb.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return json;
    }
}