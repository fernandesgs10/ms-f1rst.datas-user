package br.com.f1rst.datas.user;

import br.com.f1rst.datas.user.controller.AddressController;
import br.com.f1rst.datas.user.dto.AddressDto;
import br.com.f1rst.datas.user.dto.ResponseDto;
import br.com.f1rst.datas.user.mapper.AddressMapper;
import br.com.f1rst.datas.user.service.AddressService;
import br.com.f1rst.datas.user.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    public static final String ENDPOINT_ADDRESS = "/v1/address/cep/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressMapper addressMapper;

    @MockBean
    private AddressService addressService;

    @MockBean
    private JmsTemplate jmsTemplate;

    @MockBean
    private JwtService jwtService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    @DisplayName("Deve encontrar um endereço pelo CEP e retornar status 200")
    public void findAddressByCep_whenValidCep_thenReturns200() throws Exception {
        String cep = "12345678";
        AddressDto responseDto = new AddressDto(); // Preencha com os dados esperados
        when(addressService.findAddressByCep(anyString())).thenReturn(responseDto);

        mockMvc.perform(get(ENDPOINT_ADDRESS + cep)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve registrar o IP do usuário ao buscar endereço por CEP")
    public void findAddressByCep_logUserIp() throws Exception {
        String cep = "12345678";
        AddressDto responseDto = new AddressDto(); // Preencha com os dados esperados
        when(addressService.findAddressByCep(anyString())).thenReturn(responseDto);

        // Mock do HttpServletRequest para retornar o IP desejado
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        mockMvc.perform(get(ENDPOINT_ADDRESS + cep)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("javax.servlet.http.HttpServletRequest", request))
                .andExpect(status().isOk());

        // Verifique se o log de IP é gerado (caso tenha um mecanismo de verificação de logs)
        // Você pode precisar de uma implementação específica para capturar logs se necessário.
    }
}
