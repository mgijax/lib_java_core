package org.jax.mgi.shr.types;

import junit.framework.*;
import java.sql.*;
import java.text.*;

public class TestConverter
    extends TestCase {
  //private Converter converter = null;

  public TestConverter(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    //converter = new Converter();
  }

  protected void tearDown() throws Exception {
    //converter = null;
    super.tearDown();
  }

  public void testStringToBoolean() throws Exception {
    String s = "TRUE";
    assertTrue(Converter.toBoolean(s).booleanValue());
    s = "false";
    assertTrue(!Converter.toBoolean(s).booleanValue());
    s = "1";
    assertTrue(Converter.toBoolean(s).booleanValue());
    s = "0";
    assertTrue(!Converter.toBoolean(s).booleanValue());
    s = "anything but true";
    try {
      assertTrue(!Converter.toBoolean(s).booleanValue());
    }
    catch (Exception ke) {
      // this is what we expect
      assertTrue(true);
      return;
    }
    // we shouldnt get here
    assertTrue(false);
  }

  public void testStringToFloat() throws Exception {
    String s = "1.8767564";
    float expectedReturn = (float)1.8767564;
    Float actualReturn = Converter.toFloat(s);
    assertEquals(expectedReturn, actualReturn.floatValue(), (float)0.0001);
  }

  public void testStringToInteger() throws Exception {
    String s = "1";
    int expectedReturn = 1;
    Integer actualReturn = Converter.toInteger(s);
    assertEquals("return value", expectedReturn, actualReturn.intValue());
    s = "1.0235";
    actualReturn = Converter.toInteger(s);
    assertTrue(false);
  }

  public void testStringToPrimitiveBoolean() throws Exception {
    String s = "true";
    boolean expectedReturn = true;
    boolean actualReturn = Converter.toPrimitiveBoolean(s);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testStringToPrimitiveFloat() throws Exception {
    String s = "1.098";
    float expectedReturn = (float)1.098;
    float actualReturn = Converter.toPrimitiveFloat(s);
    assertEquals(expectedReturn, actualReturn, (float)0.0001);
  }

  public void testStringToPrimitiveInt() throws Exception {
    String s = "100";
    int expectedReturn = 100;
    int actualReturn = Converter.toPrimitiveInt(s);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testStringToTimestamp() throws Exception, ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    java.util.Date date = formatter.parse("2003.07.04.00.00.00");
    Timestamp expectedReturn = new Timestamp(date.getTime());
    String s = "2003-07-04 00:00:00.0";
    Timestamp actualReturn = Converter.toTimestamp(s);
    assertEquals("return value", expectedReturn, actualReturn);
    s = "2003/7/4";
    actualReturn = Converter.toTimestamp(s);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromBoolean() {
    Boolean input = new Boolean(true);
    String expectedReturn = "1";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
    input = new Boolean(false);
    expectedReturn = "0";
    actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromFloat() {
    Float input = new Float((float)1.0987865611111);
    String expectedReturn = "1.0987866";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromInteger() {
    Integer input = new Integer(25);
    String expectedReturn = "25";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromTimestamp() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    java.util.Date date = formatter.parse("2003.07.04.00.00.00");
    Timestamp input = new Timestamp(date.getTime());
    String expectedReturn = "2003-07-04 00:00:00.0";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromPrimitiveBoolean() {
    boolean input = false;
    String expectedReturn = "0";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromPrimitiveFloat() {
    float input = (float)4.12341111111;
    String expectedReturn = "4.123411";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testToStringFromPrimitiveInt() {
    int input = -16;
    String expectedReturn = "-16";
    String actualReturn = Converter.toString(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testWrapBoolean() {
    boolean input = false;
    Boolean expectedReturn = new Boolean(false);
    Boolean actualReturn = Converter.wrap(input);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testWrapFloat() {
    float input = 0;
    Float expectedReturn = new Float(0);
    Float actualReturn = Converter.wrap(input);
    assertEquals("return value", expectedReturn.floatValue(), actualReturn.floatValue(), 0);
  }

  public void testWrapInt() {
    int input = 0;
    Integer expectedReturn = new Integer(0);
    Integer actualReturn = Converter.wrap(input);
    assertEquals("return value", expectedReturn.intValue(), actualReturn.intValue());
  }

}
