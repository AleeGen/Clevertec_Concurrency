package ru.clevertec.cuncurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    private static final int dataCapacity = 100;
    private Client client;
    private Server server;

    @BeforeEach
    void setUp() {
        server = new Server();
        client = new Client(server, dataCapacity);
    }

    @Test
    void checkClientShouldReturnExpectedAccumulator() throws InterruptedException {
        int expected = (dataCapacity + 1) * dataCapacity / 2;
        int actual = client.call();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkServerShouldHaveExpectedData() throws InterruptedException {
        List<Integer> expected = IntStream.range(1, dataCapacity + 1).boxed().toList();
        client.call();
        List<Integer> actual = server.getCommonResource();
        assertTrue(CollectionUtils.isEqualCollection(actual, expected));
    }

}
