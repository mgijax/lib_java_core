// $Header$
// $Name$

package org.jax.mgi.shr.ioutils;

/**
 * @is An iterator for iterating through records of data
 * @has A data file. A RecordDataReader for parsing the data file into
 * records. A RecordDataInterpreter for instantiating java data objects
 * based on the current record. If the interpreter is null then the current
 * record is returned as a String. The interpreter also defines whether a
 * current record is considered to be vaild or not. If an interpreter is
 * defined then only valid records are returned during iteration.
 * @does iterates through data as a collection of records and returns java
 * data objects. The caller must set the Interpreter and cast the return
 * objects to the known type.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */


public interface RecordDataIterator {

  /**
   * Set the reference of a RecordDataInterpreter which will then be used on
   * subsequent calls to the next() method to create java data objects
   * based on the contents of the record.
   * @param interpreter the RecordDataInterpreter object
   */

  public void setInterpreter(RecordDataInterpreter interpreter);

  /**
   * return a boolean value that designates whether there are any records
   * remaining within the current iteration of the input file. If there
   * is a RecordDataInterpreter set for this iteration then this method
   * returns true only if the record is considered valid by the interpreter,
   * that is if the isValid() method of the interpreter returns true.
   * @return true if more records remain
   */

  public boolean hasNext();

  /**
   * Retrieves the next record from the input file which can be represented
   * as any java data object depending on the type of Interpreter
   * provided. If a RecordDataInterpreter is defined then only a record that
   * is considered valid by the interpreter is returned, that is if the
   * isValid() method of the interpreter is true.
   * @return the next object from the file.
   * @throws RecordFormatException thrown if the next record was badly
   * formated
   * @throws IOUException thrown if there is an io error in retrieving the
   * next record
   */

  public java.lang.Object next() throws IOUException, RecordFormatException;

  /**
   * closes the record source
   * @throws IOUException thrown if an io error occurs
   */
  public void close() throws IOUException;

}
// $Log$
// Revision 1.4  2003/06/04 15:06:34  mbw
// javadoc edits
//
// Revision 1.3  2003/05/20 15:22:43  mbw
// merged code from branch lib_java_ioutils-1-0-5-jsam
//
// Revision 1.2.2.9  2003/05/16 15:10:38  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.2.2.8  2003/05/07 23:37:02  mbw
// javadoc edits
//
// Revision 1.2.2.7  2003/04/29 17:26:01  mbw
// added changes from code reviews
//
// Revision 1.2.2.6  2003/04/28 16:24:29  mbw
// changed due to impact of new exception handling model
//
// Revision 1.2.2.5  2003/04/15 11:58:09  mbw
// javadoc edits
//
// Revision 1.2.2.4  2003/04/10 17:18:01  mbw
// added new method to interface
//
// Revision 1.2.2.3  2003/03/21 16:12:29  mbw
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
