package ru.spbau.mit.kazakov.Junit.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated method will be executed by {@link ru.spbau.mit.kazakov.Junit.TestExecutor}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    String DEFAULT_IGNORE_REASON = "DEFAULT";

    /**
     * Specifies type of expected exception.
     */
    @NotNull
    Class<? extends Throwable> expected() default DefaultExpectedException.class;

    /**
     * Specifies reason for annotated method to be ignored.
     */
    @NotNull
    String ignore() default DEFAULT_IGNORE_REASON;

    /**
     * Default value for expected exception.
     */
    class DefaultExpectedException extends Throwable {

    }
}
