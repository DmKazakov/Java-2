package ru.spbau.mit.kazakov.Junit.classes;

import ru.spbau.mit.kazakov.Junit.annotations.Test;

import java.io.FileNotFoundException;

public class ThrowsExpectedException {
    @Test(expected = FileNotFoundException.class)
    public void throwsExpectedException() throws FileNotFoundException {
        throw new FileNotFoundException();
    }

}
