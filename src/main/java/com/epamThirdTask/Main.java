package com.epamThirdTask;

import com.epamThirdTask.client.Client;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Client> clients = new LinkedBlockingDeque<>();
        Semaphore operators = new Semaphore(2);

        for (int i = 0; i < 10 ; i++) {
            clients.add(new Client(operators,clients, new Random().nextInt(1000)+1, i));
        }
        clients.forEach(Thread::start);
    }
}
