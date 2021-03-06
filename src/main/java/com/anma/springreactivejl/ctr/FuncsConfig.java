package com.anma.springreactivejl.ctr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class FuncsConfig {

    @Bean
    RouterFunction<ServerResponse> nested(NestedHandler nestedHandler) {

        var jsonRP = accept(APPLICATION_JSON).or(accept(APPLICATION_XML));
        RequestPredicate sseRP = accept(TEXT_EVENT_STREAM);

        return route()
                .nest(path("/nested"), builder -> builder
                        .nest(jsonRP, nestedBuilder -> nestedBuilder
                                .GET("/{pv}", nestedHandler::pathVariable)
                                .GET("", nestedHandler::noPathVariable)
                        )
                        .add(route(sseRP, nestedHandler::sse))
                )
                .build();

    }
}
