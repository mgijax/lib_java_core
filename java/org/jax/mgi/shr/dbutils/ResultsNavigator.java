// $Header$
// $Name$

package org.jax.mgi.shr.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

/**
 * @is An object used to iterate through the results of a database
 * query.
 * @has A JDBC ResultSet and a RowDataInterpreter
 * @does Iterates through the result set and returns a java data
 * object for each row. If no RowDataInterpreter has been defined then a
 * RowReference object is created for each row.
 * @company Jackson Lab
 * @author MWalker
 * @version 1.0
 */
public class ResultsNavigator {

  // The internal ResultSet object
  private ResultSet rs = null;
  // If not null then this object is used to create java data objects
  // from the current row
  private RowDataInterpreter interpreter = null;
  // metadata obtained through the ResultSet
  private ResultSetMetaData meta = null;

  // the following constant definitions are exceptions thrown by this class
  private static final String JDBCException =
      DBExceptionFactory.JDBCException;
  private static final String CloseErr =
      DBExceptionFactory.CloseErr;


  /**
   * constructor
   * @param rsIn the JDBC ResultsSet object from a query
   * @throws DBException thrown if there is a database error
   */
  protected ResultsNavigator(ResultSet rsIn) throws DBException {
    rs = rsIn;
  }

  /**
   * close the ResultSet object
   * @assumes nothing
   * @effects the ResultSet object will be closed
   * @throws DBException thrown if there is a JDBC Exception
   */
  public void close() throws DBException {
    try {
      rs.close();
    }
    catch (SQLException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(CloseErr, e);
      throw e2;
    }
  }

  /**
   * sets the value of the RowDataInterpreter
   * @assumes nothing
   * @effects the internal reference to an Interpreter will be set
   * @param in the given RowDataInterpreter
   */
  public void setInterpreter(RowDataInterpreter in) {
    interpreter = in;
  }

  /**
   * returns a boolean value to designate whether thare are more rows left
   * to process in the current iteration
   * @assumes nothing
   * @effects the ResultSet pointer will be advanced
   * @return true if rows remain, otherwise return false.
   * @throws DBException thrown if there is a JDBC Exception
   */
  public boolean next() throws DBException {
    try {
      return rs.next();
    }
    catch (SQLException e) {
      throw dbException("move to the next row of a result set", e);
    }
  }

  /**
   * Creates and returns a java data object based on the current row from the
   * ResultSet unless no RowDataInterpreter has been defined, which in
   * that case returns a RowReference object. The RowRefernce is strictly
   * a reference to the currrent row and so will only represent the current
   * row. It will change with each call to next().
   * @assumes nothing
   * @effects any previously returned RowReference object would be now
   * referencing the new row
   * @return the object created based on the current row from the ResultSet.
   * The object will have to be cast to correct type.
   * @throws DBException thrown if there is a JDBC Exception
   */
  public java.lang.Object getCurrent() throws DBException {
    RowReference rowref = new RowReference(rs);
    Object interpretedItem = null;
    if (interpreter != null)
      interpretedItem = interpreter.interpret(rowref);
    else
      interpretedItem = rowref;
    return interpretedItem;
  }

  /**
   * Returns a RowReference object for the current row.
   * @assumes nothing
   * @effects any previously returned RowReference object would now
   * referencing this returned RowReference
   * @return the RowReference object for the current row.
   * @throws DBException thrown if there is a JDBC Exception
   */
  public RowReference getRowReference() throws DBException {
    return new RowReference(rs);
  }


  /**
   * Moves the iterator to the first row
   * @assumes nothing
   * @effects the pointer within the ResultSet is moved to the beginning
   * @return boolean true if the iterator is on a valid row, false if there
   * are no rows
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean first() throws DBException {
    try {
      return rs.first();
    }
    catch (SQLException e) {
      throw dbException("move to the first row of a result set", e);
    }
  }

  /**
   * Moves the iterator to the last row
   * @assumes nothing
   * @effects the pointer in the ResultSet will be moved to the last
   * position
   * @return boolean true if the iterator is on a valid row,
   * false otherwise
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean last() throws DBException {
    try {
      return rs.last();
    }
    catch (SQLException e) {
      throw dbException("move to the last row of a result set", e);
    }

  }

  /**
   * Moves the iterator to the end of the iteration, after the last row. This
   * method has no effect if the results set contains no rows.
   * @assumes nothing
   * @effects the ResultSet pointer will be moved to after the last position
   * @throws DBException thrown if a JDBC error occurs
   */
  public void afterLast() throws DBException {
    try {
      rs.afterLast();
    }
    catch (SQLException e) {
      throw dbException("move after the last row of a result set", e);
    }
  }

  /**
   * Moves the to the start of the iteration, just before the first row.
   * this method has no effect if there are no rows.
   * @assumes nothing
   * @effects the ResultSet pointer will be moved to before the first
   * position
   * @throws DBException thrown if a JDBC error occurs
   */
  public void beforeFirst() throws DBException {
    try {
      rs.beforeFirst();
    }
    catch (SQLException e) {
      throw dbException("move before the first row of a result set", e);
    }
  }

