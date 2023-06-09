package kt.reactive.mywebflux.handler;

import kt.reactive.mywebflux.entity.Customer;
import kt.reactive.mywebflux.exception.CustomAPIException;
import kt.reactive.mywebflux.repository.R2CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final R2CustomerRepository customerRepository;
    private Mono<ServerResponse> response406 =
            ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build(); //HttpStatus.Series.CLIENT_ERROR
    public Mono<ServerResponse> getCustomers(ServerRequest request) {
        Flux<Customer> customerFlux = customerRepository.findAll();
        return ServerResponse.ok() //ServerResponse.BodyBuilder
                .contentType(MediaType.APPLICATION_JSON) //ServerResponse.BodyBuilder
                .body(customerFlux, Customer.class); //Mono<ServerResponse>
    }
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return customerRepository.findById(id)
                .flatMap(customer -> ServerResponse.ok()  //ServerResponse.BodyBuilder
                        .contentType(MediaType.APPLICATION_JSON)  //ServerResponse.BodyBuilder
                        .body(BodyInserters.fromValue(customer))
                ).switchIfEmpty(getError(id));
    }
    private Mono<ServerResponse> getError(Long id) {
        return Mono.error(new CustomAPIException("Customer Not Found with id " + id, HttpStatus.NOT_FOUND));
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<Customer> unSavedCustomerMono = request.bodyToMono(Customer.class);
        return unSavedCustomerMono.flatMap(customer ->
                customerRepository.save(customer) //Mono<Customer>
                        .flatMap(savedCustomer ->
                                ServerResponse.accepted() //ACCEPTED: 202
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(savedCustomer)
                        )
        ).switchIfEmpty(response406);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Customer> unUpdatedCustomerMono = request.bodyToMono(Customer.class);

        Mono<Customer> updatedCustomerMono = unUpdatedCustomerMono.flatMap(customer ->
                customerRepository.findById(id)
                        .flatMap(existCustomer -> {
                            existCustomer.setFirstName(customer.getFirstName());
                            existCustomer.setLastName(customer.getLastName());
                            return customerRepository.save(existCustomer);
                        })
        ).switchIfEmpty(Mono.error(new CustomAPIException("Customer Not Found Id " + id, HttpStatus.NOT_FOUND)));

        return updatedCustomerMono.flatMap(customer ->
                        ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(customer));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<ServerResponse> serverResponseMono = customerRepository.findById(id)
                .flatMap(existCustomer ->
                        ServerResponse.ok()
                                .build(customerRepository.delete(existCustomer)))
                .switchIfEmpty(getError(id));
        return serverResponseMono;

    }

}