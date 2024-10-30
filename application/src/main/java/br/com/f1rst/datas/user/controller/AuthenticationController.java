package br.com.f1rst.datas.user.controller;

import br.com.f1rst.datas.user.Utils.UserUtil;
import br.com.f1rst.datas.user.dto.ResponseDto;
import br.com.f1rst.datas.user.dto.ResponseTemplateDto;
import br.com.f1rst.datas.user.dto.TokenDto;
import br.com.f1rst.datas.user.dto.UserDto;
import br.com.f1rst.datas.user.entity.UserEntity;
import br.com.f1rst.datas.user.service.AuthenticationService;
import br.com.f1rst.datas.user.service.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@SuppressWarnings({"rawtypes"})
@Slf4j
@RestController
@RequestMapping(value = "v1/authentication")
@AllArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "login", authorizations = {@Authorization(value = "OAuth2")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Call successfully made"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 500, message = "Unknown error")
    })
    public ResponseEntity<ResponseDto> login(@RequestBody UserDto loginDto) {
        log.info("Starting login process for {}", loginDto);

        Optional<UserEntity> authMongoOptional = authenticationService.login(loginDto);
        return authMongoOptional
                .map(this::getResponseDtoResponseEntity)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ResponseTemplateDto.createResponse(null, HttpStatus.UNAUTHORIZED)));
    }


    public ResponseEntity<ResponseDto> getResponseDtoResponseEntity(UserEntity user) {
        if (user == null) {
            ResponseDto response = ResponseTemplateDto.createResponse(null, HttpStatus.UNAUTHORIZED);
            log.info("Authentication UNAUTHORIZED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String jwtToken = jwtService.generateToken(user);
        TokenDto loginResponse = new TokenDto();

        loginResponse.setToken(jwtToken);
        String expiresIn = UserUtil.convertMillisToHours(jwtService.getExpirationTime());
        loginResponse.setExpiresIn(expiresIn);

        ResponseDto response = ResponseTemplateDto.createResponse(loginResponse, HttpStatus.OK);
        log.info("User authenticated successfully");
        return ResponseEntity.ok(response);
    }

}