package br.com.f1rst.datas.user.service.impl;

import br.com.f1rst.datas.user.common.GatewayException;
import br.com.f1rst.datas.user.dto.UserDto;
import br.com.f1rst.datas.user.entity.UserEntity;
import br.com.f1rst.datas.user.router.AuthenticationRouter;
import br.com.f1rst.datas.user.service.AuthenticationService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationRouter authenticationRouter;

    @Override
    public Optional<UserEntity> login(UserDto userDto) {
        return executeRoute(authenticationRouter.login(), userDto);
    }

    private Optional<UserEntity> executeRoute(RouteBuilder routeBuilder, Object body) {
        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(routeBuilder);
            ctx.start();

            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                Object response = producerTemplate.requestBody(AuthenticationRouter.ROUTE_LOGIN, body);

                if (response instanceof Optional) {
                    @SuppressWarnings("unchecked")
                    Optional<UserEntity> result = (Optional<UserEntity>) response;
                    return result;
                } else if (response instanceof UserEntity) {
                    return Optional.of((UserEntity) response);
                } else {
                    log.warn("Unexpected response type from route {}: {}", AuthenticationRouter.ROUTE_LOGIN,
                            response != null ? response.getClass().getName() : "null");
                    return Optional.empty();
                }
            }
        } catch (Exception ex) {
            log.error("Error processing route {}: {}", AuthenticationRouter.ROUTE_LOGIN, ex.getMessage(), ex);
            handleException(ex);
            throw new GatewayException(ex.getMessage());
        }
    }

    private void handleException(Exception ex) {
        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
            if (cause instanceof HttpClientErrorException.BadRequest) {
                throw (HttpClientErrorException.BadRequest) cause;
            } else if (cause instanceof FeignException) {
                throw (FeignException) cause;
            }
        }
    }
}
