package ru.spbau.mit.kazakov.Junit;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.mit.kazakov.Junit.exceptions.NoNullaryConstructorException;
import ru.spbau.mit.kazakov.Junit.exceptions.NonNullaryMethodException;
import ru.spbau.mit.kazakov.Junit.exceptions.PrivateConstructorException;
import ru.spbau.mit.kazakov.Junit.exceptions.PrivateMethodException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TestExecutorTest {
    private static final File RESOURCES_FOLDER = new File("src/test/resources");
    private static final String PACKAGE_NAME = "ru.spbau.mit.kazakov.Junit.classes";
    private static ClassLoader RESOURCES_CLASS_LOADER;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void initClassLoader() throws MalformedURLException {
        RESOURCES_CLASS_LOADER = new URLClassLoader(new URL[]{RESOURCES_FOLDER.toURI().toURL()});
    }

    @Test
    public void testFailedTest() throws Exception {
        String testClassName = "ThrowsIOException";
        TestExecutor testExecutor = executeTest(testClassName);

        List<TestResult> results = testExecutor.getResults();
        TestResult result = results.get(0);

        assertStats(1, 1, 0, 0, testExecutor);
        assertEquals(1, results.size());
        assertResult(PACKAGE_NAME + "." + testClassName, "throwsIOException",
                "java.io.IOException was thrown.", result);
    }

    @Test
    public void testIgnoredTest() throws Exception {
        String testClassName = "IgnoredTest";
        TestExecutor testExecutor = executeTest(testClassName);

        List<TestResult> results = testExecutor.getResults();
        TestResult result = results.get(0);

        assertStats(1, 0, 0, 1, testExecutor);
        assertEquals(1, results.size());
        assertResult(PACKAGE_NAME + "." + testClassName, "test",
                "Reason for ignoring: reason.", result);
    }

    @Test
    public void testPassedTest() throws Exception {
        String testClassName = "PassedTest";
        TestExecutor testExecutor = executeTest(testClassName);

        List<TestResult> results = testExecutor.getResults();
        TestResult result = results.get(0);

        assertStats(1, 0, 1, 0, testExecutor);
        assertEquals(1, results.size());
        assertResult(PACKAGE_NAME + "." + testClassName, "test",
                "Passed.", result);
    }

    @Test
    public void testExpectedExceptionTest() throws Exception {
        String testClassName = "ThrowsExpectedException";
        TestExecutor testExecutor = executeTest(testClassName);

        List<TestResult> results = testExecutor.getResults();
        TestResult result = results.get(0);

        assertStats(1, 0, 1, 0, testExecutor);
        assertEquals(1, results.size());
        assertResult(PACKAGE_NAME + "." + testClassName, "throwsExpectedException",
                "Passed.", result);
    }

    @Test
    public void testUnexpectedExceptionTest() throws Exception {
        String testClassName = "UnexpectedExceptionTest";
        TestExecutor testExecutor = executeTest(testClassName);

        List<TestResult> results = testExecutor.getResults();
        TestResult result = results.get(0);

        assertStats(1, 1, 0, 0, testExecutor);
        assertEquals(1, results.size());
        assertResult(PACKAGE_NAME + "." + testClassName, "test",
                "Expected java.io.IOException, but java.lang.NullPointerException was thrown.", result);
    }

    @Test
    public void testAnnotationsTest() throws Exception {
        String testClassName = "AnnotationsTest";
        TestExecutor testExecutor = executeTest(testClassName);

        List<TestResult> results = testExecutor.getResults();

        assertStats(3, 0, 3, 0, testExecutor);
        assertEquals(3, results.size());
    }

    @Test
    public void testNoNullaryConstructorTest() throws Exception {
        exception.expect(NoNullaryConstructorException.class);
        exception.expectMessage("Class ru.spbau.mit.kazakov.Junit.classes.NoNullaryConstructor has no nullary constructor"
                + " or represents an abstract class / an interface.");

        String testClassName = "NoNullaryConstructor";
        TestExecutor testExecutor = executeTest(testClassName);
        //noinspection ResultOfMethodCallIgnored
        testExecutor.getResults();
    }

    @Test
    public void testNotNullaryTest() throws Exception {
        exception.expect(NonNullaryMethodException.class);
        exception.expectMessage("Method test of class ru.spbau.mit.kazakov.Junit.classes.NotNullaryTest must take no arguments.");

        String testClassName = "NotNullaryTest";
        TestExecutor testExecutor = executeTest(testClassName);
        //noinspection ResultOfMethodCallIgnored
        testExecutor.getResults();
    }

    @Test
    public void testPrivateConstructorTest() throws Exception {
        exception.expect(PrivateConstructorException.class);
        exception.expectMessage("Constructor of class ru.spbau.mit.kazakov.Junit.classes.PrivateConstructor must be public.");

        String testClassName = "PrivateConstructor";
        TestExecutor testExecutor = executeTest(testClassName);
        //noinspection ResultOfMethodCallIgnored
        testExecutor.getResults();
    }

    @Test
    public void testPrivateTest() throws Exception {
        exception.expect(PrivateMethodException.class);
        exception.expectMessage("Method test of class ru.spbau.mit.kazakov.Junit.classes.PrivateTest must be public.");

        String testClassName = "PrivateTest";
        TestExecutor testExecutor = executeTest(testClassName);
        //noinspection ResultOfMethodCallIgnored
        testExecutor.getResults();
    }

    @NotNull
    private TestExecutor executeTest(@NotNull String className) throws Exception {
        Class<?> testClass = RESOURCES_CLASS_LOADER.loadClass(PACKAGE_NAME + "." + className);
        TestExecutor testExecutor = new TestExecutor(Collections.singletonList(testClass));
        testExecutor.executeTests();
        return testExecutor;
    }

    private void assertStats(int total, int failed, int passed, int ignored, @NotNull TestExecutor testExecutor) {
        assertEquals(total, testExecutor.getTotal());
        assertEquals(failed, testExecutor.getFailed());
        assertEquals(passed, testExecutor.getPassed());
        assertEquals(ignored, testExecutor.getIgnored());
    }

    private void assertResult(@NotNull String className, @NotNull String method, @NotNull String result,
                              @NotNull TestResult testResult) {
        assertEquals(className, testResult.getAClass());
        assertEquals(method, testResult.getMethod());
        assertEquals(result, testResult.getResult());
    }
}