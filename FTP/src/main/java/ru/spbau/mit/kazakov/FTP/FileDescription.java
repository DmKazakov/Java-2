package ru.spbau.mit.kazakov.FTP;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Class for representing information about files given by server.
 */
public class FileDescription {
    private String name;
    private boolean isDirectory;

    public FileDescription(@NotNull String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public FileDescription(@NotNull File file) {
        name = file.getName();
        isDirectory = file.isDirectory();
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileDescription) {
            FileDescription f = (FileDescription) o;
            return name.equals(f.name) && isDirectory == f.isDirectory;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name + " " + isDirectory;
    }
}
