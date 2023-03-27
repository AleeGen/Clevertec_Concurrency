package ru.clevertec.cuncurrency;

import ru.clevertec.cuncurrency.communication.Request;
import ru.clevertec.cuncurrency.communication.Response;
import ru.clevertec.cuncurrency.exception.ServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    private final Lock lock = new ReentrantLock();
    private final List<Integer> commonResource;
    private final Random random = new Random();

    public Server() {
        commonResource = new ArrayList<>();
    }

    public List<Integer> getCommonResource() {
        return commonResource;
    }

    private int random() {
        return 100 + random.nextInt(900);
    }

    public Response getResponse(Request request) {
        try {
            lock.lock();
            Thread.sleep(random());
            commonResource.add(request.number());
            return new Response(commonResource.size());
        } catch (InterruptedException e) {
            throw new ServerException("Exception when executing the  " + Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }

}
