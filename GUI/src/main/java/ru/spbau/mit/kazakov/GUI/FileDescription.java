package ru.spbau.mit.kazakov.GUI;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Class for representing information about files given by server.
 */
public class FileDescription {
    private String path;
    private boolean isDirectory;

    public FileDescription(@NotNull String path, boolean isDirectory) {
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public FileDescription(@NotNull File file) {
        path = file.getAbsolutePath();
        isDirectory = file.isDirectory();
    }

    @NotNull
    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileDescription) {
            FileDescription f = (FileDescription) o;
            return path.equals(f.path) && isDirectory == f.isDirectory;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return path + " " + isDirectory;
    }
}
