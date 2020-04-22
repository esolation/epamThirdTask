package com.epamThirdTask;

import com.epamThirdTask.client.Client;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CallCenter {

    private Semaphore sem = new Semaphore(2,true);
    public void start(BlockingQueue<Client> clients){

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        clients.forEach(executorService::submit);
        executorService.shutdown();

    }


}
