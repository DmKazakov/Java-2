package ru.spbau.mit.kazakov.Junit.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when test class has no public constructors.
 */
public class PrivateConstructorException extends Exception {
    public PrivateConstructorException(@NotNull String message) {
        super(message);
    }
}
