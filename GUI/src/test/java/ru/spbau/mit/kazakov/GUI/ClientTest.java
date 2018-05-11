package ru.spbau.mit.kazakov.GUI;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
    private Path testingPath;

    @Rule
    public final TemporaryFolder testingFolder = new TemporaryFolder();


    @Before
    public void initialize() {
        testingPath = testingFolder.getRoot().toPath();
    }

    @Test
    public void testConstructor() throws ConnectionException {
        new Client(mock(Socket.class));
    }

    @Test
    public void testList() throws IOException, ConnectionException {
        ByteArrayOutputStream byteResponseStream = new ByteArrayOutputStream();
        DataOutputStream responseStream = new DataOutputStream(byteResponseStream);
        responseStream.writeInt(3);
        responseStream.writeUTF("some dir");
        responseStream.writeBoolean(true);
        responseStream.writeUTF("some file");
        responseStream.writeBoolean(false);
        responseStream.writeUTF("another file");
        responseStream.writeBoolean(false);
        responseStream.flush();

        InputStream in = new ByteArrayInputStream(byteResponseStream.toByteArray());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);

        Client client = new Client(socket);
        List<FileDescription> fileDescriptions = client.list("some path");

        DataInputStream requestStream = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        int code = requestStream.readInt();
        String path = requestStream.readUTF();
        assertEquals(Request.getCode(Request.LIST), code);
        assertEquals("some path", path);

        List<FileDescription> expected = Arrays.asList(new FileDescription("some dir", true),
                new FileDescription("some file", false),
                new FileDescription("another file", false));
        assertThat(fileDescriptions, containsInAnyOrder(expected.toArray()));
    }

    @Test
    public void testGet() throws IOException, ConnectionException {
        ByteArrayOutputStream byteResponseStream = new ByteArrayOutputStream();
        DataOutputStream responseStream = new DataOutputStream(byteResponseStream);
        responseStream.writeInt("some content".getBytes(StandardCharsets.UTF_8).length);
        responseStream.writeUTF("some content");
        responseStream.flush();

        InputStream in = new ByteArrayInputStream(byteResponseStream.toByteArray());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);

        Path path = Paths.get(testingPath.toString(), "file_name");
        Files.createFile(path);

        Client client = new Client(socket);
        client.get("some path", path.toString());

        DataInputStream requestStream = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        int code = requestStream.readInt();
        String requestPath = requestStream.readUTF();
        assertEquals(Request.getCode(Request.GET), code);
        assertEquals("some path", requestPath);

        assertArrayEquals("some content".getBytes(), Files.readAllBytes(path));
    }
}