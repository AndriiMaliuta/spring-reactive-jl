package com.anma.springreactivejl.rsock.pp;

import io.rsocket.Payload;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public record Ping(BootifulProperties properties) {

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {

        var socket = RSocketConnector
                .create()
                .reconnect(Retry.backoff(50, Duration.ofMillis(500)))
                .connect(TcpClientTransport.create(this.properties.getHost(),
                        this.properties.getPort()));

        RSocketClient
                .from(socket)
                .requestChannel(Flux.interval(Duration.ofSeconds(1)).map(i -> DefaultPayload.create("Hello @ " + i)))
                .map(Payload::getDataUtf8)//
                .take(10)
                .subscribe();

    }
}
