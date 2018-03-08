package ru.spbau.mit.kazakov.ThreadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.xml.ws.Holder;

import org.jetbrains.annotations.NotNull;

/**
 * A class for computation in multiple threads.
 */
public class ThreadPoolImpl {
    private final Thread[] threads;
    private final Queue<Task<?>> tasksQueue = new LinkedList<>();

    /**
     * Creates specified number of threads for computation.
     */
    public ThreadPoolImpl(int numberOfThreads) {
        this.threads = new Thread[numberOfThreads];
        Runnable threadCycle = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                Task task;
                synchronized (tasksQueue) {
                    while (this.tasksQueue.isEmpty()) {
                        try {
                            tasksQueue.wait();
                        } catch (InterruptedException exception) {
                            return;
                        }
                    }
                    task = tasksQueue.remove();
                }

                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (task) {
                    task.compute();
                    task.notify();
                }
            }

        };

        for (int i = 0; i < numberOfThreads; ++i) {
            this.threads[i] = new Thread(threadCycle);
            this.threads[i].start();
        }

    }

    /**
     * Adds a new task to queue.
     *
     * @param task a computation
     * @param <T>  type of computation result
     * @return added task
     */
    @NotNull
    public <T> LightFuture<T> addTask(@NotNull Supplier<T> task) {
        Task<T> lightFuture = new Task<>(task);
        synchronized (tasksQueue) {
            tasksQueue.add(lightFuture);
            tasksQueue.notify();
        }
        return lightFuture;
    }

    /**
     * Sends terminating signals to all threads.
     */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    /**
     * An implementation of LightFuture for using in {@link ThreadPoolImpl}.
     *
     * @param <T> type of computation result.
     */
    private class Task<T> implements LightFuture<T> {
        private final List<Task<?>> deferredTasks = new ArrayList<>();
        private Supplier<T> computation;
        private boolean isReady;
        private boolean isSuccessful = true;
        private Holder<T> result;

        /**
         * Initializes supplier for computation.
         */
        public Task(@NotNull Supplier<T> computation) {
            this.computation = computation;
        }

        /**
         * Makes computation, saves result and adds deferred task to queue.
         */
        public void compute() {
            try {
                result = new Holder<>(computation.get());
            } catch (RuntimeException exception) {
                isSuccessful = false;
            }

            isReady = true;
            synchronized (tasksQueue) {
                tasksQueue.addAll(this.deferredTasks);
                tasksQueue.notify();
            }
        }

        /**
         * @see LightFuture#isReady()
         */
        public boolean isReady() {
            return isReady;
        }

        /**
         * @see LightFuture#get()
         */
        public T get() throws LightExecutionException, InterruptedException {
            if (!this.isReady) {
                synchronized (this) {
                    while (!this.isReady) {
                        this.wait();
                    }
                }
            }

            if (!this.isSuccessful) {
                throw new LightExecutionException();
            } else {
                return this.result.value;
            }
        }

        /**
         * @see LightFuture#thenApply(Function)
         */
        @NotNull
        public synchronized <U> LightFuture<U> thenApply(@NotNull Function<T, U> function) throws LightExecutionException {
            if (!isSuccessful) {
                throw new LightExecutionException();
            }

            Supplier<U> computation = () -> function.apply(result.value);
            if (isReady) {
                return addTask(computation);
            } else {
                Task<U> task = new Task<>(computation);
                deferredTasks.add(task);
                return task;
            }
        }
    }
}

