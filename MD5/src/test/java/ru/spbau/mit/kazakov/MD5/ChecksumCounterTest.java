package ru.spbau.mit.kazakov.MD5;

import com.sun.istack.internal.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


public class ChecksumCounterTest {
    @Rule
    public final TemporaryFolder testingFolder = new TemporaryFolder();

    private Path testingPath;

    @Before
    public void initialize() throws Exception {
        testingPath = testingFolder.getRoot().toPath();
    }

    @Test
    public void testSingleThreadConstructor() {
        new ChecksumCounter(2048, HashAlgorithm.MD5);
    }

    @Test
    public void testMultiThreadConstructor() {
        new ChecksumCounter(2048, HashAlgorithm.MD5, 3);
    }

    @Test
    public void testGetHashFileSingleThread() throws Exception {
        Path path = Paths.get(testingPath.toString(), "not_empty_file");
        Files.createFile(path);
        Files.write(path, "Not empty".getBytes());

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5);

        assertEquals(countFileChecksum(path), checksumCounter.getHash(path.toString()));
    }

    @Test
    public void testGetHashFileMultiThread() throws Exception {
        Path path = Paths.get(testingPath.toString(), "not_empty_file");
        Files.createFile(path);
        Files.write(path, "Not empty".getBytes());

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5, 3);

        assertEquals(countFileChecksum(path), checksumCounter.getHash(path.toString()));
    }

    @Test
    public void testGetHashEmptyFileSingleThread() throws Exception {
        Path path = Paths.get(testingPath.toString(), "empty_file");
        Files.createFile(path);

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5);

        assertEquals(countFileChecksum(path), checksumCounter.getHash(path.toString()));
    }

    @Test
    public void testGetHashEmptyFileMultiThread() throws Exception {
        Path path = Paths.get(testingPath.toString(), "empty_file");
        Files.createFile(path);

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5, 4);

        assertEquals(countFileChecksum(path), checksumCounter.getHash(path.toString()));
    }

    @Test
    public void testGetHashDirectorySingleThread() throws Exception {
        Path folderPath = Paths.get(testingPath.toString(), "not_empty");
        Files.createDirectory(folderPath);
        Path path = Paths.get(folderPath.toString(), "not_empty_file");
        Files.createFile(path);
        Files.write(path, "Not empty".getBytes());

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5);

        assertEquals(countOneDepthFolderChecksum(folderPath), checksumCounter.getHash(folderPath.toString()));
    }

    @Test
    public void testGetHashDirectoryMultiThread() throws Exception {
        Path folderPath = Paths.get(testingPath.toString(), "not_empty");
        Files.createDirectory(folderPath);
        Path path = Paths.get(folderPath.toString(), "not_empty_file");
        Files.createFile(path);
        Files.write(path, "Not empty".getBytes());

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5, 2);

        assertEquals(countOneDepthFolderChecksum(folderPath), checksumCounter.getHash(folderPath.toString()));
    }

    @Test
    public void testGetHashEmptyDirectorySingleThread() throws Exception {
        Path folderPath = Paths.get(testingPath.toString(), "empty");
        Files.createDirectory(folderPath);

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5);

        assertEquals(countStringChecksum("empty"), checksumCounter.getHash(folderPath.toString()));
    }

    @Test
    public void testGetHashEmptyDirectoryMultiThread() throws Exception {
        Path folderPath = Paths.get(testingPath.toString(), "empty");
        Files.createDirectory(folderPath);

        ChecksumCounter checksumCounter = new ChecksumCounter(2048, HashAlgorithm.MD5);

        assertEquals(countStringChecksum("empty"), checksumCounter.getHash(folderPath.toString()));
    }

    @NotNull
    private String countFileChecksum(@NotNull Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[2048];

        try (InputStream inputStream = Files.newInputStream(path);
             DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest)
        ) {
            //noinspection StatementWithEmptyBody
            while (digestInputStream.read(buffer) > -1) {

            }
        }

        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }

    @NotNull
    private String countStringChecksum(@NotNull String string) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(string.getBytes());

        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }

    @NotNull
    private String countOneDepthFolderChecksum(@NotNull Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(path.getFileName().toString().getBytes());

        for(Path filePath : Files.list(path).collect(Collectors.toList())) {
            byte[] buffer = new byte[2048];

            try (InputStream inputStream = Files.newInputStream(filePath);
                 DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest)
            ) {
                //noinspection StatementWithEmptyBody
                while (digestInputStream.read(buffer) > -1) {

                }
            }
        }

        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }
}