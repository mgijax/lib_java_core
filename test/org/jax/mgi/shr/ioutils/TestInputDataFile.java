package org.jax.mgi.shr.ioutils;

import junit.framework.*;
import org.jax.mgi.shr.config.*;
import java.io.*;
import java.util.Properties;
import org.jax.mgi.shr.unitTest.FileUtility;

public class TestInputDataFile
    extends TestCase {

  private InputDataFile inputFile = null;
  private String filename = "Mm.data";
  private String filename2 = "MmShort.data";
  private String filename3 = "MmAllCaps.data";
  private String input4IteratorTest = "testInput";
  private String compareFilename = "MmCompare.data";
  private String compareFilename2 = "MmShortCompare.data";
  private String compareFilename3 = "MmAllCapsCompare.data";
  private String outputFilename = "MmTestFile.data";
  private String line1 = "this is line 1";
  private String line2 = "this is line 2";
  private String line3 = "this is line 3";
  private DataInput1 dataInput1 = null;
  private DataInput2 dataInput2 = null;
  private DataCompare1 dataCompare1 = null;
  private DataCompare1a dataCompare1a = null;
  private DataCompare2b dataCompare2b = null;
  private String config1 = "config1";
  private FileUtility fileUtility = new FileUtility();


  public TestInputDataFile(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    String s =
        "#format: sh\n" +
        "INFILE_NAME=testfile\n" +
        "INFILE_END_DELIMITER=^//\n" +
        "INFILE_BUFFERSIZE=2222\n";
    FileUtility.createFile(config1, s);
    s = line1 + "\n" + line2 + "\n" + line3;
    FileUtility.createFile(input4IteratorTest, s);
    dataInput1 = new DataInput1(filename);
    dataInput2 = new DataInput2(filename2);
    dataCompare1 = new DataCompare1(compareFilename);
    dataCompare1a = new DataCompare1a(compareFilename);
    dataCompare2b = new DataCompare2b(compareFilename2);
    dataInput1.createFile();
    dataInput2.createFile();
    dataCompare1.createFile();
    dataCompare2b.createFile();
  }

  protected void tearDown() throws Exception {
    inputFile = null;
    dataInput1 = null;
    dataCompare1 = null;
    dataInput2 = null;
    dataCompare2b = null;
    FileUtility.delete(filename);
    FileUtility.delete(filename2);
    FileUtility.delete(compareFilename);
    FileUtility.delete(compareFilename2);
    super.tearDown();
  }

  public void testDefaults() throws Exception {
    InputDataCfg fileCfg = new InputDataCfg();
    try {
      InputDataFile inputFile = new InputDataFile(fileCfg);
    }
    catch (IOUException e) {
      // this is what we expect with null filename
      assertTrue(true);
      return;
    }
    // we dont expect to get here
    assertTrue(false);
  }

  public void testInStreamDataManager1() throws Exception {
    System.setProperty("INFILE_NAME", filename2);
    InputDataCfg cfg = new InputDataCfg();
    inputFile = new InputDataFile(cfg);
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    RecordDataIterator it = inputFile.getIterator();
    while (it.hasNext()) {
      String s = (String)it.next();
      out.write(s);
      out.write("-----------------------------------------------\n");
    }
    out.close();
    it.close();
    FileUtility util = new FileUtility();
    assertTrue(FileUtility.compare(outputFilename, compareFilename2));
  }

  public void testInStreamDataManager2() throws Exception {
    System.setProperty("INFILE_END_DELIMITER", "^\\|\\|");
    System.setProperty("INFILE_NAME", filename);
    ConfigReinitializer.reinit();
    InputDataCfg configParm = new InputDataCfg();
    inputFile = new InputDataFile(configParm);
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    RecordDataIterator i = inputFile.getIterator();
    while (i.hasNext()) {
      String s = (String)i.next();
      out.write(s);
      out.write("-----------------------------------------------\n");
    }
    out.close();
    i.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename));
    Properties p = System.getProperties();
    p.remove("INFILE_END_DELIMITER");
    p.remove("INFILE_NAME");
    ConfigReinitializer.reinit();
  }

  public void testGetIterator() throws Exception {
    inputFile =
        new InputDataFile(filename3);
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    Interpreter interp = new Interpreter();
    RecordDataIterator i = inputFile.getIterator(interp);
    while (i.hasNext()) {
      String s = (String)i.next();
      out.write(s);
      out.write("-----------------------------------------------\n");
    }
    out.close();
    i.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename3));
  }

  public void testIterator1() throws Exception {
    inputFile =
        new InputDataFile(input4IteratorTest);
    inputFile.setEndDelimiter("$");
    RecordDataIterator i = inputFile.getIterator();
    i.hasNext();
    i.hasNext();
    i.hasNext();
    String s = (String)i.next();
    assertEquals(s, line1 + "\n");
    s = (String)i.next();
    assertEquals(s, line2 + "\n");
    s = (String)i.next();
    assertEquals(s, line3);
  }

  public void testIterator2() throws Exception {
    inputFile =
        new InputDataFile(input4IteratorTest);
    inputFile.setEndDelimiter("$");
    RecordDataIterator i = inputFile.getIterator();
    int count = 0;
    while (i.hasNext()) {
      count++;
      i.next();
    }
    assertEquals(count, 3);
  }

  public void testIterator3() throws Exception {
    inputFile =
        new InputDataFile(input4IteratorTest);
    inputFile.setEndDelimiter("$");
    RecordDataIterator i = inputFile.getIterator();
    int count = 0;
    while (i.hasNext()) {
      count++;
      i.next();
    }
    // at end of file, a call to next() should cause exception
    try {
      i.next();
    }
    catch (Exception e) {
      // this is what we expect
      assertTrue(true);
      return;
    }
    // we dont expect to make it here
    assertTrue(false);
    assertEquals(count, 3);
  }




  private class Interpreter implements RecordDataInterpreter {
    public Object interpret(String s) {
      return new String(s.toUpperCase());
    }
    public boolean isValid(String s) {
      return true;
    }
  }


}
