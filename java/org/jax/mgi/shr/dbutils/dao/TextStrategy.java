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
import org.jax.mgi.shr.dbutils.bcp.BCPTranslatable;
import org.jax.mgi.shr.dbutils.bcp.BCPWriter;
import org.jax.mgi.shr.config.BCPWriterCfg;

import org.jax.mgi.shr.rpt.TextManager;
import org.jax.mgi.shr.rpt.TextWriter;

/**
 * A class which implements the InsertStrategy interface and can insert
 * DAO objects into the database using bcp
 * @has a BCPManager.
 * @does extracts bcp records from DAO objects and passes them to
 * the coresponding BCPWriters for the target table.
 * @author M Walker
 */
public class TextStrategy
    implements InsertStrategy
{
    /**
     * the bcp resource from which BCPWriters are obtained
     */
    private TextManager textManager;

    /**
     * a map of BCPWriters indexed by the targeted table names.
     */
    private TextWriter textWriter = null;
    /**
     * the filename to write to
     */
    private String filename = null;

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
    public TextStrategy(TextManager textMgr, String filename)
    {
        this.textManager = textMgr;
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
        TextWriter writer = null;

        try
        {
            writer = textManager.getTextWriter(this.filename);
            Vector v = trans.getBCPVector(null);
            writer.write(v);
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
