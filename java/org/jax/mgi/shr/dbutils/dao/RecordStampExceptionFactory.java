package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 *  An ExceptionFactory for RecordStampExceptions.
 * @has a hashmap of predefined RecordStampExceptions stored by a name
 * @does looks up RecordStampExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 */
public class RecordStampExceptionFactory
    extends ExceptionFactory
{
    /**
     * Unknown record stamp type
     */
    public static final String UnknownType =
        "org.jax.mgi.shr.dbutils.dao.UnknownType";
    static
    {
        exceptionsMap.put(UnknownType, new RecordStampException(
            "The record stamp type for class ?? is unknown. If this " +
            "is a new record stamp type then the RecordStamper class needs" +
            "to be modified to know about it.", false));
    }

    /**
     * Could not obtain user information
     */
    public static final String UnknownUser =
        "org.jax.mgi.shr.dbutils.dao.UnknownUser";
    static
    {
        exceptionsMap.put(UnknownUser, new RecordStampException(
            "Could not apply automatic record stamp to class ?? due to " +
            "unknown user in the database", false));
    }

    /**
     * Error in configuration
     */
    public static final String ConfigErr =
        "org.jax.mgi.shr.dbutils.dao.ConfigErr";
    static
    {
        exceptionsMap.put(ConfigErr, new RecordStampException(
            "Could not apply automatic record stamp to class ?? due to " +
            "configuration error", false));
    }
}