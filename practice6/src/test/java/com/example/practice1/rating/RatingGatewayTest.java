package com.example.practice1.rating;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingGatewayTest {

    @Mock
    RatingClient client;

    private RatingClientProperties props(String baseUrl, Duration connect, Duration read) {
        return new RatingClientProperties(baseUrl, "key", connect, read);
    }

    @Test
    void returnsFallback_whenClientFails() {
        when(client.find(anyString(), anyString()))
                .thenThrow(new ResourceAccessException("boom"));
        RatingGateway gateway = new RatingGateway(client,
                props("http://unused", Duration.ofSeconds(1), Duration.ofSeconds(1)));

        RatingResult result = gateway.ratingFor("Ponyo");

        assertThat(result.source()).isEqualTo("FALLBACK");
        assertThat(result.rating()).isNull();
    }

    @Test
    void returnsFallback_whenRemoteServiceIsStopped() throws Exception {
        int deadPort;
        try (ServerSocket socket = new ServerSocket(0)) {
            deadPort = socket.getLocalPort();   // port is released afterwards: nothing listens on it
        }
        RatingClientProperties p = props("http://localhost:" + deadPort,
                Duration.ofSeconds(1), Duration.ofSeconds(1));
        RatingGateway gateway = new RatingGateway(new RatingClientConfig().ratingClient(p), p);

        assertThat(gateway.ratingFor("Ponyo").source()).isEqualTo("FALLBACK");
    }

    @Test
    void returnsFallback_whenRemoteIsSlowerThanReadTimeout() throws Exception {
        HttpServer slow = HttpServer.create(new InetSocketAddress(0), 0);
        slow.createContext("/", exchange -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });
        slow.start();
        try {
            RatingClientProperties p = props("http://localhost:" + slow.getAddress().getPort(),
                    Duration.ofSeconds(1), Duration.ofMillis(300));
            RatingGateway gateway = new RatingGateway(new RatingClientConfig().ratingClient(p), p);

            long start = System.nanoTime();
            RatingResult result = gateway.ratingFor("Ponyo");
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            assertThat(result.source()).isEqualTo("FALLBACK");
            assertThat(elapsedMs).isLessThan(1500);   // waited ~300 ms, not the full 2 s
        } finally {
            slow.stop(0);
        }
    }
}
