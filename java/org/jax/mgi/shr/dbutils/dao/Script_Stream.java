package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.ScriptWriter;
import org.jax.mgi.shr.dbutils.ScriptException;

/**
 * A SQLStream for doing inserts, updates and deletes using scripting
 * @has a ScriptStrategy for performing updates, inserts and deletes
 * @does inserts, updates and deletes DAO objects using scripting
 * @company The Jackson Laboratory
 * @author M Walker
 */
public class Script_Stream
    extends SQLStream
{
    /**
     * the ScriptWriter to use
     */
    private ScriptWriter writer = null;

    // the following constant defintions are exceptions thrown by this class
    private static final String ExecuteScriptErr =
        DBExceptionFactory.ExecuteScriptErr;
    private static String SQLStreamCloseErr =
        DBExceptionFactory.SQLStreamCloseErr;

    /**
     * constructor which sets the ScriptWriter for this instance
     * @assumes nothing
     * @effects nothing
     * @param writer the ScriptWriter to use
     */
    public Script_Stream(ScriptWriter writer)
    {
        super();
        this.writer = writer;
        ScriptStrategy scriptStrategy = new ScriptStrategy(writer);
        super.setInsertStrategy(scriptStrategy);
        super.setUpdateStrategy(scriptStrategy);
        super.setDeleteStrategy(scriptStrategy);
    }

    /**
     * execute the script
     * @assumes nothing
     * @effects isql is executed through the ScriptWriter
     * @throws DBException thrown if there is an error executing the isql
     * command
     */
    public void close()
        throws DBException
    {
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