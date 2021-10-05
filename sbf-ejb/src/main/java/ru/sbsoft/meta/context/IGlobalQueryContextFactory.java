package ru.sbsoft.meta.context;

/**
 *
 * @author Kiselev
 */
public interface IGlobalQueryContextFactory {

    GlobalQueryContext createGlobalQueryContext(String username, boolean isAdmin);
}
