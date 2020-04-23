package com.epamThirdTask;

import com.epamThirdTask.client.Client;


import java.util.concurrent.*;


public class CallCenter {

    public void start(BlockingQueue<Client> clients){

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        clients.forEach(executorService::submit);
        executorService.shutdown();

    }


}
