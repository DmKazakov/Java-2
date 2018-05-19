package ru.spbau.mit.kazakov.FTP;


import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Server for file transfer.
 */
public class Server {
    private final ServerSocket serverSocket;
    private static final int NUMBER_OF_THREADS = 4;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Initializes server socket.
     */
    public Server(@NotNull ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Creates server socket, waits for client and handles it's requests.
     */
    public void startServer() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (IOException exception) {
                //nothing to do
            }
        }
    }

    /**
     * Handles client's requests.
     */
    private class ClientHandler implements Runnable {
        private Socket client;

        /**
         * Initializes client's socket.
         */
        public ClientHandler(@NotNull Socket client) {
            this.client = client;
        }

        /**
         * Request-Response cycle.
         */
        @Override
        public void run() {
            try (DataInputStream in = new DataInputStream(client.getInputStream());
                 DataOutputStream out = new DataOutputStream(client.getOutputStream())) {
                while (true) {
                    Request request = Request.getQuery(in.readInt());
                    String path = in.readUTF();
                    switch (request) {
                        case LIST:
                            Future<List<FileDescription>> listTask = threadPool.submit(new ListQueryHandler(path));
                            List<FileDescription> directoryContent = listTask.get();
                            out.writeInt(directoryContent.size());
                            for (FileDescription file : directoryContent) {
                                out.writeUTF(file.getName());
                                out.writeBoolean(file.isDirectory());
                            }
                            out.flush();
                            break;
                        case GET:
                            Future<String> getTask = threadPool.submit(new GetQueryHandler(path));
                            String fileContent = getTask.get();
                            out.writeInt(fileContent.getBytes(StandardCharsets.UTF_8).length);
                            out.writeUTF(fileContent);
                            out.flush();
                            break;
                    }
                }
            } catch (Exception exception) {
                try {
                    client.close();
                } catch (IOException e) {
                    //nothing to do
                }
            }
        }
    }

    /**
     * Handles list request.
     */
    private static class ListQueryHandler implements Callable<List<FileDescription>> {
        private String path;

        /**
         * Initializes path to directory fot listing.
         */
        private ListQueryHandler(@NotNull String path) {
            this.path = path;
        }

        /**
         * List files in directory.
         *
         * @return files in directory
         */
        @Override
        @NotNull
        public List<FileDescription> call() {
            File directory = new File(path);
            List<FileDescription> fileDescriptions = new ArrayList<>();

            if (directory.isDirectory()) {
                File[] directoryContent = directory.listFiles();
                if (directoryContent != null) {
                    for (File file : directoryContent) {
                        fileDescriptions.add(new FileDescription(file));
                    }
                }
            }

            return fileDescriptions;
        }
    }

    /**
     * Handles get request.
     */
    private static class GetQueryHandler implements Callable<String> {
        private String path;

        /**
         * Initializes path to file.
         */
        private GetQueryHandler(@NotNull String path) {
            this.path = path;
        }

        /**
         * Gets file content
         */
        @Override
        @NotNull
        public String call() {
            byte[] data = new byte[0];
            Path path = Paths.get(this.path);

            if (Files.exists(path) && Files.isRegularFile(path)) {
                try {
                    data = Files.readAllBytes(path);
                } catch (IOException e) {
                    //Nothing to do
                }
            }

            return new String(data, StandardCharsets.UTF_8);
        }
    }
}
