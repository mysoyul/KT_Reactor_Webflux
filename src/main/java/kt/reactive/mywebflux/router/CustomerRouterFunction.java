package kt.reactive.mywebflux.router;

import kt.reactive.mywebflux.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CustomerRouterFunction {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(CustomerHandler customerHandler) {
        //route() 메서드이 2번째 아규먼트 HandlerFunction
        // HandlerFunction의 추상메서드 reactor.core.publisher.Mono<T> handle(ServerRequest request)
        return RouterFunctions
                .route(GET("/router/r2customers"), customerHandler::getCustomers)
                .andRoute(GET("/router/r2customers/{id}"), customerHandler::getCustomer)
                .andRoute(POST("/router/r2customers"), customerHandler::saveCustomer)
                .andRoute(PUT("/router/r2customers/{id}"), customerHandler::updateCustomer);
//        return RouterFunctions.route(GET("/router/r2customers"), new HandlerFunction<ServerResponse>() {
//            @Override
//            public Mono<ServerResponse> handle(ServerRequest request) {
//                return customerHandler.getCustomers(request);
//            }
//        });
    }
}