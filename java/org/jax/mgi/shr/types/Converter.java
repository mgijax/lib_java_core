// $Header$
// $Name$

package org.jax.mgi.shr.types;

import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


/**
 * @is A static object which handles java data type conversions for a known set
 * of java data types. The following types are supported:<br>
 * int, float, boolean, Integer, Float, Boolean, String and Timestamp
 * @has Static methods for doing java data type conversions.
 * @does Converts input between java types and strings and between
 * primitive types and the wrapper classes.<br>
 *
 * @company Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */


public class Converter {

  // the following constants are the exceptions used by this class
  private static final String UnhandledDataType =
      TypesExceptionFactory.UnhandledDataType;
  private static final String MalformedString =
      TypesExceptionFactory.MalformedString;

  // private constructor. use as static.
  private Converter() {
  }

  /**
   * create the wrapper class from the given primitive
   * @param input primitive
   * @return wrapper type
   */
  public static Integer wrap(int input) {
    return new Integer(input);
  }

  /**
   * create the wrapper class from the given primitive
   * @param input primitive
   * @return wrapper type
   */
  public static Float wrap(float input) {
    return new Float(input);
  }

  /**
   * create the wrapper class from the given primitive
   * @param input primitive
   * @return wrapper type
   */
  public static Boolean wrap(boolean input) {
    return new Boolean(input);
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String
   */
  public static String toString(int input) {
    return wrap(input).toString();
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String
   */
  public static String toString(float input) {
    return wrap(input).toString();
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String whose value would be either "0" or "1"
   */
  public static String toString(boolean input) {
    if (input)
      return "1";
    else
      return "0";
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String
   */
  public static String toString(Integer input) {
    return input.toString();
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String
   */
  public static String toString(Float input) {
    return input.toString();
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String whose value will be either "0" or "1"
   */
  public static String toString(Boolean input) {
    if (input.booleanValue())
      return "1";
    else
      return "0";
  }

  /**
   * convert data to String
   * @param input data for conversion
   * @return data converted to String
   */
  public static String toString(Timestamp input) {
    return input.toString();
  }

  /**
   * convert data from generic type object to String. The object type is
   * identified and if it is not of allowed type then a TypesException
   * is raised. See class definition for allowed types.
   * @param input data for conversion
   * @return data converted to String
   * @throws TypesException thrown if there is an error in conversion
   */
  public static String objectToString(Object input) throws TypesException {
    if (input instanceof String)
      return (String)input;
    else if (input instanceof Integer)
      return toString((Integer)input);
    else if (input instanceof Float)
      return toString((Float)input);
    else if (input instanceof Timestamp)
      return toString((Timestamp)input);
    else if (input instanceof Boolean)
      return toString((Boolean)input);
    else {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e =
          (TypesException)factory.getException(UnhandledDataType);
      e.bind(input.getClass().getName());
      throw e;
    }
  }

  /**
   *
   * convert data from String to primitive value. Strings representing
   * decimal numbers will cause a TypesException to be raised.
   * @param s data for conversion
   * @return data converted to primitive
   * @throws TypesException thrown if there is an error in conversion
   */
  public static int toPrimitiveInt(String s) throws TypesException {
    try {
      return (new Integer(s)).intValue();
    }
    catch (NumberFormatException e) {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e2 =
          (TypesException)factory.getException(MalformedString, e);
      e2.bind(s);
      e2.bind("int");
      throw e2;
    }
  }

  /**
   *
   * convert data from String to primitive value
   * @param s data for conversion
   * @return data converted to primitive
   * @throws TypesException thrown if there is an error in conversion
   */
  public static float toPrimitiveFloat(String s)
  throws TypesException
  {
    try {
      return (new Float(s)).floatValue();
    }
    catch (NumberFormatException e) {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e2 =
          (TypesException)factory.getException(MalformedString);
      e2.bind(s);
      e2.bind("float");
      throw e2;
    }
  }

  /**
   *
   * convert data from String to primitive value. "true" yields
   * true; all else yields false. The function is case insensitive.
   * @param s data for conversion
   * @return data converted to primitive
   * @throws TypesException thrown if there is an error in conversion
   */
  public static boolean toPrimitiveBoolean(String s) throws TypesException {
    return convertToBoolean(s);
  }

  /**
   *
   * convert data from String to Integer. Strings representing decimal
   * numbers will cause a TypesException to be raised.
   * @param s data for conversion
   * @return new object converted
   * @throws TypesException thrown if there is an error in conversion
   */
  public static Integer toInteger(String s) throws TypesException {
    try {
      return new Integer(s);
    }
    catch (NumberFormatException e) {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e2 =
          (TypesException)factory.getException(MalformedString, e);
      e2.bind(s);
      e2.bind("Integer");
      throw e2;
    }
  }

  /**
   *
   * convert data from String to Float
   * @param s data for conversion
   * @return new object converted
   * @throws TypesException
   */
  public static Float toFloat(String s) throws TypesException {
    try {
      return new Float(s);
    }
    catch (NumberFormatException e) {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e2 =
          (TypesException)factory.getException(MalformedString, e);
      e2.bind(s);
      e2.bind("Float");
      throw e2;
    }
  }

  /**
   *
   * convert data from String to Boolean. "True" or "true" yields
   * true; all else yields false.
   * @param s data for conversion
   * @return new object converted
   * @throws TypesException thrown if there is an error in conversion
   */
  public static Boolean toBoolean(String s) throws TypesException {
    return new Boolean(convertToBoolean(s));
  }

  /**
   *
   * convert data from String to Timestamp. the string format accepted is
   * any of the following patterns, with or without the time segments:<br>
   * yyyy-mm-dd hh:mm:ss.fffffffff<br>
   * yyyy/mm/dd hh:mm:ss.fffffffff
   * @param s data for conversion
   * @return new object converted
   * @throws TypesException thrown if there is an error in conversion
   */
  public static Timestamp toTimestamp(String s)
  throws TypesException
  {
    SimpleDateFormat format = null;
    Date date = null;
    format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      date = format.parse(s);
      return (new Timestamp(date.getTime()));
    }
    catch (ParseException e) {}
    format = new SimpleDateFormat("yyyy/MM/dd");
    try {
      date = format.parse(s);
      return (new Timestamp(date.getTime()));
    }
    catch (ParseException e) {}

    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
    try {
      date = format.parse(s);
      return (new Timestamp(date.getTime()));
    }
    catch (ParseException e) {}
    format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSSSSS");
    try {
      date = format.parse(s);
      return new Timestamp(date.getTime());
    }
    catch (ParseException e) {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e2 =
          (TypesException)factory.getException(MalformedString, e);
      e2.bind(s);
      e2.bind("Timestamp");
      throw e2;
    }
  }

  /**
   *
   * Convert a string to a boolean value.  "TRUE", "YES" or "1" are
   * equivalent to true.  "FALSE", "NO" or "0" are equivalent to false.
   * Anything else yields false.
   * @param s data for conversion
   * @return true or false
   * @throws TypesException thrown if there is an error in conversion
   */
  private static boolean convertToBoolean(String s) throws TypesException {
    if (s.toUpperCase().equals("TRUE") ||
      s.toUpperCase().equals("YES") ||
      s.equals("1"))
      return true;
    else if (s.toUpperCase().equals("FALSE") ||
      s.toUpperCase().equals("NO") ||
      s.equals("0"))
      return false;
    else {
      TypesExceptionFactory factory = new TypesExceptionFactory();
      TypesException e2 =
          (TypesException)factory.getException(MalformedString);
      e2.bind(s);
      e2.bind("boolean");
      throw e2;
    }
  }
}

// $Log$
// Revision 1.16  2003/10/22 15:32:28  mbw
// changed name of toString(Object) method to objectToString(Object)
//
// Revision 1.15  2003/05/22 15:50:52  mbw
// javadocs edits
//
// Revision 1.14  2003/05/16 15:11:49  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.13  2003/04/22 22:19:55  mbw
// added casting of MGIException to TypesException
//
// Revision 1.12  2003/04/22 00:10:09  mbw
// no longer throwing type KnownException
//
// Revision 1.11  2003/04/17 23:32:26  mbw
// made constructor private
//
// Revision 1.10  2003/04/08 22:44:29  mbw
// now throwing exception if string value dows not represent a boolean
//
// Revision 1.9  2003/04/03 01:10:27  mbw
// method has been changed for private use only
//
// Revision 1.8  2003/03/21 16:22:54  mbw
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
