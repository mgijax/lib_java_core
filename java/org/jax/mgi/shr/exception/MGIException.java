// $Header$
// $Name$

package org.jax.mgi.shr.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @is An exception object created by the ExceptionFactory class that
 *        may contain a hierarchy of other exception objects.  The "parent"
 *        attribute will contain the parent exception object if one exists.
 *        The parent exception can be another KnownException or a subclass
 *        of the Exception class.
 * @has
 * <UL>
 *   <LI> Exception attributes
 * </UL>
 * @does
 * <UL>
 *   <LI> Sets the exception attributes based on the parameters that are
 *        passed by the ExceptionFactory.
 *   <LI> Provides methods to bind variables to the exception message.
 *   <LI> Provides a method to get the exception message from this object
 *        and append the message of any parent exception objects.
 *   <LI> Overrides the toString method to return the exception message.
 * </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class MGIException
    extends ChainedException
    implements Cloneable {
  /////////////////
  //  Variables  //
  /////////////////

  // Exception object attributes.
  //
  private boolean dataRelated;

  // flag indicating whether or not include a stack trace in the message
  //
  private static boolean okToStackTrace = false;

  /**
   * constructor.
   * @assumes Nothing
   * @effects Nothing.
   * @param pMessage The text of the exception message.
   * @param pDataRelated Indicates whether or not the exception is an
   * error in the data.
   */
  public MGIException(String pMessage, boolean pDataRelated) {
    super(pMessage);
    dataRelated = pDataRelated;
    if (okToStackTrace) // include the stack trace in the message
    {
      ByteArrayOutputStream trace = new ByteArrayOutputStream();
      printStackTrace(new PrintStream(trace));
      replaceMessage(trace.toString());
    }
  }

  /**
   * Return the severity of this exception object.
   * @assumes Nothing
   * @effects Nothing
   * @param pDataRelated Indicates whether or not the exception is an
   * error in the data.
   */
  public void setDataRelated(boolean pDataRelated) {
    dataRelated = pDataRelated;
  }

  /**
   * Return whether this exception is data related.
   * @assumes Nothing
   * @effects Nothing
   * @return true if exception is data related or otherwise false.
   */
  public boolean isDataRelated() {
    return dataRelated;
  }

  /**
   * set whether or not to include a stack trace in the exception message
   * @param bool true or false
   */
  public static void setOkToStackTrace(boolean bool) {
    okToStackTrace = bool;
  }

  /**
   * get a RuntimeException for throwing within an unsupported method
   * @return the RuntimeException
   */
  public static RuntimeException getUnsupportedMethodException() {
    Exception e = new Exception();
    StackTraceElement trace[] = e.getStackTrace();
    StackTraceElement ste = trace[1];
    String className = ste.getClassName();
    String methodName = ste.getMethodName();
    String message = "Class " + className +
        " does not support the method " + methodName;
    return new java.lang.UnsupportedOperationException(message);

  }


}
// $Log$
// Revision 1.2  2004/01/05 18:31:35  mbw
// bug fix: now setOkToStackTrace() should be working
//
// Revision 1.1  2003/12/30 16:56:32  mbw
// imported into this product
//
// Revision 1.8  2003/10/03 16:55:31  mbw
// added the new setOkToStackTrace method
//
// Revision 1.7  2003/09/29 15:26:48  mbw
// added new method getUnsupportedMethodException()
//
// Revision 1.6  2003/06/04 17:05:26  mbw
// javadoc edits
//
// Revision 1.5  2003/05/22 15:49:51  mbw
// javadocs edits
//
// Revision 1.4  2003/05/16 15:09:58  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.3  2003/05/07 23:09:43  mbw
// implemented cloning of exceptions from the base class hashmap
//
// Revision 1.2  2003/04/22 22:14:06  mbw
// made constructor public
//
// Revision 1.1  2003/04/22 00:03:57  mbw
// renamed KnownException.java to MGIException.java
//
// Revision 1.6  2003/04/16 17:33:23  dbm
// Changes per design review
//
// Revision 1.5  2003/03/21 15:51:36  mbw
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
