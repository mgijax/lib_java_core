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
public class BCP_Batch_Stream
    extends SQLStream
{
    /**
     * the BatchProcessor to use
     */
    private BatchProcessor batch = null;

    /**
     * the BCPManager to use
     */
    private BCPManager bcpMgr = null;

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
    public BCP_Batch_Stream(SQLDataManager sqlMgr, BCPManager bcpMgr)
        throws
        DBException
    {
        super();
        this.bcpMgr = bcpMgr;
        this.batch = sqlMgr.getBatchProcessor();
        BatchStrategy batchStrategy = new BatchStrategy(batch);
        BCPStrategy bcpStrategy = new BCPStrategy(bcpMgr);
        super.setUpdateStrategy(batchStrategy);
        super.setInsertStrategy(bcpStrategy);
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
            bcpMgr.executeBCP();
        }
        catch (MGIException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(SQLStreamCloseErr, e);
            e2.bind(this.getClass().getName());
            throw e2;
        }
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