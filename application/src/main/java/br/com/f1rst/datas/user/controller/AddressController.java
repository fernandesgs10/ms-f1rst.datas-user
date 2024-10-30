package br.com.f1rst.datas.user.controller;

import br.com.f1rst.datas.user.dto.ResponseDto;
import br.com.f1rst.datas.user.dto.ResponseTemplateDto;
import br.com.f1rst.datas.user.service.AddressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("v1/address")
@RequiredArgsConstructor
public class AddressController extends AbstractController {

    private final AddressService addressService;
    private final JmsTemplate jmsTemplate;


    @GetMapping("/cep/{cep}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation(value = "find Address by cep", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<ResponseDto> findAddressByCep(@PathVariable("cep") String cep, HttpServletRequest request) {
        log.info("find cep by: {}", cep);

        String userIpAddress = request.getRemoteAddr();
        log.info("User IP: {}", userIpAddress);

        String username = null;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) auth.getPrincipal()).getUsername();
        }
        log.info("Authenticated user: {}", username);
        var addressByCep = addressService.findAddressByCep(cep);

        Object objDatas[] = {addressByCep, username, userIpAddress};
        jmsTemplate.convertAndSend("address-log", objDatas);

        ResponseDto response = ResponseTemplateDto.createResponse(addressByCep, HttpStatus.OK);
        log.info("find cep successfully: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}