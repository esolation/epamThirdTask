package com.epamThirdTask.client;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client extends Thread {
    private Semaphore sem;
    Lock lock = new ReentrantLock();
    private int id;
    private boolean isHealed;
    private BlockingQueue<Client> clients;

    private int timeToService;
    Logger log = Logger.getRootLogger();
    private int countOfRecall;

    public Client(Semaphore sem, BlockingQueue<Client> clients, int timeToService, int id) {
        this.id = id;
        this.sem = sem;
        this.timeToService = timeToService;
        this.clients = clients;
        this.countOfRecall = 3;
        this.isHealed = false;
        //PropertyConfigurator.configure("log4j.properties");
    }

    public void service() throws InterruptedException {
        if(timeToService < new Random().nextInt(1000)+1)
            isHealed = true;
        sleep(1000);

    }

    @Override
    public  void run() {
        try {
                if (!isHealed && countOfRecall > 0) {
                    sem.acquire();
                    //log.info("Client " + id + " is service now");
                    System.out.println("Client " + id + " is service now");
                    service();
                    if (isHealed) {
                        //log.info("Client was successful serviced");
                        System.out.println("Client " + id + " was successful serviced");
                        countOfRecall = 0;
                        clients.remove(this);
                    } else {
                        //log.info("Client will reconnect");
                        System.out.println("Client " + id + " will reconnect");
                        countOfRecall--;
                    }
                    sem.release();

                }

            } catch(InterruptedException e){
                e.printStackTrace();
            }



    }

}
