package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.ScriptWriter;

/**
 * This class is a testing implementation of the SQLStream class which is used
 * for testing SQLStream functionality
 * @has
 * @does
 * @company The Jackson Laboratory
 * @author M Walker
 *
 */

public class SQLStreamTestImpl extends SQLStream
{

    private SQLDataManager sqlMgr = null;
    private BatchProcessor batch = null;

    /**
     * constructor
     * @param sqlMgr SQLDataManager instance
     * @throws DBException thrown if there is an error with the database
     */
    public SQLStreamTestImpl(SQLDataManager sqlMgr,
                             ScriptWriter scriptWriter) throws DBException
    {
        super();
        this.batch = sqlMgr.getBatchProcessor();
        BatchStrategy batchStrategy = new BatchStrategy(batch);
        InlineStrategy inlineStrategy = new InlineStrategy(sqlMgr);
        ScriptStrategy script = new ScriptStrategy(scriptWriter);
        super.setUpdateStrategy(batchStrategy);
        super.setInsertStrategy(inlineStrategy);
        super.setDeleteStrategy(script);
    }

    /**
     * close the SQLStream
     * @throws DBException thrown if there is an error with the database
     */
    public void close() throws DBException
    {

    }

}