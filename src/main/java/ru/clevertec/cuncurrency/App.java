package ru.clevertec.cuncurrency;

import java.util.concurrent.*;

public class App {

    private static final int nTasks = 10000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Server server = new Server();
        Client client = new Client(server, nTasks);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(client);
        System.out.println("Result accumulator: " + future.get());
        executor.shutdown();
    }

}
