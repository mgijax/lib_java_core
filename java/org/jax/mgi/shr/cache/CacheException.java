package org.jax.mgi.shr.cache;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An MGIException which represents an error in cache handling
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @author M Walker
 * @version 1.0
 */
public class CacheException
    extends MGIException
{
    public CacheException(String pMessage, boolean pDataRelated)
    {
        super(pMessage, pDataRelated);
    }
}