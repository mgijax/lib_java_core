package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.BatchException;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

/**
 * @is a SQLStream for doing inserts with bcp and updates and deletes with
 * JDBC batch
 * @has a JDBCBatchStrategy for performing updates and deletes and a
 * BCPStrategy for perfoming inserts
 * @does provides a set of update, insert and delete strategies for updating
 * a given DAO objects in a database
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */
public class Batch_Stream
    extends SQLStream
{
    /**
     * the BatchProcessor to use
     */
    private BatchProcessor batch = null;

    // the following constant defintions are exceptions thrown by this class
    private static final String ExecuteBatchErr =
        DBExceptionFactory.ExecuteBatchErr;
    private static String SQLStreamCloseErr =
        DBExceptionFactory.SQLStreamCloseErr;

    /**
     * constructor
     * @param sqlMgr the SQLDataManager to use
     * @param bcpMgr the BCPManager to use
     * @throws DBException thrown if there is an error accessing the database
     */
    public Batch_Stream(SQLDataManager sqlMgr)
        throws
        DBException
    {
        super();
        this.batch = sqlMgr.getBatchProcessor();
        BatchStrategy batchStrategy = new BatchStrategy(batch);
        super.setUpdateStrategy(batchStrategy);
        super.setInsertStrategy(batchStrategy);
        super.setDeleteStrategy(batchStrategy);
    }

    /**
     * execute the bcp commands followed by the batch statements
     * @assumes nothing
     * @effects data will be modified in bulk within the database
     * @throws DBException thrown if there is an error accessing the database
     */
    public void close()
        throws DBException
    {
        try
        {
            batch.executeBatch();
        }
        catch (BatchException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(ExecuteBatchErr, e);
            throw e2;
        }
    }
}
