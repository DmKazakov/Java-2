package ru.spbau.mit.kazakov.Junit;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * Represents result of a test execution.
 */
@Value
class TestResult {
    private final String aClass;
    private final String method;
    private final String result;
    private long time;

    public TestResult(@NotNull Method test, @NotNull String result, long time) {
        aClass = test.getDeclaringClass().getCanonicalName();
        method = test.getName();
        this.result = result;
        this.time = time;
    }

    @Override
    @NotNull
    public String toString() {
        return "Class: " + aClass + "\n" +
                "Method: " + method + "\n" +
                "Result: " + result + "\n" +
                "Time: " + time + " ns";
    }
}
