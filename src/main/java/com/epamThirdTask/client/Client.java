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
    private AtomicInteger countOfRecall = new AtomicInteger(3);
    private volatile static AtomicInteger j = new AtomicInteger(0);

    private Logger log = Logger.getRootLogger();

    public static AtomicInteger getJ() {
        return j;
    }

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

    private void canRecall() throws InterruptedException {
        if(new Random().nextInt(3)+1 <= 1) {
            log.info("Client " + id + " will recall");
            countOfRecall.decrementAndGet();
            Thread.sleep(20);
            log.info("Client " + id + " is reconnected");
        }
        else
            Thread.sleep(40);
    }
    @Override
    public  void run() {
        log.info("Client " + id + " in queue now");
        while (isActive) {

            if (sem.tryAcquire()) {
                log.info("Client " + id + " is service now");
                service();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("Client " + id + " was successful serviced");
                sem.release();
            }
            else if(countOfRecall.get() >=1) {
                try {
                    canRecall();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
