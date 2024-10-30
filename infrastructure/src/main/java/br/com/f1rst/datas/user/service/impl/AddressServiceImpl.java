package br.com.f1rst.datas.user.service.impl;

import br.com.f1rst.datas.user.common.GatewayException;
import br.com.f1rst.datas.user.dto.AddressDto;
import br.com.f1rst.datas.user.router.AddressRouter;
import br.com.f1rst.datas.user.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRouter addressRouter;


    private <T> T executeRoute(RouteBuilder routeBuilder, String routeId, Object body) {
        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(routeBuilder);
            ctx.start();

            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                return producerTemplate.requestBody(routeId, body, (Class<T>) Object.class);
            }
        } catch (Exception ex) {
            log.error("Error processing route {}: {}", routeId, ex.getMessage(), ex);
            handleException(ex);
            throw new RuntimeException(ex.getCause().getMessage());
        }
    }

    private void handleException(Exception ex) {
        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
            if (cause instanceof HttpClientErrorException.BadRequest) {
                throw (HttpClientErrorException.BadRequest) cause;
            } else if (cause instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) cause;
            } else if (cause instanceof GatewayException) {
                throw (GatewayException) cause;
            }
        }
    }

    @Override
    public AddressDto findAddressByCep(String cep) {
        return executeRoute(addressRouter.findAddressByCep(), AddressRouter.ROUTE_FIND_ADDRESS_BY_CEP, cep);
    }

    @Override
    public void saveLog(Object[] objects) {
        executeRoute(addressRouter.saveLog(), AddressRouter.ROUTE_SAVE_LOG, objects);
    }
}