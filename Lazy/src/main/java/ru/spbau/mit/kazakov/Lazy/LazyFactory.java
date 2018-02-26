package ru.spbau.mit.kazakov.Lazy;


import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LazyFactory {
    @NotNull
    public static <T> Lazy<T> createSingleThreadLazy(@NotNull Supplier<T> supplier) {
        return new SingleThreadLazy<>(supplier);
    }

    @NotNull
    public static <T> Lazy<T> createMultiThreadLazy(@NotNull Supplier<T> supplier) {
        return new MultiThreadLazy<>(supplier);
    }

    private static class SingleThreadLazy<T> implements Lazy<T> {
        private Supplier<T> supplier;
        private T supplied;

        public SingleThreadLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if(supplier != null) {
                supplied = supplier.get();
                supplier = null;
            }

            return supplied;
        }
    }

    private static class MultiThreadLazy<T> implements Lazy<T> {
        private Supplier<T> supplier;
        private T supplied;

        public MultiThreadLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            //noinspection SynchronizeOnNonFinalField
            synchronized (supplied) {
                if (supplier != null) {
                    supplied = supplier.get();
                    supplier = null;
                }

                return supplied;
            }
        }
    }
}
