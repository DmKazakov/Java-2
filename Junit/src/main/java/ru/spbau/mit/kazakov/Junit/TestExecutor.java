package ru.spbau.mit.kazakov.Junit;

import lombok.Getter;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.kazakov.Junit.annotations.*;
import ru.spbau.mit.kazakov.Junit.exceptions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class for executing tests of loaded classes.
 */
public class TestExecutor {
    private List<Class<?>> loadedClasses;
    @Getter
    private int total;
    @Getter
    private int failed;
    @Getter
    private int passed;
    @Getter
    private int ignored;
    @Getter
    private List<TestResult> results = new ArrayList<>();

    /**
     * Initializes list of loaded classes.
     */
    public TestExecutor(@NotNull List<Class<?>> loadedClasses) {
        this.loadedClasses = loadedClasses;
    }

    /**
     * Executes tests of loaded classes.
     *
     * @throws NoNullaryConstructorException when a class has no nullary constructor or represents an abstract class / an interface
     * @throws PrivateConstructorException   when a class has no public constructors
     * @throws PrivateMethodException        when a method isn't public
     * @throws NonNullaryMethodException     when a method isn't nullary
     * @throws MethodInvocationException     when when a method has thrown an exception
     */
    @NotNull
    public void executeTests() throws NoNullaryConstructorException, PrivateConstructorException,
            PrivateMethodException, NonNullaryMethodException, MethodInvocationException {
        for (Class<?> aClass : loadedClasses) {
            results.addAll(processClass(aClass));
        }
    }

    /**
     * Invoke methods of specified class with testing annotations.
     *
     * @param aClass specified class
     * @return results of tests
     * @throws NoNullaryConstructorException when a class has no nullary constructor or represents an abstract class / an interface
     * @throws PrivateConstructorException   when a class has no public constructors
     * @throws PrivateMethodException        when a method isn't public
     * @throws NonNullaryMethodException     when a method isn't nullary
     * @throws MethodInvocationException     when when a method has thrown an exception
     */
    @NotNull
    private List<TestResult> processClass(@NotNull Class<?> aClass) throws NoNullaryConstructorException,
            PrivateConstructorException, PrivateMethodException, NonNullaryMethodException, MethodInvocationException {
        List<Method> before = getAnnotatedMethods(aClass, Before.class);
        List<Method> after = getAnnotatedMethods(aClass, After.class);
        List<Method> beforeClass = getAnnotatedMethods(aClass, BeforeClass.class);
        List<Method> afterClass = getAnnotatedMethods(aClass, AfterClass.class);
        List<Method> tests = getAnnotatedMethods(aClass, Test.class);

        Object instance;
        try {
            instance = aClass.newInstance();
        } catch (InstantiationException e) {
            throw new NoNullaryConstructorException("Class " + aClass.getCanonicalName() +
                    " has no nullary constructor or represents an abstract class / an interface.");
        } catch (IllegalAccessException e) {
            throw new PrivateConstructorException("Constructor of class " + aClass.getCanonicalName() + " must be public.");
        }

        invokeMethods(instance, beforeClass);
        List<TestResult> results = new ArrayList<>();
        for (Method method : tests) {
            invokeMethods(instance, before);
            results.add(executeTest(instance, method));
            invokeMethods(instance, after);
        }
        invokeMethods(instance, afterClass);

        return results;
    }

    /**
     * Executes specified test method.
     *
     * @param test specified test method
     * @return test result
     * @throws PrivateMethodException    when test isn't public
     * @throws NonNullaryMethodException when test isn't nullary
     */
    @NotNull
    private TestResult executeTest(@NotNull Object instance, @NotNull Method test) throws PrivateMethodException,
            NonNullaryMethodException {
        total++;
        Test annotation = test.getAnnotation(Test.class);
        String ignoreReason = annotation.ignore();
        Class<?> expectedException = annotation.expected();

        if (!ignoreReason.equals(Test.DEFAULT_IGNORE_REASON)) {
            ignored++;
            String result = "Reason for ignoring: " + ignoreReason + ".";
            return new TestResult(test, result, 0);
        }

        Throwable thrownException = null;
        StopWatch clock = new StopWatch();
        clock.start();
        try {
            test.invoke(instance);
        } catch (IllegalAccessException exception) {
            throw new PrivateMethodException("Method " + test.getName() + " of class "
                    + test.getDeclaringClass().getCanonicalName() + " must be public.");
        } catch (IllegalArgumentException exception) {
            throw new NonNullaryMethodException("Method " + test.getName() + " of class "
                    + test.getDeclaringClass().getCanonicalName() + " must take no arguments.");
        } catch (InvocationTargetException exception) {
            thrownException = exception.getTargetException();
        } finally {
            clock.stop();
        }

        long time = clock.getTime(TimeUnit.NANOSECONDS);
        String result;
        if (thrownException == null) {
            if (!expectedException.equals(Test.DefaultExpectedException.class)) {
                failed++;
                result = "Expected " + expectedException.getName() + " wasn't thrown.";
            } else {
                passed++;
                result = "Passed.";
            }
        } else if (thrownException.getClass().equals(annotation.expected())) {
            passed++;
            result = "Passed.";
        } else {
            failed++;
            if (!expectedException.equals(Test.DefaultExpectedException.class)) {
                result = "Expected " + expectedException.getName() + ", but "
                        + thrownException.getClass().getName() + " was thrown.";
            } else {
                result = thrownException.getClass().getName() + " was thrown.";
            }
        }

        return new TestResult(test, result, time);
    }

    /**
     * Invokes specified methods on specified object.
     *
     * @param instance specified object
     * @param toInvoke specified methods
     * @throws PrivateMethodException    when a method isn't public
     * @throws NonNullaryMethodException when a method isn't nullary
     * @throws MethodInvocationException when when a method has thrown an exception
     */
    private void invokeMethods(@NotNull Object instance, @NotNull List<Method> toInvoke) throws
            PrivateMethodException,
            NonNullaryMethodException, MethodInvocationException {
        for (Method method : toInvoke) {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException exception) {
                throw new PrivateMethodException("Method " + method.getName() + " of class "
                        + method.getDeclaringClass().getCanonicalName() + " must be public.");
            } catch (InvocationTargetException exception) {
                throw new MethodInvocationException("Method " + method.getName() + " of class "
                        + method.getDeclaringClass().getCanonicalName() + " has thrown an exception.");
            } catch (IllegalArgumentException exception) {
                throw new NonNullaryMethodException("Method " + method.getName() + " of class "
                        + method.getDeclaringClass().getCanonicalName() + " must take no arguments.");
            }
        }

    }

    /**
     * Searches for methods of specified class annotated with specified annotation.
     *
     * @param aClass     specified class
     * @param annotation specified annotation
     * @return found methods
     */
    @NotNull
    private List<Method> getAnnotatedMethods(@NotNull Class<?> aClass, @NotNull Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();

        while (aClass != Object.class) {
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return methods;
    }
}
