package ru.spbau.mit.kazakov.Junit.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when test class has no nullary constructor or represents an abstract class / an interface.
 */
public class NoNullaryConstructorException extends Exception {
    public NoNullaryConstructorException(@NotNull String message) {
        super(message);
    }
}
