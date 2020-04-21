package com.epamThirdTask.client;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;


public class Client extends Thread {
    private Semaphore sem;
    private boolean isActive = true;
    private int id;
    private boolean isHealed;
    private BlockingQueue<Client> clients;
    private Logger log = Logger.getRootLogger();


    public int gettingId() {
        return id;
    }

    public Client(Semaphore sem, BlockingQueue<Client> clients, int timeToService, int id) {
        this.id = id;
        this.sem = sem;
        this.clients = clients;
        this.isHealed = false;
        PropertyConfigurator.configure("E:\\java projects\\epamThirdTask\\epamThirdTask\\src\\main\\java\\resources\\log4j.properties");
    }

    private void service()  {
        isHealed = true;
    }

    private void canRecall()  {
        if(new Random().nextBoolean()) {
            log.info("Client " + id + " will recall");
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
                    if (isHealed) {
                        log.info("Client " + id + " was successful serviced");
                        isActive = false;
                        sem.release();
                    }
            }
            } catch(InterruptedException e){
                e.printStackTrace();
            }
    }
}
