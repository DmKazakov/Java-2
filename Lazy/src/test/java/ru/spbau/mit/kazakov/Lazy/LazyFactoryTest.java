package ru.spbau.mit.kazakov.Lazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyFactoryTest {
    @Test
    public void testSingleThreadConstructor() {
        LazyFactory.createSingleThreadLazy(() -> 5);
    }

    @Test
    public void testSingleThreadGet() {
        Lazy<String> singleThreadLazy = LazyFactory.createSingleThreadLazy(() -> ":" + "c");
        assertEquals(":c", singleThreadLazy.get());
    }

    @Test
    public void testSingleThreadEvaluateOnce() {
        Lazy<Object> singleThreadLazy = LazyFactory.createSingleThreadLazy(Object::new);
        Object firstEvaluation = singleThreadLazy.get();
        assertEquals(firstEvaluation, singleThreadLazy.get());
    }

    @Test
    public void testSingleThreadLazy() {
        List<Integer> list = Arrays.asList(56);
        Lazy<Integer> singleThreadLazy = LazyFactory.createSingleThreadLazy(() -> list.get(0));
        list.set(0, 11);
        assertEquals(Integer.valueOf(11), singleThreadLazy.get());
    }

    @Test
    public void testSingleThreadNullResult() {
        Lazy<Integer> singleThreadLazy = LazyFactory.createSingleThreadLazy(() -> null);
        assertNull(singleThreadLazy.get());
    }

    @Test
    public void testMultiThreadConstructor() {
        LazyFactory.createMultiThreadLazy(() -> 5);
    }

    @Test
    public void testMultiThreadGet() {
        Lazy<String> multiThreadLazy = LazyFactory.createMultiThreadLazy(() -> ":" + "c");
        Runnable getLazy = () -> assertEquals(":c", multiThreadLazy.get());
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(getLazy));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Test
    public void testMultiThreadEvaluateOnce() {
        Lazy<Object> multiThreadLazy = LazyFactory.createMultiThreadLazy(Object::new);
        Object firstEvaluation = multiThreadLazy.get();
        Runnable getLazy = () -> assertEquals(firstEvaluation, multiThreadLazy.get());
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(getLazy));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Test
    public void testMultiThreadLazy() {
        List<Integer> list = Arrays.asList(56);
        Lazy<Integer> multiThreadLazy = LazyFactory.createMultiThreadLazy(() -> list.get(0));
        list.set(0, 11);
        Runnable getLazy = () -> assertEquals(Integer.valueOf(11), multiThreadLazy.get());
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(getLazy));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Test
    public void testMultiThreadNullResult() {
        Lazy<Integer> multiThreadLazy = LazyFactory.createSingleThreadLazy(() -> null);
        Runnable getLazy = () -> assertNull(multiThreadLazy.get());
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(getLazy));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }
}