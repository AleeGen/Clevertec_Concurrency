package ru.clevertec.cuncurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cuncurrency.communication.Request;
import ru.clevertec.cuncurrency.communication.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ClientTest {
    private static final int dataCapacity = 100;
    private Client client;
    @Mock
    private static Server server;

    @BeforeEach
    void setUp() {
        client = new Client(server, dataCapacity);
    }

    @Test
    void checkCallShouldTakeAllDataRequest() throws InterruptedException {
        doReturn(new Response(0)).when(server).getResponse(any(Request.class));
        client.call();
        int actual = client.getDataRequest().size();
        assertThat(actual).isEqualTo(0);
    }

}