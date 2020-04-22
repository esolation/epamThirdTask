package com.epamThirdTask.client;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class Client implements Runnable {
    private Semaphore sem;
    private boolean isActive = true;
    private int id;
    private boolean isService;
    private BlockingQueue<Client> clients;
    public volatile static AtomicInteger j = new AtomicInteger(0);

    private Logger log = Logger.getRootLogger();


    public int gettingId() {
        return id;
    }

    public boolean isServiced() {
        return isService;
    }

    public void setServiced(boolean serviced) {
        isService = serviced;
    }

    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public void setClients(BlockingQueue<Client> clients) {
        this.clients = clients;
    }

    public Client(Semaphore sem, int id) {
        this.id = id;
        this.sem = sem;
        //this.clients = clients;
        this.isService = false;

        PropertyConfigurator.configure("src/main/resources/log4j.properties");
    }

    private void service()  {
        isService = true;

        isActive = false;
        j.incrementAndGet();
    }

    private void canRecall()  {
        if(new Random().nextBoolean()) {
            log.info("Client " + id + " will recall");
            //set client to the end of the queue
            clients.remove(this);
            clients.add(this);
        }
        else
            isActive = true;
    }
    @Override
    public  void run() {

        try {
            while (isActive) {
                if (clients.take() != this){
                    canRecall();
                }
                sem.acquire();
                log.info("Client " + id + " is service now");
                service();
                log.info("Client " + id + " was successful serviced");
                sem.release();

            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }


}
