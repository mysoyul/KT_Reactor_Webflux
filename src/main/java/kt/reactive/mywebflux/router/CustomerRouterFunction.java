package kt.reactive.mywebflux.router;

import kt.reactive.mywebflux.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CustomerRouterFunction {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(CustomerHandler customerHandler) {
        //route() 메서드이 2번째 아규먼트 HandlerFunction
        // HandlerFunction의 추상메서드 reactor.core.publisher.Mono<T> handle(ServerRequest request)
        return RouterFunctions.route(GET("/router/r2customers"), customerHandler::getCustomers);
    }
}