package org.jax.mgi.shr.cache;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * An ExceptionFactory for cache exceptions.
 * @has a hashmap of predefined CacheExceptions stored by a name key
 * @does looks up CacheExceptions by name
 * @author M Walker
 */
public class CacheExceptionFactory
    extends ExceptionFactory
{
    /**
     * A given RowDataInterpreter did not create a KeyValue object
     */
    public static final String MissingKeyValue =
        "org.jax.mgi.shr.cache.MissingKeyValue";
    static
    {
        exceptionsMap.put(MissingKeyValue, new CacheException(
            "The RowDataInterpreter class, ??, did not create the KeyValue object " +
            "required by the current CacheStrategy", false));
    }

    /**
     * This is an unknown cache strategy type
     */
    public static final String UnknownStrategy =
        "org.jax.mgi.shr.cache.UnknownStrategy";
    static
    {
        exceptionsMap.put(UnknownStrategy, new CacheException(
            "An unknown strategy type of ?? given to the constructor " +
            "of RowDataCacheHandler", false));
    }

    /**
     * Full cache was not initialized
     */
    public static final String InitializationErr =
        "org.jax.mgi.shr.cache.InitializationErr";
    static
    {
        exceptionsMap.put(InitializationErr, new CacheException(
            "A null initialization string was found for a full cache. " +
            "The method getFullInitQuery must be implemented with a " +
            "non-null value", false));
    }
}