package ru.spbau.mit.kazakov.Junit.classes;

import ru.spbau.mit.kazakov.Junit.annotations.*;

public class AnnotationsTest {
    private int beforeCalls;
    private int afterCalls;
    private int beforeClassCalls;
    private int afterClassCalls;
    private int executedTests;

    @BeforeClass
    public void beforeClass() {
        beforeClassCalls++;
    }

    @AfterClass
    public void afterClass() {
        afterClassCalls++;
    }

    @Before
    public void before() {
        beforeCalls++;
    }

    @After
    public void after() {
        afterCalls++;
    }

    @Test
    public void test1() throws Exception {
        executedTests++;
        if (beforeClassCalls != 1) {
            throw new Exception("Before class annotation exception");
        }
        if (beforeCalls != executedTests) {
            throw new Exception("Before annotation exception");
        }
        if (afterCalls != executedTests - 1) {
            throw new Exception("After annotation exception");
        }
        if (afterClassCalls != 0) {
            throw new Exception("After class annotation exception");
        }
    }

    @Test
    public void test2() throws Exception {
        executedTests++;
        if (beforeClassCalls != 1) {
            throw new Exception("Before class annotation exception");
        }
        if (beforeCalls != executedTests) {
            throw new Exception("Before annotation exception");
        }
        if (afterCalls != executedTests - 1) {
            throw new Exception("After annotation exception");
        }
        if (afterClassCalls != 0) {
            throw new Exception("After class annotation exception");
        }
    }

    @Test
    public void test3() throws Exception {
        executedTests++;
        if (beforeClassCalls != 1) {
            throw new Exception("Before class annotation exception");
        }
        if (beforeCalls != executedTests) {
            throw new Exception("Before annotation exception");
        }
        if (afterCalls != executedTests - 1) {
            throw new Exception("After annotation exception");
        }
        if (afterClassCalls != 0) {
            throw new Exception("After class annotation exception");
        }
    }
}
