package ru.spbau.mit.kazakov.ThreadPool;

import java.util.function.Function;

/**
 * An interface representing a task from {@link ThreadPoolImpl}.
 *
 * @param <T> type of computation result
 */
public interface LightFuture<T> {
    /**
     * Returns true if computation is completed, and false otherwise.
     */
    boolean isReady();

    /**
     * Returns result of computation.
     *
     * @throws LightExecutionException if an exception was thrown during computation
     */
    T get() throws LightExecutionException, InterruptedException;

    /**
     * Creates a new task for {@link ThreadPoolImpl} using specified function, that requires computation result of current task.
     *
     * @param function specified function
     * @param <U>      type of computation result of new task
     * @return created task
     * @throws LightExecutionException if an exception was thrown during computation
     */
    <U> LightFuture<U> thenApply(Function<T, U> function) throws LightExecutionException;
}