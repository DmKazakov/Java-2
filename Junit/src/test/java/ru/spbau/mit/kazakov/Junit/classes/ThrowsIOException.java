package ru.spbau.mit.kazakov.Junit.classes;

import ru.spbau.mit.kazakov.Junit.annotations.Test;

import java.io.IOException;

public class ThrowsIOException {
    @Test
    public void throwsIOException() throws IOException {
        throw new IOException();
    }
}
