package ru.spbau.mit.kazakov.MD5;

import com.sun.istack.internal.NotNull;

/**
 * Console application for counting checksum.
 */
public class Main {
    private static final int NUMBER_OF_THREADS = 5;
    private static final HashAlgorithm ALGORITHM = HashAlgorithm.MD5;
    private static final int BUFFER_SIZE = 2048;

    public static void main(@NotNull String[] args) {
        if (args.length == 0) {
            System.out.println("No directory specified");
        } else {
            computeHashes(args[0]);
        }
    }

    /**
     * Computes hashes of specified directory/file using multi and single thread algorithms.
     *
     * @param path to directory/file
     */
    public static void computeHashes(@NotNull String path) {
        ChecksumCounter singleThreadChecksumCounter = new ChecksumCounter(BUFFER_SIZE, ALGORITHM);
        ChecksumCounter multiThreadChecksumCounter = new ChecksumCounter(BUFFER_SIZE, ALGORITHM, NUMBER_OF_THREADS);

        try {
            long start = System.currentTimeMillis();
            System.out.println(singleThreadChecksumCounter.getHash(path));
            System.out.println("Computed using single thread algorithm in" + (System.currentTimeMillis() - start) + "msecs");

            start = System.currentTimeMillis();
            System.out.println(multiThreadChecksumCounter.getHash(path));
            System.out.println("Computed using multi thread algorithm in" + (System.currentTimeMillis() - start) + "msecs");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
