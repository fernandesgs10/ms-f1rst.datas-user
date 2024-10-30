package br.com.f1rst.datas.user.router;

import br.com.f1rst.datas.user.exchange.AuthenticationExchange;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationRouter {

    private final AuthenticationExchange accountPayExchange;

    public static final String ROUTE_LOGIN = "direct:login";

    public RouteBuilder getRoute(String route) {
        switch (route) {
            case ROUTE_LOGIN:
                return login();
            default:
                throw new IllegalArgumentException("Invalid route: " + route);
        }
    }

    public RouteBuilder login() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_LOGIN)
                        .bean(accountPayExchange, "login");
            }
        };
    }
}
