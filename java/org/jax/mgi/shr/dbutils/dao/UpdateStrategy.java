package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is an interface which defines a method to update a given  DAO
 * object within the database. This could be implemented in a number of
 * different ways, including inline sql, batch JDBC or scripting
 * @has nothing
 * @does provides an interface
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */
public interface UpdateStrategy
{
    /**
     * update the given DAO object into the database
     * @assumes nothing
     * @effects the given DAO will be updated in the database or
     * batched up to be updated in the database, depending on the
     * specific implementation.
     * @param dao the object to update
     * @throws DBException thrown if an exception occurs when executing
     * the update
     */
    void update(DAO dao)
        throws DBException;
}