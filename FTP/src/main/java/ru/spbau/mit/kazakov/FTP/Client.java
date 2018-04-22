package ru.spbau.mit.kazakov.FTP;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Client fot file transfer.
 */
public class Client {
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * Initializes socket's streams.
     */
    public Client(@NotNull Socket socket) throws ConnectionException {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException exception) {
            throw new ConnectionException();
        }
    }

    /**
     * Sends request to server to list directory at specified path.
     *
     * @param path specified path
     * @return file's descriptions
     * @throws ConnectionException if IOException occurs
     */
    @NotNull
    public List<FileDescription> list(@NotNull String path) throws ConnectionException {
        try {
            out.writeInt(Request.getCode(Request.LIST));
            out.writeUTF(path);
            out.flush();

            int numberOfFiles = in.readInt();
            List<FileDescription> fileDescriptions = new ArrayList<>();
            for (int i = 0; i < numberOfFiles; i++) {
                String name = in.readUTF();
                boolean isDirectory = in.readBoolean();
                fileDescriptions.add(new FileDescription(name, isDirectory));
            }

            return fileDescriptions;
        } catch (IOException exception) {
            throw new ConnectionException();
        }
    }

    /**
     * Sends request to server to get file's content from path on the server and saves it at specified path.
     *
     * @param getFrom path to server's file
     * @param saveTo  path to save file
     * @throws ConnectionException if IOException occurs during work with socket's streams
     * @throws IOException         if unable to save file
     */
    public void get(@NotNull String getFrom, @NotNull String saveTo) throws ConnectionException, IOException {
        String fileContent;
        try {
            out.writeInt(Request.getCode(Request.GET));
            out.writeUTF(getFrom);
            out.flush();

            in.readInt();
            fileContent = in.readUTF();
        } catch (IOException exception) {
            throw new ConnectionException();
        }

        try (FileOutputStream outputStream = new FileOutputStream(saveTo)) {
            outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        }
    }

}
