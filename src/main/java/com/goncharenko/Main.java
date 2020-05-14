package com.goncharenko;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        //Queue<Item> sharedQueue = new LinkedList<>();
        LinkedBlockingQueue<Item> sharedQueue = new LinkedBlockingQueue<Item>();
        Supervisor supervisor = new Supervisor(sharedQueue);


        executor.execute(new Producer(sharedQueue, supervisor));
        executor.execute(new Producer(sharedQueue, supervisor));
        executor.execute(new Producer(sharedQueue, supervisor));
        executor.execute(new Producer(sharedQueue, supervisor));
        executor.execute(new Producer(sharedQueue, supervisor));
        executor.execute(new Producer(sharedQueue, supervisor));

        executor.execute(new Consumer(sharedQueue, supervisor));
        executor.execute(new Consumer(sharedQueue, supervisor));
        executor.execute(new Consumer(sharedQueue, supervisor));

        executor.execute(supervisor);
        executor.shutdown();
    }
}
