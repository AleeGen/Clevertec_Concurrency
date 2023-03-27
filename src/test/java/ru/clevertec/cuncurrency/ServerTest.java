package ru.clevertec.cuncurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.cuncurrency.communication.Request;
import ru.clevertec.cuncurrency.communication.Response;
import ru.clevertec.cuncurrency.exception.ServerException;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

    private static Server server;
    private final int nRequests = 100;

    @BeforeEach
    void setUp() {
        server = new Server();
    }

    @Test
    void checkGetResponseShouldEqualSizeCommonResource() throws InterruptedException {
        Collection<Callable<Response>> tasks =
                Collections.nCopies(nRequests, () -> server.getResponse(new Request(0)));
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.invokeAll(tasks);
        int actual = server.getCommonResource().size();
        int expected = tasks.size();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetResponseShouldEqualNumberRequest() {
        AtomicInteger response = new AtomicInteger();
        Stream.generate(() -> response.getAndSet(server.getResponse(new Request(0)).number()))
                .limit(nRequests).count();
        int actual = response.get();
        assertThat(actual).isEqualTo(nRequests);
    }

    @Test
    void checkGetResponseShouldThrowServerException() throws InterruptedException {
        AtomicBoolean isException = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            try {
                server.getResponse(new Request(0));
            } catch (ServerException e) {
                isException.set(true);
            }
        });
        thread.start();
        thread.interrupt();
        thread.join();
        assertTrue(isException.get());
    }

}