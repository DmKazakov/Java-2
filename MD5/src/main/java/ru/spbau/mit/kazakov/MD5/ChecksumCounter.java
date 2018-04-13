package ru.spbau.mit.kazakov.MD5;

import com.sun.istack.internal.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * Class for counting checksum of files and directories using following rule:
 * f(file) = HashAlgorithm(<content>)
 * f(dir) = HashAlgorithm(<dir name> + f(file1) + ...)
 * Supporting algorithms: MD5, SHA-1, SHA-256.
 */
public class ChecksumCounter {
    private HashAlgorithm algorithm;
    private int bufferSize;
    private ForkJoinPool forkJoinPool;

    /**
     * Creates single thread checksum counter.
     *
     * @param bufferSize available size of buffer for files hashing
     * @param algorithm  for computation
     */
    public ChecksumCounter(int bufferSize, @NotNull HashAlgorithm algorithm) {
        this.bufferSize = bufferSize;
        this.algorithm = algorithm;
        forkJoinPool = null;
    }

    /**
     * Creates checksum counter with Fork-Join pool.
     *
     * @param bufferSize      available size of buffer for files hashing
     * @param algorithm       for computation
     * @param numberOfThreads in Fork-Join pool
     */
    public ChecksumCounter(int bufferSize, @NotNull HashAlgorithm algorithm, int numberOfThreads) {
        this.bufferSize = bufferSize;
        this.algorithm = algorithm;
        forkJoinPool = new ForkJoinPool(numberOfThreads);
    }

    /**
     * Computes hash from specified file/directory.
     *
     * @param filePath path to file/directory.
     * @return hash in string
     * @throws IllegalArgumentException if there is no such path
     * @throws IOException              if an exception occurred during working with filesystem
     */
    @NotNull
    public String getHash(@NotNull String filePath) throws Exception {
        return DatatypeConverter.printHexBinary(getHashBytes(filePath));
    }

    /**
     * Computes hash from specified file/directory.
     *
     * @param filePath path to file/directory.
     * @return hash in byte array
     * @throws IllegalArgumentException if there is no such path
     * @throws IOException              if an exception occurred during working with filesystem
     */
    @NotNull
    private byte[] getHashBytes(@NotNull String filePath) throws Exception {
        Path path;
        try {
            path = Paths.get(filePath);
        } catch (InvalidPathException exception) {
            throw new IllegalArgumentException("There is no such path: " + filePath);
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Algorithm " + algorithm.toString() + " isn't supported");
        }
        if (Files.isDirectory(path)) {
            updateDigestByDirectory(path, messageDigest);
        } else {
            updateDigestByFile(path, messageDigest);
        }
        return messageDigest.digest();
    }

    /**
     * Updates specified digest using specified directory.
     *
     * @param path          to specified directory
     * @param messageDigest specified digest
     * @throws IOException if an exception occurred during working with filesystem
     */
    private void updateDigestByDirectory(@NotNull Path path, @NotNull MessageDigest messageDigest) throws Exception {
        final List<Path> directoryContent = Files.list(path).collect(Collectors.toList());
        messageDigest.update(path.getFileName().toString().getBytes());
        if (forkJoinPool == null) {
            for (Path filePath : directoryContent) {
                messageDigest.update(getHashBytes(filePath.toString()));
            }
        } else {
            ArrayList<RecursiveTask<byte[]>> tasks = new ArrayList<>();
            for (Path filePath : directoryContent) {
                final RecursiveTask<byte[]> task = new RecursiveTask<byte[]>() {
                    @Override
                    protected byte[] compute() {
                        try {
                            return getHashBytes(filePath.toString());
                        } catch (Exception exception) {
                            return null;
                        }
                    }
                };
                tasks.add(task);
                forkJoinPool.execute(task);
            }
            for (RecursiveTask<byte[]> task : tasks) {
                try {
                    byte[] result = task.get();
                    if (result == null) {
                        throw new IOException();
                    }
                    messageDigest.update(result);
                } catch (Exception exception) {
                    throw new Exception("An error occurred during computation");
                }
            }
        }
    }

    /**
     * Updates specified digest using specified file.
     *
     * @param path          to file
     * @param messageDigest specified digest
     * @throws IOException if an exception occurred during working with filesystem
     */
    private void updateDigestByFile(@NotNull Path path, @NotNull MessageDigest messageDigest) throws IOException {
        byte[] buffer = new byte[bufferSize];
        try (InputStream inputStream = Files.newInputStream(path);
             DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest)
        ) {
            //noinspection StatementWithEmptyBody
            while (digestInputStream.read(buffer) > -1) {

            }
        }
    }
}
