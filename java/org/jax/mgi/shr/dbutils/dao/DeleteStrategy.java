package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is an interface which defines a method to delete the given DAO
 * object from the database. This could be implemented in a number of
 * different ways, including inline sql, batch JDBC or scripting
 * @has nothing
 * @does provides an interface for deleting dao objects
 * @Copyright Jackson Labatory
 * @author M Walker
 * @version 1.0
 */
public interface DeleteStrategy
{
    /**
     * delete the given DAO object from the database
     * @assumes nothing
     * @effects the given DAO will be deleted from the database or
     * batched up to be deleted from the database, depending on the
     * specific implementation.
     * @param dao the object to delete
     * @throws DBException thrown if an exception occurs when executing
     * the delete
     */
    void delete(DAO dao)
        throws DBException;
}