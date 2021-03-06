package com.anma.springreactivejl.ctr;

import com.anma.springreactivejl.model.Cat;
import com.anma.springreactivejl.srv.CatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CatHandlerFunction implements HandlerFunction<ServerResponse> {

    private final CatRepo catRepo;

    @Autowired
    public CatHandlerFunction(CatRepo catRepo) {
        this.catRepo = catRepo;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        var method = request.method();
        String methName = method.name();
        return ServerResponse.ok().bodyValue("Murz cat here!");
    }

    public Mono<ServerResponse> cats(ServerRequest request) {
        return ServerResponse.ok().bodyValue(catRepo.findAll().blockFirst());
    }
}
