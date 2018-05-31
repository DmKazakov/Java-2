package ru.spbau.mit.kazakov.GUI;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

/**
 * Enum for representation client's requests
 */
public enum Request {
    LIST, GET;

    private static final BiMap<Integer, Request> queriesCodes = HashBiMap.create();

    static {
        queriesCodes.put(1, LIST);
        queriesCodes.put(2, GET);
    }

    /**
     * Returns code of specified request.
     *
     * @param request specified request
     */
    public static int getCode(@NotNull Request request) {
        return queriesCodes.inverse().get(request);
    }

    /**
     * Returns request by its code.
     */
    @NotNull
    public static Request getQuery(int code) {
        return queriesCodes.get(code);
    }
}
