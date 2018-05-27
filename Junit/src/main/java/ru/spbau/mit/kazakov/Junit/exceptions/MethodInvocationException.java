package ru.spbau.mit.kazakov.Junit.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when when a method with test annotation has thrown an exception.
 */
public class MethodInvocationException extends Exception {
    public MethodInvocationException(@NotNull String message) {
        super(message);
    }
}
