package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;

/**
 *  An MGIException which represents an error when stamping user/time
 * fields within a record.
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @author M Walker
 */
public class RecordStampException
    extends MGIException
{
    public RecordStampException(String pMessage, boolean pDataRelated)
    {
        super(pMessage, pDataRelated);
    }
}