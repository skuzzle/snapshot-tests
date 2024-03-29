package de.skuzzle.test.snapshots.data.xml;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

final class CachedJAXBContexts {

    private static final Map<Class<?>, JAXBContext> cache = new ConcurrentHashMap<>();

    public static JAXBContext getOrCreateContext(Object actual) {
        return cache.computeIfAbsent(actual.getClass(), type -> {
            try {
                return JAXBContext.newInstance(type);
            } catch (final JAXBException e) {
                throw new IllegalArgumentException("Could not create JAXBContext for " + type, e);
            }
        });
    }

    private CachedJAXBContexts() {
        // hidden
    }
}
