package ru.spbau.mit.kazakov.Junit.classes;

import ru.spbau.mit.kazakov.Junit.annotations.Test;

import java.io.IOException;

public class UnexpectedExceptionTest {
    @Test(expected = IOException.class)
    public void test() {
        throw new NullPointerException();
    }
}
