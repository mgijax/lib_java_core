package org.jax.mgi.shr.dbutils;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.BatchUpdateException;
import java.util.Vector;

import org.jax.mgi.shr.log.Logger;

/**
 * A class for handling jdbc batch execution
 * @has a Statement class
 * @does stores a batch of sql statements and executes them all with one
 * network call to the database
 * @company The Jackson Laboratory
 * @author M Walker
 */

public class BatchProcessor
{

    /**
     * the Statement class which is used for performing batch
     */
    private Statement statement = null;
    /**
     * indicator of whether or not resources have been closed
     */
    private boolean isClosed = false;
    /**
     * the collection of sql statements being executed
     */
    private Vector history = new Vector();

    /**
     * the logger to use for error reporting
     */
    private Logger logger = null;

    /*
     * the following constant definitions are exceptions thrown by this class
     */
    private static String AddBatchErr =
        DBExceptionFactory.AddBatchErr;
    private static String ExecuteBatchErr =
        DBExceptionFactory.ExecuteBatchErr;
    private static String BatchNotSupported =
        DBExceptionFactory.BatchNotSupported;
    private static String ResourceClosed =
        DBExceptionFactory.ResourceClosed;
    private static String JDBCException =
        DBExceptionFactory.JDBCException;
    private static String BatchCommandFailed =
        BatchExceptionFactory.BatchCommandFailed;

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @param statement the JDBC Statement class used for handling batch sql
     */
    protected BatchProcessor(Statement statement)
    {
        this.statement = statement;
    }

    /**
     * override the logger to use for error reporting which is initialized to
     * the same logger from the SQLDataManager this instance was derived from
     * @assumes nothing
     * @effects all subsequent logging will use the given Logger
     * @param logger the Logger
     */
    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * add a sql string to the batch
     * @assumes nothing
     * @effects adds the given string the batch of sql statements
     * @param sql the sql string
     * @throws DBException thrown if there is an error in the database
     */
    public void addBatch(String sql)
        throws DBException
    {
        if (isClosed)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e = (DBException)
                eFactory.getException(ResourceClosed);
            e.bind("add the following sql to the batchProcessor: " + sql);
            throw e;
        }
        try
        {
            statement.addBatch(sql);
            history.add(sql);
        }
        catch (SQLException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(AddBatchErr, e);
            e2.bind(sql);
            throw e2;
        }
    }

    /**
     * execute the batch of sql
     * @assumes nothing
     * @effects nothing
     * @return the array of counts for each update from the batch
     * @throws DBException thrown if there is an error in the database
     * @throws BatchException thrown if there is a batch exception
     */
    public int[] executeBatch()
        throws DBException, BatchException
    {
        if (isClosed)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e = (DBException)
                eFactory.getException(ResourceClosed);
            e.bind("execute the batchProcessor");
            throw e;
        }
        try
        {
            int[] results = statement.executeBatch();
            if (logger != null)
            {
                logger.logInfo("The Batch Processor completed sucessfully");
                logger.logInfo(history.size() +
                               " sql statements were run\n0 failed");
            }
            return results;
        }
        catch (BatchUpdateException e)
        {
            BatchExceptionFactory eFactory = new BatchExceptionFactory();
            BatchException e2 = (BatchException)
                eFactory.getException(BatchCommandFailed, e);
            int[] updateCounts = e.getUpdateCounts();
            e2.setUpdateCounts(updateCounts);
            int failedCount = 0;
            int totalCount = updateCounts.length;
            if (logger != null)
            {
                logger.logError(
                    "The following is a list of all sql statements that " +
                    "failed for this run");
            }
            for (int i = 0; i < totalCount; i++)
            {
                switch (updateCounts[i])
                {
                    case BatchException.ERROR:
                        if (logger != null)
                        {
                            logger.logError((String)history.get(i));
                        }
                        failedCount++;
                }
            }
            if (logger != null)
            {
                logger.logInfo("The Batch Processor completed sucessfully");
                logger.logError(totalCount + " sql statements were run\n" +
                                failedCount + " failed");
            }
            throw e2;
        }
        catch (SQLException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(BatchNotSupported, e);
            throw e2;
        }

    }

    /**
     * close resources for this processor
     * @assumes nothing
     * @effects the resources are freed and the processor will no longer
     * be available for operations
     * @throws DBException thrown if there is an error with the database
     */
    public void close()
        throws DBException
    {
        try
        {
            this.statement.close();
        }
        catch (SQLException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(JDBCException, e);
            e2.bind("close the BatchProcessor " + this.getClass().getName());
            throw e2;
        }
        history = null;
        isClosed = true;
    }

}