package org.jax.mgi.shr.dbutils;

import java.util.*;

public class QuerySeries
{

    private ArrayList sqlStatements = null;
    private Iterator i = null;
    private SQLDataManager sqlMgr = null;
    private ResultsNavigator closeNav = null;


    protected QuerySeries(ArrayList sqlStatements,
                          SQLDataManager sqlMgr)
    {
        this.sqlStatements = sqlStatements;
        this.i = sqlStatements.iterator();
        this.sqlMgr = sqlMgr;
    }

    public int queryCount()
    {
        return sqlStatements.size();
    }

    public boolean hasNext()
    {
        return i.hasNext();
    }

    public ResultsNavigator executeNextQuery()
    throws DBException
    {
        if (closeNav != null)
        {
            try
            {
                closeNav.close();
            }
            catch (Exception e) {} // do nothing if already closed
        }
        String sql = (String)i.next();
        closeNav = sqlMgr.executeQuery(sql);
        return closeNav;
    }

}