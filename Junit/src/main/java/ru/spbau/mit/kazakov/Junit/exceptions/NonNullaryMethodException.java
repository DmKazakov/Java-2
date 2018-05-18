package ru.spbau.mit.kazakov.Junit.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a method with test annotation isn't nullary.
 */
public class NonNullaryMethodException extends Exception {
    public NonNullaryMethodException(@NotNull String message) {
        super(message);
    }
}
