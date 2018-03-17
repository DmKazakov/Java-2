package ru.spbau.mit.kazakov.Lazy;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Returns two implementations of Lazy interface: single and multi thread safe implementations.
 */
public class LazyFactory {
    /**
     * Returns single thread implementation
     *
     * @param supplier evaluation
     * @param <T>      type of evaluated result
     */
    @NotNull
    public static <T> Lazy<T> createSingleThreadLazy(@NotNull Supplier<T> supplier) {
        return new SingleThreadLazy<>(supplier);
    }

    /**
     * Returns multi thread implementation
     *
     * @param supplier evaluation
     * @param <T>      type of evaluated result
     */
    @NotNull
    public static <T> Lazy<T> createMultiThreadLazy(@NotNull Supplier<T> supplier) {
        return new MultiThreadLazy<>(supplier);
    }

    /**
     * A single thread implementation of Lazy interface.
     *
     * @param <T> type of evaluated value
     */
    private static class SingleThreadLazy<T> implements Lazy<T> {
        private Supplier<T> supplier;
        private T supplied;

        /**
         * Initializes supplier for lazy evaluation.
         */
        public SingleThreadLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * Makes evaluation at the first call and saves result.
         *
         * @return result of evaluation
         */
        @Override
        @Nullable
        public T get() {
            if (supplier != null) {
                supplied = supplier.get();
                supplier = null;
            }

            return supplied;
        }
    }

    /**
     * A multi thread implementation of Lazy interface.
     *
     * @param <T> type of evaluated value
     */
    private static class MultiThreadLazy<T> implements Lazy<T> {
        private volatile Supplier<T> supplier;
        private T supplied;

        /**
         * Initializes supplier for lazy evaluation.
         */
        public MultiThreadLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * Makes evaluation at the first call and saves result.
         *
         * @return result of evaluation
         */
        @Override
        @Nullable
        public T get() {
            if (supplier == null) {
                return supplied;
            }
            synchronized (this) {
                if (supplier != null) {
                    supplied = supplier.get();
                    supplier = null;
                }

            }
            return supplied;
        }
    }
}
