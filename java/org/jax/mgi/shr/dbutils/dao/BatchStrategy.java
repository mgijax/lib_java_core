package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * A class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using JDBC batch
 * @has a BatchProcessor.
 * @does extracts SQL from a DAO object and executes the SQL in batch.
 * @author M Walker
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
        batch.addBatch(SQLStrategyHelper.convertToJDBCProc(sql));
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
        batch.addBatch(SQLStrategyHelper.convertToJDBCProc(sql));
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
        batch.addBatch(SQLStrategyHelper.convertToJDBCProc(sql));
    }
}