  /**
   * move the iteration to the given row number in the iteration
   * @assumes nothing
   * @effects the ResultSet pointer will be moved
   * @param row the row number
   * @return true if the position is on a row; false otherwise
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean absolute(int row) throws DBException {
    try {
      return rs.absolute(row);
    }
    catch (SQLException e) {
      throw dbException("move a absolute number of rows in a result set", e);
    }
  }

  /**
   * Moves the iterator a relative number of rows either in positive or
   * negative direction. Attempting to move beyond the first or last row will
   * position the curser before/after the first/last row.
   * @assumes nothing
   * @effects the ResultSet pointer will be moved
   * @param row the relative row number
   * @return true if the position is on a row; false otherwise
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean relative(int row) throws DBException {
    try {
      return rs.relative(row);
    }
    catch (SQLException e) {
      throw dbException("move a relative number of rows in a result set", e);
    }
  }

  /**
   * Move the iterator to the previous row
   * @assumes nothing
   * @effects the ResultSet pointer will be moved
   * @return true if the position is valid, false if it beyond the result
   * set
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean previous() throws DBException {
    try {
      return rs.previous();
    }
    catch (SQLException e) {
      throw dbException("move to previous row in a result set", e);
    }
  }

  /**
   * Retrieves whether the iterator is on the first row of the result set
   * @assumes nothing
   * @effects nothing
   * @return true if the iterator is on the first row; false otherwise
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean isFirst() throws DBException {
    try {
      return rs.isFirst();
    }
    catch (SQLException e) {
      throw dbException("retrieving whether the result set is on first " +
                        "row", e);
    }
  }

  /**
   * Retrieves whether the iterator is on the last row of the result set
   * @assumes nothing
   * @effects nothing
   * @return true if the iterator is on the last row; otherwise false
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean isLast() throws DBException {
    try {
      return rs.isLast();
    }
    catch (SQLException e) {
      throw dbException("retrieving whether the result set is on last " +
                        "row", e);
    }
  }

  /**
   * Retrieves whether the iterator is after the last row of the result set
   * @assumes nothing
   * @effects nothing
   * @return true if the iterator is after the last row; false otherwise
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean isAfterLast() throws DBException {
    try {
      return rs.isAfterLast();
    }
    catch (SQLException e) {
      throw dbException("retrieving whether the result set is after the " +
                        "last row", e);
    }
  }

  /**
   * Retrieves whether the iterator is before the first row of the result set
   * @assumes nothing
   * @effects nothing
   * @return true if the iterator is before the first row; false otherwise
   * @throws DBException thrown if a JDBC error occurs
   */
  public boolean isBeforeFirst() throws DBException {
    try {
      return rs.isBeforeFirst();
    }
    catch (SQLException e) {
      throw dbException("retrieving whether the result set is before the " +
                        "first row", e);
    }
  }

        /**
         * get the row metadata
         * @assumes nothing
         * @effects nothing
         * @return the metadata
         * @throws DBException thrown if there is an error from the database
         */
        public ResultSetMetaData getMetaData() throws DBException {
                if (meta == null)
                        setMetaData();
                return meta;
        }

        /**
         * set the internal reference for the row metadata
         * @assumes nothing
         * @effects the internal reference for the row metadata will be set
         * @throws DBException
         */
        private void setMetaData() throws DBException {
                try {
                        meta = rs.getMetaData();
                }
                catch (SQLException e) {
                        throw dbException("get metadata from ResultSet", e);
                }
        }

  /**
   * get a DBException with name DBExceptionFactory.JDBCException and
   * bind the given string to it
   * @assumes nothing
   * @effects nothing
   * @param s the string to bind to the exception message
   * @return the DBException object
   */
  private DBException dbException(String s, SQLException e) {
    DBExceptionFactory factory = new DBExceptionFactory();
    DBException e2 = (DBException) factory.getException(JDBCException, e);
    e2.bind(s);
    return e2;
  }

}

// $Log$
// Revision 1.2  2004/01/07 18:09:36  mbw
// added new method getCurrentRef
//
// Revision 1.1  2003/12/30 16:50:34  mbw
// imported into this product
//
// Revision 1.2  2003/12/09 22:49:06  mbw
// merged jsam branch onto the trunk
//
// Revision 1.1.4.4  2003/06/17 20:25:58  mbw
// including original exception when throwing new execptions within a catch block
//
// Revision 1.1.4.3  2003/06/04 18:29:55  mbw
// javadoc edits
//
// Revision 1.1.4.2  2003/05/22 15:55:02  mbw
// changed from interface to full implementation
//
// Revision 1.1.4.1  2003/05/20 18:30:18  mbw
// merged from branch lib_java_shrdbutils-1-0-7-jsam
//
// Revision 1.1.2.2  2003/05/16 15:11:22  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.1.2.1  2003/05/08 01:55:30  mbw
// renamed RowDataIterator to ResultsNavigator.java
//
// Revision 1.2.2.5  2003/04/25 17:12:01  mbw
// updated to reflect changes in design of exception handling
//
// Revision 1.2.2.4  2003/04/15 12:05:21  mbw
// javadocs edits
//
// Revision 1.2.2.3  2003/03/21 16:53:01  mbw
// added standard header/footer
//
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
