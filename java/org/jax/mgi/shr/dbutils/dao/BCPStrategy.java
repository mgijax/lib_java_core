package org.jax.mgi.shr.dbutils.dao;

import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.dbutils.bcp.BCPException;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;
import org.jax.mgi.shr.dbutils.bcp.BCPTranslatable;
import org.jax.mgi.shr.dbutils.bcp.BCPWriter;
import org.jax.mgi.shr.config.BCPWriterCfg;

/**
 * A class which implements the InsertStrategy interface and can insert
 * DAO objects into the database using bcp
 * @has a BCPManager.
 * @does extracts bcp records from DAO objects and passes them to
 * the coresponding BCPWriters for the target table.
 * @author M Walker
 */
public class BCPStrategy
    implements InsertStrategy
{
    /**
     * the bcp resource from which BCPWriters are obtained
     */
    private BCPManager bcpManager;
    /**
     * the SQLDataManager for running ddl and pre/post sql
     */
    private SQLDataManager sqlManager;

    /**
     * a map of BCPWriters indexed by the targeted table names.
     */
    private HashMap bcpWriters = new HashMap();

    /*
     * the following constant definitions are exceptions thrown by this class
     */
    private static String DAOErr = DBExceptionFactory.DAOErr;
    private static String UnexpectedType = DBExceptionFactory.UnexpectedType;

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @param bcpMgr the BCPManager to use
     */
    public BCPStrategy(SQLDataManager sqlMgr, BCPManager bcpMgr)
    {
        sqlManager = sqlMgr;
        bcpManager = bcpMgr;
    }

    /**
     * insert the DAO object into the database by writing a record to
     * a bcp file
     * @assumes nothing
     * @effects the given DAO will be added to the appropriate
     * bcp file for subsequent bcp execution
     * @param dao the dao object to insert into the database
     * @throws DBException thrown if there is an error in data validation
     * or if there is an error writing to the bcp file
     */
    public void insert(DAO dao)
        throws DBException
    {
        BCPTranslatable trans = (BCPTranslatable) dao;
        Vector tables = trans.getBCPSupportedTables();
        BCPWriter writer = null;
        Table table = null;
        for (Iterator i = tables.iterator(); i.hasNext(); )
        {
            Object o = i.next();
            if (o instanceof Table)
            {
                table = (Table) o;
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                table = Table.getInstance(s, sqlManager);
            }
            else
            {
                DBExceptionFactory eFactory = new DBExceptionFactory();
                DBException e = (DBException)
                    eFactory.getException(UnexpectedType);
                throw e;
            }
            try
            {
                writer = getWriter(table);
                writer.write( (BCPTranslatable) dao);
            }
            catch (MGIException e)
            {
                DBExceptionFactory eFactory = new DBExceptionFactory();
                DBException e2 = (DBException)
                    eFactory.getException(DAOErr, e);
                e2.bind(dao.getClass().getName());
                throw e2;
            }
        }
    }

    /**
     * get a BCPWriter for the given table from the hashmap and if it does
     * not exist then create one and add it to the hasmap
     * @assumes nothing
     * @effects a new entry could be added to the internal hashmap
     * @param table the given table
     * @throws DBException if there is an error with the database
     * @throws BCPException thrown if the BCPWriter could not be obtained
     * from the given Table object
     * @throws ConfigException if there is an error configuring the BCPWriters
     * @return the corresponding BCPWriter
     */
    private BCPWriter getWriter(Table table)
        throws BCPException, DBException, ConfigException
    {
        BCPWriter writer = (BCPWriter) bcpWriters.get(table);
        if (writer == null)
        {
            BCPWriterCfg cfg =
                new BCPWriterCfg(table.getName().toUpperCase());
            writer = bcpManager.getBCPWriter(table, cfg);
            bcpWriters.put(table, writer);
        }
        return writer;
    }
}

  /**************************************************************************
 *
 * Warranty Disclaimer and Copyright Notice
 *
 *  THE JACKSON LABORATORY MAKES NO REPRESENTATION ABOUT THE SUITABILITY OR
 *  ACCURACY OF THIS SOFTWARE OR DATA FOR ANY PURPOSE, AND MAKES NO WARRANTIES,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING MERCHANTABILITY AND FITNESS FOR A
 *  PARTICULAR PURPOSE OR THAT THE USE OF THIS SOFTWARE OR DATA WILL NOT
 *  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS, OR OTHER RIGHTS.
 *  THE SOFTWARE AND DATA ARE PROVIDED "AS IS".
 *
 *  This software and data are provided to enhance knowledge and encourage
 *  progress in the scientific community and are to be used only for research
 *  and educational purposes.  Any reproduction or use for commercial purpose
 *  is prohibited without the prior express written permission of The Jackson
 *  Laboratory.
 *
 * Copyright \251 1996, 1999, 2002 by The Jackson Laboratory
 *
 * All Rights Reserved
 *
 **************************************************************************/
