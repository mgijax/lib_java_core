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
  private DataInput3 dataInput3 = null;
  private DataCompare1 dataCompare1 = null;
  private DataCompare2 dataCompare2 = null;
  private DataCompare3 dataCompare3 = null;
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
        "INFILE_DELIMITER=^//\n" +
        "INFILE_BUFFERSIZE=2222\n";
    FileUtility.createFile(config1, s);
    s = line1 + "\n" + line2 + "\n" + line3;
    FileUtility.createFile(input4IteratorTest, s);
    dataInput1 = new DataInput1(filename);
    dataInput2 = new DataInput2(filename2);
    dataInput3 = new DataInput3(filename3);
    dataCompare1 = new DataCompare1(compareFilename);
    dataCompare2 = new DataCompare2(compareFilename2);
    dataCompare3 = new DataCompare3(compareFilename3);
    dataInput1.createFile();
    dataInput2.createFile();
    dataInput3.createFile();
    dataCompare1.createFile();
    dataCompare2.createFile();
    dataCompare3.createFile();
  }

  protected void tearDown() throws Exception {
    inputFile = null;
    dataInput1 = null;
    dataCompare1 = null;
    dataInput2 = null;
    dataCompare2 = null;
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
    System.setProperty("INPUT_FILENAME", filename2);
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
    System.setProperty("INPUT_DELIMITER", "^\\|\\|");
    System.setProperty("INPUT_FILENAME", filename);
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
    p.remove("INPUT_DELIMITER");
    p.remove("INPUT_FILENAME");
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
    inputFile.setDelimiter("$");
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
    inputFile.setDelimiter("$");
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
    inputFile.setDelimiter("$");
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
