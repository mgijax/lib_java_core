package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * An interface for performing database inserts, updates and deletes of
 * DAOs (Data Access Objects)
 * @has nothing
 * @does nothing
 * @company The Jackson Laboratory
 * @author M Walker
 */
public interface DAOPersistent
{
    /**
     * delete the given DAO object from the database
     * @assumes nothing
     * @effects the dao object will be deleted
     * @param dao the object to delete
     * @throws DBException thrown if there is an error executing the delete
     */
    void delete(DAO dao)
        throws DBException;

    /**
     * update the given DAO object in the database
     * @assumes nothing
     * @effects the dao object will be updated
     * @param dao the object to update
     * @throws DBException thrown if there is an error executing the update
     */
    void update(DAO dao)
        throws DBException;

    /**
     * insert the given DAO object in the database
     * @assumes nothing
     * @effects the dao object will be inserted
     * @param dao the object to insert
     * @throws DBException thrown if there is an error executing the insert
     */
    void insert(DAO dao)
        throws DBException;
}