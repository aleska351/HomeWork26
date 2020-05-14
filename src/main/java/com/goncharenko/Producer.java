package com.goncharenko;

import java.util.Queue;

public class Producer implements Runnable {
    private final Queue<Item> queue;
    private final ProducerObserver observer;

    public Producer(Queue<Item> queue, ProducerObserver observer) {
        this.queue = queue;
        this.observer = observer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Item item = produce();

                observer.onItemProduced(item);

                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (queue) {
                        if (queue.offer(item)) {
                            break;
                        }
                    }

                    Thread.yield(); // позволить другим потокам поработать
                }

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
