package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is an interface which defines a method to insert DAO objects into
 * the database. This could be implemented in a number of different ways,
 * including through bcp, inline sql, batch JDBC or scripting
 * @has nothing
 * @does provides an interface for inserting dao objects
 * @copyright Jackson Labatory
 * @author M Walker
 * @version 1.0
 */
public interface InsertStrategy
{
    /**
     * insert the given DAO object into the database
     * @assumes nothing
     * @effects the given DAO will be inserted into the database or
     * batched up to be inserted into the database, depending on the
     * specific implementation
     * @param dao the object to insert
     * @throws DBException thrown if an exception occurs when executing
     * the insert
     */
    void insert(DAO dao)
        throws DBException;
}