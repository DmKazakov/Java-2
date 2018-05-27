package ru.spbau.mit.kazakov.Junit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for unit testing.
 */
public class Junit {
    /**
     * Executes all testing methods contained in specified package root and prints results.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No path specified.");
            return;
        }

        try {
            List<Class<?>> tests = loadClasses(args[0]);
            TestExecutor testExecutor = new TestExecutor(tests);
            testExecutor.executeTests();

            System.out.println("Total: " + testExecutor.getTotal());
            System.out.println("Ignored: " + testExecutor.getIgnored());
            System.out.println("Passed: " + testExecutor.getPassed());
            System.out.println("Failed: " + testExecutor.getFailed());
            System.out.println();
            for (TestResult testResult : testExecutor.getResults()) {
                System.out.println(testResult.toString());
                System.out.println();
            }
        } catch (MalformedURLException exception) {
            System.out.println("Unable to access to specified path.");
        } catch (ClassNotFoundException exception) {
            System.out.println("Broken classpath.");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Loads all classes contained in specified package root.
     *
     * @param path to package root
     * @return list of Class object with loaded classes
     * @throws MalformedURLException  if unable to access to specified path
     * @throws ClassNotFoundException if a classpath is broken
     */
    @NotNull
    private static List<Class<?>> loadClasses(@NotNull String path) throws MalformedURLException, ClassNotFoundException {
        File packageRoot = new File(path);
        ClassLoader classLoader = new URLClassLoader(new URL[]{packageRoot.toURI().toURL()});

        List<Class<?>> classes = new ArrayList<>();
        for (File classFile : FileUtils.listFiles(packageRoot, new String[]{"class"}, true)) {
            String className = getClassName(packageRoot, classFile);
            classes.add(classLoader.loadClass(className));
        }

        return classes;
    }

    /**
     * Returns class's full name by it's file and package root.
     */
    private static String getClassName(@NotNull File packageRoot, @NotNull File classFile) {
        String pathToClass = packageRoot.toPath().relativize(classFile.toPath()).toString();
        return StringUtils.removeEnd(pathToClass, ".class").replaceAll(File.separator, ".");
    }
}
