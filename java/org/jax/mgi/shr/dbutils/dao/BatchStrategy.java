package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is a class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using JDBC batch
 * @has a BatchProcessor.
 * @does casts the given DAO to a SQLTranslatable from which to
 * obtain the sql statements and executes the sql for either insert, update or
 * delete as a batch statement.
 * @copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */
public class BatchStrategy
    implements DeleteStrategy, InsertStrategy, UpdateStrategy
{
    /**
     * the BatchProcessor used for executing batch sql
     */
    private BatchProcessor batch = null;
    /**
     * constructor
     * @param batch the BatchProcessor to use
     */
    public BatchStrategy(BatchProcessor batch)
    {
        this.batch = batch;
    }

    /**
     * update the given DAO object
     * @assumes nothing
     * @effects the given DAO will be batched up for update into the
     * database
     * @param dao the dao object to update in the database
     * @throws DBException thrown if there is an error with the database
     */
    public void update(DAO dao)
        throws DBException
    {
        String sql = ( (SQLTranslatable) dao).getUpdateSQL();
        batch.addBatch(sql);
    }

    /**
     * delete the given DAO object
     * @assumes nothing
     * @effects the given DAO will be batched up for a delete from the
     * database
     * @param dao the dao object to delete
     * @throws DBException thrown if there is an error accessing the database
     */
    public void delete(DAO dao)
        throws DBException
    {
        String sql = ( (SQLTranslatable) dao).getDeleteSQL();
        batch.addBatch(sql);
    }

    /**
     * insert the given DAO object
     * @assumes nothing
     * @effects the given DAO will be batched up for insert into the
     * database
     * @param dao the dao object to insert
     * @throws DBException thrown if there is an error accessing the database
     */
    public void insert(DAO dao)
        throws DBException
    {
        String sql = ( (SQLTranslatable) dao).getDeleteSQL();
        batch.addBatch(sql);
    }
}