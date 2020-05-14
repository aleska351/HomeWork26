package com.goncharenko;

import java.util.concurrent.LinkedBlockingQueue;

public class ConsumerConcurrency implements Runnable {
    private final LinkedBlockingQueue<Item> queue;
    private final ConsumerObserver observer;

    public ConsumerConcurrency(LinkedBlockingQueue<Item> queue, ConsumerObserver observer) {
        this.queue = queue;
        this.observer = observer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Item item;

                item = queue.take();
                consume(item);
            } catch (InterruptedException e) {
                // нас попытались прервать, уходим насовсем
                return;
            }
        }
    }

    private void consume(Item item) throws InterruptedException {
        int workDuration = (int) (item.getValue() * Math.random());

        Thread.sleep(workDuration);

        observer.onItemConsumed(item);
    }

    interface ConsumerObserver {
        void onItemConsumed(Item item);
    }
}
