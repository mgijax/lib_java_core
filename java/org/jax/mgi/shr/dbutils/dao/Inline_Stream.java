package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * A SQLStream for doing inserts, updates and deletes with inline sql
 * @has a InlineSQLStrategy for performing updates, inserts and deletes
 * @does inserts, updates and deletes DAO objects onto an SQLStream
 * @company The Jackson Laboratory
 * @author M Walker
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
     * does nothing since all sql is executied in realtime using inline sql
     * @assumes nothing
     * @effects nothing
     */
    public void close()
    {
    }
}