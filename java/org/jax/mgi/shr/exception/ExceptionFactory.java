// $Header$
// $Name$

package org.jax.mgi.shr.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * @is a base class object that stores Exceptions.
 * @has a static hasmap for storing Exceptions.
 * @does provides static storage and static lookup methods.
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ExceptionFactory
{
  // the storage for Exceptions
  static protected HashMap exceptionsMap = new HashMap();

  /**
   * overrides the toString() method from the Object class
   * @assumes nothing
   * @effects nothing
   * @return a String which contains a list of all the Exceptions stored
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (Iterator it = exceptionsMap.entrySet().iterator(); it.hasNext(); ) {
      Entry entry = (Entry)it.next();
      buffer.append(entry.getKey() + ": " + entry.getValue() + "\n");
    }
    return new String(buffer);
  }


    /**
     * looks up the Exception by name within the storage.
     * @assumes Nothing
     * @effects Nothing
     * @param pExceptionName The name of the exception.
     * @return The Exception object stored by the given name or an
     * Exception object which designates that no object was found by
     * that name.
     */
    public MGIException getException (String pExceptionName)
    {
      MGIException e = (MGIException)exceptionsMap.get(pExceptionName);
      if (e == null) {
        throw new RuntimeException(
          "Internal error: an exception was raised " +
          "which has not yet been defined: " +
          "exception name " + pExceptionName + ". " +
          "This should be considered as a bug");
      }
      return (MGIException)e.clone(); // return a copy not original
    }

    /**
     * looks up the Exception by name within the storage and
     * set the parent exception on the found object.
     * @assumes Nothing
     * @effects Nothing
     * @param pExceptionName The name of the exception.
     * @param pExceptionParent The parent exception
     * @return The Exception object stored by the given name or an
     * Exception object which designates that no object was found by
     * that name.
     */
    public MGIException getException (String pExceptionName,
                                      Exception pExceptionParent)
    {
      MGIException e = getException(pExceptionName);
      if (e == null) {
        e = new MGIException(
          "Internal error: an exception was raised " +
          "which has not yet been defined: " +
          "exception name " + pExceptionName + ". " +
          "This should be considered as a bug", false);
      }
      // return a copy not original
      MGIException eCloned = (MGIException) e.clone();
      eCloned.setParent(pExceptionParent);
      return eCloned;
    }

}

// $Log$
// Revision 1.18  2003/09/23 20:07:53  mbw
// removed definition of unsupported method exception
//
// Revision 1.17  2003/09/18 18:59:50  mbw
// added a MethodNotFound exception
//
// Revision 1.16  2003/06/17 20:14:37  mbw
// now throws a new runtime exception if the lookup fails for an exception by name
//
// Revision 1.15  2003/06/04 17:05:25  mbw
// javadoc edits
//
// Revision 1.14  2003/05/22 15:49:51  mbw
// javadocs edits
//
// Revision 1.13  2003/05/07 23:09:42  mbw
// implemented cloning of exceptions from the base class hashmap
//
// Revision 1.12  2003/04/22 22:14:56  mbw
// added getException methods to this class and removed lookupException method
//
// Revision 1.11  2003/04/22 00:06:03  mbw
// added hashmap for storing Exceptions
// removed static definitions of exceptions (now being defined in subclasses)
//
// Revision 1.10  2003/04/18 12:41:20  mbw
// added new exception
//
// Revision 1.9  2003/04/15 11:59:06  mbw
// added more exceptions
//
// Revision 1.8  2003/04/10 00:44:49  mbw
// removed exception for filename not specified in config file
//
// Revision 1.7  2003/04/09 21:28:51  mbw
// removed an existing bcp execption no longer used
//
// Revision 1.6  2003/04/01 21:53:35  mbw
// resolved impact of class name change
//
// Revision 1.5  2003/03/21 15:51:33  mbw
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
