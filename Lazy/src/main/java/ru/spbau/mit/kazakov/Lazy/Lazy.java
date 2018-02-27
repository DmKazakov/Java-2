package ru.spbau.mit.kazakov.Lazy;

/**
 * Generic interface for lazy evaluation.
 * @param <T> type of evaluating value
 */
public interface Lazy<T> {

    /**
     * Returns result of lazy evaluation.
     */
    T get();
}
