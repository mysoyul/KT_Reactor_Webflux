package kt.reactive.mywebflux.router;

import kt.reactive.mywebflux.entity.Customer;
import kt.reactive.mywebflux.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
public class CustomerRouterFunction {

    @RouterOperations({
            @RouterOperation(path = "/router/r2customers", method = RequestMethod.GET,
                    beanClass = CustomerHandler.class, beanMethod = "getCustomers"),
            @RouterOperation(path = "/router/r2customers/{id}", method = RequestMethod.GET,
                    beanClass = CustomerHandler.class, beanMethod = "getCustomer",
                    operation = @Operation(operationId = "getCustomer",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})
            ),
            @RouterOperation(path = "/router/r2customers", method = RequestMethod.POST,
                    beanClass = CustomerHandler.class, beanMethod = "saveCustomer",
                    operation = @Operation(operationId = "saveCustomer",
                            requestBody = @RequestBody(content =
                            @Content(schema = @Schema(implementation = Customer.class))))
            ),
            @RouterOperation(path = "/router/r2customers/{id}", method = RequestMethod.PUT,
                    beanClass = CustomerHandler.class, beanMethod = "updateCustomer",
                    operation = @Operation(operationId = "updateCustomer",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id")},
                            requestBody = @RequestBody(content =
                            @Content(schema = @Schema(implementation = Customer.class))))
            ),
            @RouterOperation(path = "/router/r2customers/{id}", method = RequestMethod.DELETE,
                    beanClass = CustomerHandler.class, beanMethod = "deleteCustomer",
                    operation = @Operation(operationId = "deleteCustomer",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(CustomerHandler customerHandler) {
        //route() 메서드이 2번째 아규먼트 HandlerFunction
        // HandlerFunction의 추상메서드 reactor.core.publisher.Mono<T> handle(ServerRequest request)
        return RouterFunctions
                .route(GET("/router/r2customers"), customerHandler::getCustomers)
                .andRoute(GET("/router/r2customers/{id}"), customerHandler::getCustomer)
                .andRoute(POST("/router/r2customers"), customerHandler::saveCustomer)
                .andRoute(PUT("/router/r2customers/{id}"), customerHandler::updateCustomer)
                .andRoute(DELETE("/router/r2customers/{id}"), customerHandler::deleteCustomer);
//        return RouterFunctions.route(GET("/router/r2customers"), new HandlerFunction<ServerResponse>() {
//            @Override
//            public Mono<ServerResponse> handle(ServerRequest request) {
//                return customerHandler.getCustomers(request);
//            }
//        });
    }
}