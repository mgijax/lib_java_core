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
 * @is a class which implements the InsertStrategy interface and can insert
 * DAO objects into the database using bcp
 * @has a BCPManager.
 * @does casts the given DAO to a BCPTranslatable and passes it to
 * the coresponding BCPWriter for the target table.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */

public class BCPStrategy implements InsertStrategy {

  /**
   * the bcp resource from which BCPWriters are obtained
   */
  private BCPManager bcpManager;

  /**
   * a map of BCPWriters indexed by the targeted table names.
   */
  private HashMap bcpWriters = new HashMap();

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String DataInstanceErr =
      DBExceptionFactory.DataInstanceErr;
  private static String UnexpectedType =
      DBExceptionFactory.UnexpectedType;

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   */
  public BCPStrategy(BCPManager bcpMgr)
  {
    bcpManager = bcpMgr;
  }
  /**
   * insert the DAO object into the database by writing a record to
   * a bcp file
   * @assumes nothing
   * @effects the given DAO will be added to the corresponding
   * bcp file for subsequent bcp command execution
   * @param dbComponent the object to insert into the database
   * @throws DBException thrown if there is an error inserting the
   * DAO
   */
  public void insert(DAO dbComponent) throws DBException
  {
    BCPTranslatable trans = (BCPTranslatable)dbComponent;
    Vector tables = trans.getBCPSupportedTables();
    BCPWriter writer = null;
    Table table = null;
    for (Iterator i = tables.iterator(); i.hasNext(); )
    {
      Object o = i.next();
      if (o instanceof Table) {
        table = (Table)o;
      }
      else if (o instanceof String) {
        String s = (String)o;
        SQLDataManager sqlMgr = null;
        try {
          sqlMgr = bcpManager.getSQLDataManager();
        }
        catch (ConfigException e) {
          DBExceptionFactory eFactory = new DBExceptionFactory();
          DBException e2 = (DBException)
              eFactory.getException(DataInstanceErr, e);
          e2.bind(dbComponent.getClass().getName());
          throw e2;
        }
        table = Table.getInstance(s, sqlMgr);
      }
      else {
        DBExceptionFactory eFactory = new DBExceptionFactory();
        DBException e = (DBException)
            eFactory.getException(UnexpectedType);
        throw e;
      }
      try {
        writer = getWriter(table);
        writer.write((BCPTranslatable)dbComponent);
      }
      catch (MGIException e) {
        DBExceptionFactory eFactory = new DBExceptionFactory();
        DBException e2 = (DBException)
            eFactory.getException(DataInstanceErr, e);
        e2.bind(dbComponent.getClass().getName());
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
   * @throws BCPException thrown if there is an error when writing to the
   * bcp file
   * @throws ConfigException if there is an error configuring the BCPWriters
   * @return the corresponding BCPWriter
   */
  private BCPWriter getWriter(Table table)
  throws BCPException, DBException, ConfigException
  {
    BCPWriter writer = (BCPWriter)bcpWriters.get(table);
    if (writer == null)
    {
			BCPWriterCfg cfg = new BCPWriterCfg(table.getName().toUpperCase());
      writer = bcpManager.getBCPWriter(table, cfg);
      bcpWriters.put(table, writer);
    }
    return writer;
  }

}
