package br.com.f1rst.datas.user.router;

import br.com.f1rst.datas.user.exchange.AddressExchange;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddressRouter {

    private final AddressExchange addressExchange;

    public static final String ROUTE_FIND_ADDRESS_BY_CEP = "direct:findAddressByCep";
    public static final String ROUTE_SAVE_LOG = "direct:saveLog";

    public RouteBuilder getRoute(String route) {
        switch (route) {
            case ROUTE_FIND_ADDRESS_BY_CEP:
                return findAddressByCep();
            case ROUTE_SAVE_LOG:
                return saveLog();
            default:
                throw new IllegalArgumentException("Invalid route: " + route);
        }
    }

    public RouteBuilder findAddressByCep() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_FIND_ADDRESS_BY_CEP)
                        .bean(addressExchange, "findAddressByCep");
            }
        };
    }

    public RouteBuilder saveLog() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_SAVE_LOG)
                        .bean(addressExchange, "saveLog");
            }
        };
    }
}
