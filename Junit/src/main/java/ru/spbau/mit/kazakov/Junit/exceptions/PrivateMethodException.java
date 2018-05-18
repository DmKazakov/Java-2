package ru.spbau.mit.kazakov.Junit.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a method with test annotation isn't public..
 */
public class PrivateMethodException extends Exception {
    public PrivateMethodException(@NotNull String message) {
        super(message);
    }
}
