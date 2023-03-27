package ru.clevertec.cuncurrency;

import ru.clevertec.cuncurrency.communication.Request;
import ru.clevertec.cuncurrency.communication.Response;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Client implements Callable<Integer> {
    private final Lock lock = new ReentrantLock();
    private final Server server;
    private final List<Integer> dataRequest;
    private final AtomicInteger accumulator = new AtomicInteger();
    private final Random random = new Random();
    private final int maxWaitTime = 10;
    private final int nThreads = 5;

    public Client(Server server, int dataCapacity) {
        this.server = server;
        dataRequest = new ArrayList<>(IntStream.range(1, dataCapacity + 1).boxed().toList());
    }

    public List<Integer> getDataRequest() {
        return dataRequest;
    }

    private Request getRequest() {
        lock.lock();
        try {
            int randomNRequest = random.nextInt(dataRequest.size());
            return new Request(dataRequest.remove(randomNRequest));
        } finally {
            lock.unlock();
        }
    }

    private void give(Response response) {
        accumulator.addAndGet(response.number());
    }

    @Override
    public Integer call() throws InterruptedException {
        Collection<Callable<Object>> tasks = Collections.nCopies(dataRequest.size(),
                Executors.callable(() -> give(server.getResponse(getRequest()))));
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        executor.invokeAll(tasks, maxWaitTime, TimeUnit.SECONDS);
        executor.shutdown();
        return accumulator.get();
    }

}