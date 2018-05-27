package ru.spbau.mit.kazakov.Junit.classes;

import ru.spbau.mit.kazakov.Junit.annotations.Test;

public class IgnoredTest {
    @Test(ignore = "reason")
    public void test() throws Exception {
        throw new Exception();
    }
}
