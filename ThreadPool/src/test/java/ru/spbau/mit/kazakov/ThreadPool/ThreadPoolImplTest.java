package ru.spbau.mit.kazakov.ThreadPool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

public class ThreadPoolImplTest {
    @Test
    public void testConstructorAndShutdown() throws InterruptedException {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(20);
        threadPool.shutdown();
    }

    @Test
    public void testIsReady() throws LightExecutionException, InterruptedException {
        Supplier<Integer> task = () -> 404;
        ThreadPoolImpl threadPool = new ThreadPoolImpl(2);
        LightFuture<Integer> lightFuture = threadPool.addTask(task);
        lightFuture.get();
        Assert.assertTrue(lightFuture.isReady());
    }

    @Test
    public void testComputeSimple() throws LightExecutionException, InterruptedException {
        Supplier<String> task = () -> ":(";
        ThreadPoolImpl threadPool = new ThreadPoolImpl(5);
        LightFuture<String> lightFuture = threadPool.addTask(task);
        Assert.assertEquals(":(", lightFuture.get());
    }

    @Test(
            expected = LightExecutionException.class
    )
    public void testComputeThrowsException() throws LightExecutionException, InterruptedException {
        //noinspection NumericOverflow
        Supplier<Integer> task = () -> 5 / 0;
        ThreadPoolImpl threadPool = new ThreadPoolImpl(5);
        LightFuture<Integer> lightFuture = threadPool.addTask(task);
        lightFuture.get();
    }

    @Test
    public void testComputeMultipleTasks() throws LightExecutionException, InterruptedException {
        Object computationResult = new Object();
        Supplier<String> task1 = () -> {
            safeSleep();
            return ":(";
        };
        Supplier<Integer> task2 = () -> {
            safeSleep();
            return 101;
        };
        Supplier<Object> task3 = () -> {
            safeSleep();
            return computationResult;
        };
        Supplier<Character> task4 = () -> {
            safeSleep();
            return 'n';
        };
        Supplier<Byte> task5 = () -> {
            safeSleep();
            return (byte) 0;
        };

        ThreadPoolImpl threadPool = new ThreadPoolImpl(3);
        LightFuture<String> lightFuture1 = threadPool.addTask(task1);
        LightFuture<Integer> lightFuture2 = threadPool.addTask(task2);
        LightFuture<Object> lightFuture3 = threadPool.addTask(task3);
        LightFuture<Character> lightFuture4 = threadPool.addTask(task4);
        LightFuture<Byte> lightFuture5 = threadPool.addTask(task5);

        Assert.assertEquals(":(", lightFuture1.get());
        Assert.assertEquals(Integer.valueOf(101), lightFuture2.get());
        Assert.assertEquals(computationResult, lightFuture3.get());
        Assert.assertEquals(Character.valueOf('n'), lightFuture4.get());
        Assert.assertEquals(Byte.valueOf((byte) 0), lightFuture5.get());
    }

    @Test
    public void testThenApplySimple() throws LightExecutionException, InterruptedException {
        Supplier<Integer> supplier = () -> {
            this.safeSleep();
            return 999;
        };
        Function<Integer, String> function = String::valueOf;
        ThreadPoolImpl threadPool = new ThreadPoolImpl(3);
        Assert.assertEquals("999", threadPool.addTask(supplier).thenApply(function).get());
    }

    @Test
    public void testThenApplyAfterComputation() throws LightExecutionException, InterruptedException {
        Supplier<Integer> supplier = () -> 999;
        Function<Integer, String> function = String::valueOf;
        ThreadPoolImpl threadPool = new ThreadPoolImpl(3);
        LightFuture<Integer> lightFuture = threadPool.addTask(supplier);
        lightFuture.get();
        Assert.assertEquals("999", lightFuture.thenApply(function).get());
    }

    @Test
    public void testThenApplyMultiple() throws LightExecutionException, InterruptedException {
        Supplier<String> supplier = () -> {
            safeSleep();
            return "Genome-wide association";
        };
        Function<String, String> function1 = s -> s.concat(" study");
        Function<String, String> function2 = s -> s.replace("d", "ld");
        Function<String, String> function3 = s -> s.substring(0, 11);
        ThreadPoolImpl threadPool = new ThreadPoolImpl(3);

        LightFuture<String> lightFuture = threadPool.addTask(supplier);
        Assert.assertEquals("Genome-wide association study", lightFuture.thenApply(function1).get());
        Assert.assertEquals("Genome-wilde association", lightFuture.thenApply(function2).get());
        Assert.assertEquals("Genome-wide", lightFuture.thenApply(function3).get());
    }

    @Test
    public void testNumberOfThreads() throws InterruptedException {
        Set<Long> threadsId = new HashSet<>();
        Supplier<Void> supplier = () -> {
            synchronized (threadsId) {
                threadsId.add(Thread.currentThread().getId());
            }
            //noinspection InfiniteLoopStatement,StatementWithEmptyBody
            while (true) {
            }
        };
        ThreadPoolImpl threadPool = new ThreadPoolImpl(15);

        for (int i = 0; i < 100; ++i) {
            threadPool.addTask(supplier);
        }

        TimeUnit.MILLISECONDS.sleep(1500);
        Assert.assertEquals(15, threadsId.size());
    }

    private void safeSleep() {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException var2) {
            throw new RuntimeException();
        }
    }
}
