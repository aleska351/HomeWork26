package com.goncharenko;

import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConcurrency implements Runnable {
    private final LinkedBlockingQueue<Item> queue;
    private final ProducerObserver observer;

    public ProducerConcurrency(LinkedBlockingQueue<Item> queue, ProducerObserver observer) {
        this.queue = queue;
        this.observer = observer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Item item = produce();

                observer.onItemProduced(item);

                queue.put(item);

            } catch (InterruptedException e) {
                // нашу задачу прервали, уходим насовсем
                return;
            }
        }
    }

    private Item produce() throws InterruptedException {
        long workDuration = (long) (Math.random() * 200) + 100;
        Thread.sleep(workDuration);
        return new Item((int) (Math.random() * workDuration));
    }

    interface ProducerObserver {
        void onItemProduced(Item item);
    }
}
