package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is a SQLStream for doing inserts, updates and deletes with inline sql
 * @has a InlineSQLStrategy for performing updates, inserts and deletes
 * @does inserts, updates and deletes DAO objects onto an SQLStream
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */
public class Inline_Stream
    extends SQLStream
{
    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @param sqlMgr the SQLDataManager to use
     */
    public Inline_Stream(SQLDataManager sqlMgr)
    {
        super();
        InlineStrategy inlineStrategy = new InlineStrategy(sqlMgr);
        super.setInsertStrategy(inlineStrategy);
        super.setUpdateStrategy(inlineStrategy);
        super.setDeleteStrategy(inlineStrategy);
    }

    /**
     * execute the bcp commands followed by the batch statements
     * @assumes nothing
     * @effects the bcp command is executed for all BCPWriters created
     * through the BCPManager
         * @throws DBException thrown if there is an error executing the bcp command
     */
    public void close()
        throws DBException
    {
    }
}