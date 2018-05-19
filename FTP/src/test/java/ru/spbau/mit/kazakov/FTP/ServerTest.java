package ru.spbau.mit.kazakov.FTP;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ServerTest {
    private static final int PORT = 6666;
    private Path testingPath;

    @Rule
    public final TemporaryFolder testingFolder = new TemporaryFolder();


    @Before
    public void initialize() {
        testingPath = testingFolder.getRoot().toPath();
    }

    @Test
    public void testConstructor() throws IOException {
        new Server(new ServerSocket(PORT));
    }

    @Test
    public void testListCorrectPath() throws IOException, InterruptedException {
        Files.createFile(Paths.get(testingPath.toString(), "file_name"));
        Files.createFile(Paths.get(testingPath.toString(), "another_file"));
        Files.createDirectory(Paths.get(testingPath.toString(), "dir_name"));

        List<FileDescription> expected = Arrays.asList(new FileDescription("file_name", false),
                new FileDescription("another_file", false),
                new FileDescription("dir_name", true));

        testList(testingPath.toString(), expected);
    }

    @Test
    public void testListIncorrectPath() throws IOException, InterruptedException {
        Files.createFile(Paths.get(testingPath.toString(), "file_name"));
        Files.createFile(Paths.get(testingPath.toString(), "another_file"));
        Files.createDirectory(Paths.get(testingPath.toString(), "dir_name"));

        List<FileDescription> expected = new ArrayList<>();

        testList(testingPath.toString() + File.separator + "file_name", expected);
    }

    @Test
    public void testGetCorrectPath() throws IOException, InterruptedException {
        Path path = Paths.get(testingPath.toString(), "file_name");
        Files.createFile(path);
        Files.write(path, "some bytes".getBytes(StandardCharsets.UTF_8));

        testGet(testingPath.toString() + File.separator + "file_name", "some bytes",
                "some bytes".getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void testGetIncorrectPath() throws IOException, InterruptedException {
        Path path = Paths.get(testingPath.toString(), "dir_name");
        Files.createDirectory(path);

        testGet(testingPath.toString() + File.separator + "dir_name", "", 0);
    }

    private void testList(@NotNull String path, @NotNull List<FileDescription> expected) throws IOException, InterruptedException {
        DataInputStream response = mockServer(path, Request.getCode(Request.LIST));
        int numberOfFiles = response.readInt();
        List<FileDescription> fileDescriptions = new ArrayList<>();
        for (int i = 0; i < numberOfFiles; i++) {
            String name = response.readUTF();
            boolean isDirectory = response.readBoolean();
            fileDescriptions.add(new FileDescription(name, isDirectory));
        }

        assertThat(fileDescriptions, containsInAnyOrder(expected.toArray()));
    }

    private void testGet(@NotNull String path, String expectedContent, int expectedSize) throws IOException, InterruptedException {
        DataInputStream response = mockServer(path, Request.getCode(Request.GET));
        int size = response.readInt();
        String content = response.readUTF();

        assertEquals(expectedContent, content);
        assertEquals(expectedSize, size);
    }

    @NotNull
    private DataInputStream mockServer(String path, int code) throws IOException, InterruptedException {
        ByteArrayOutputStream byteRequestStream = new ByteArrayOutputStream();
        DataOutputStream requestStream = new DataOutputStream(byteRequestStream);
        requestStream.writeInt(code);
        requestStream.writeUTF(path);
        requestStream.flush();

        InputStream in = new ByteArrayInputStream(byteRequestStream.toByteArray());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Socket clientSocket = mock(Socket.class);
        when(clientSocket.getOutputStream()).thenReturn(out);
        when(clientSocket.getInputStream()).thenReturn(in);

        ServerSocket serverSocket = mock(ServerSocket.class);
        when(serverSocket.accept()).thenReturn(clientSocket).thenThrow(new IOException());
        new Thread(() -> new Server(serverSocket).startServer()).start();
        Thread.sleep(1000); //wait for server's computation

        return new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
    }
}