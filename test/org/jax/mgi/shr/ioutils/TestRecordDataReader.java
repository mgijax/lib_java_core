package org.jax.mgi.shr.ioutils;

import junit.framework.*;
import java.io.*;
import org.jax.mgi.shr.unitTest.FileUtility;

public class TestRecordDataReader
    extends TestCase {
  private RecordDataReader recordDataReader = null;
  String inputFilename = "Mm.data";
  String inputFilename2 = "MmShort.data";
  String compareFilename = "MmCompare.data";
  String compareFilename2 = "MmShortCompare.data";
  String outputFilename = "MmTestFile.data";
  DataInput1 dataInput1 = null;
  DataInput2 dataInput2 = null;
  DataCompare1 dataCompare1 = null;
  DataCompare2 dataCompare2 = null;


  public TestRecordDataReader(String name) {
    super(name);
  }

  public static void main (String[] args) {
    //junit.textui.TestRunner.run (new TestRecordDataReader("testSingleLineRecord"));
    //junit.textui.TestRunner.run (new TestRecordDataReader("testMultiLineRecord"));
  }


  protected void setUp() throws Exception {
    super.setUp();
    dataInput1 = new DataInput1(inputFilename);
    dataInput2 = new DataInput2(inputFilename2);
    dataCompare1 = new DataCompare1(compareFilename);
    dataCompare2 = new DataCompare2(compareFilename2);
    dataInput1.createFile();
    dataInput2.createFile();
    dataCompare1.createFile();
    dataCompare2.createFile();
  }

  protected void tearDown() throws Exception {
    dataInput1 = null;
    dataCompare1 = null;
    dataInput2 = null;
    dataCompare2 = null;
    FileUtility.delete(inputFilename);
    FileUtility.delete(inputFilename2);
    FileUtility.delete(compareFilename);
    FileUtility.delete(compareFilename2);
    super.tearDown();
  }

  public void testMultiLineRecord() throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename);
    RecordDataReader rdr = new RecordDataReader(in.getChannel(), "^\\|\\|", 512000);
    while (rdr.hasNext()) {
      String s = rdr.next();
      out.write(s);
      out.write("-----------------------------------------------\n");
    }
    out.close();
    rdr.closeResources();
    in.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename));
  }


  public void testSingleLineRecord() throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename2);
    RecordDataReader rdr = new RecordDataReader(in.getChannel(), "$", 512000);
    while (rdr.hasNext()) {
      String s = rdr.next();
      out.write(s);
      out.write("-----------------------------------------------\n");
    }
    out.close();
    rdr.closeResources();
    in.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename2));
  }

  public void testSmallBufferSize() throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename2);
    RecordDataReader rdr = new RecordDataReader(in.getChannel(), "$", 2);
    while (rdr.hasNext()) {
      String s = rdr.next();
      out.write(s);
      out.write("-----------------------------------------------\n");
    }
    out.close();
    rdr.closeResources();
    in.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename2));
  }

}
