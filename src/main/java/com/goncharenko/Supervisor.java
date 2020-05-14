package com.goncharenko;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Supervisor implements Runnable,
        Consumer.ConsumerObserver,
        Producer.ProducerObserver {

    private final Queue<?> queue;
    private final AtomicInteger consumedCount = new AtomicInteger(0);
    private final AtomicInteger producedCount = new AtomicInteger(0);
    private int lastConsumedCount = 0;
    private int lastProducedCount = 0;

    public Supervisor(Queue<?> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1_000L);
                printStats();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void printStats() {
        final int currentConsumedCount = consumedCount.get();
        final int diffConsumedCount = currentConsumedCount - lastConsumedCount;
        lastConsumedCount = currentConsumedCount;

        final int currentProducedCount = producedCount.get();
        final int diffProducedCount = currentProducedCount - lastProducedCount;
        lastProducedCount = currentProducedCount;

        int queueSize;

        synchronized (queue) {
            queueSize = queue.size();
        }

        System.out.println("Size: " + queueSize);
        System.out.println(String.format("Consume speed: %d i/s", diffConsumedCount));
        System.out.println(String.format("Produce speed: %d i/s", diffProducedCount));
    }

    @Override
    public void onItemConsumed(Item item) {
        consumedCount.incrementAndGet();
    }

    @Override
    public void onItemProduced(Item item) {
        producedCount.incrementAndGet();
    }
}
