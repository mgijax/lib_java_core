package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.ScriptWriter;
import org.jax.mgi.shr.dbutils.ScriptException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

/**
 * A SQLStream for doing inserts with bcp and doing updates and deletes with
 * scripting
 * @has an ScriptWriter for performing updates and deletes and a
 * BCPStrategy for perfoming inserts
 * @does provides a set of update, insert and delete strategies for updating
 * a given DAO object in a database
 * @company The Jackson Laboratory
 * @author M Walker
 */
public class BCP_Script_Stream
    extends SQLStream
{
    /**
     * the ScriptWriter to use
     */
    private ScriptWriter writer = null;

    /**
     * the BCPManager to use
     */
    private BCPManager bcpMgr = null;

    // the following constant defintions are exceptions thrown by this class
    private static final String ExecuteScriptErr =
        DBExceptionFactory.ExecuteScriptErr;
    private static String SQLStreamCloseErr =
        DBExceptionFactory.SQLStreamCloseErr;

    /**
     * constructor
     * @param writer the ScriptWriter to use
     * @param bcpMgr the BCPManager to use
     * @throws DBException thrown if there is an error accessing the database
     */
    public BCP_Script_Stream(ScriptWriter writer, SQLDataManager sqlMgr,
                             BCPManager bcpMgr)
        throws
        DBException
    {
        super();
        this.writer = writer;
        this.bcpMgr = bcpMgr;
        ScriptStrategy scriptStrategy = new ScriptStrategy(writer);
        BCPStrategy bcpStrategy = new BCPStrategy(sqlMgr, bcpMgr);
        super.setInsertStrategy(bcpStrategy);
        super.setUpdateStrategy(scriptStrategy);
        super.setDeleteStrategy(scriptStrategy);
    }

    /**
     * execute the bcp commands followed by the batch statements
     * @assumes nothing
     * @effects data will be updated in bulk withing the database
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
            writer.execute();
        }
        catch (ScriptException e)
        {
            DBExceptionFactory eFactory = new DBExceptionFactory();
            DBException e2 = (DBException)
                eFactory.getException(ExecuteScriptErr, e);
            throw e2;
        }
    }
}