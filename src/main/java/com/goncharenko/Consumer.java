package com.goncharenko;

import java.util.Queue;

public class Consumer implements Runnable {
    private final Queue<Item> queue;
    private final ConsumerObserver observer;

    public Consumer(Queue<Item> queue, ConsumerObserver observer) {
        this.queue = queue;
        this.observer = observer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Item item;

                synchronized (queue) {
                    item = queue.poll();
                }

                if (item != null) {
                    consume(item);
                } else {
                    Thread.yield(); // позволим другим потокам поработать
                }

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
