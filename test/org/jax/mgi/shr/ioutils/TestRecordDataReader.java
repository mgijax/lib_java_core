package org.jax.mgi.shr.ioutils;

import junit.framework.*;
import java.io.*;
import org.jax.mgi.shr.unitTest.FileUtility;

public class TestRecordDataReader
    extends TestCase {
  private RecordDataReader recordDataReader = null;
  private String inputFilename = "Mm.data";
  private String inputFilename2 = "MmShort.data";
  private String compareFilename = "MmCompare.data";
  private String compareFilename2 = "MmShortCompare.data";
  private String compareFilename4 = "MmShortCompare.begindel.data";
  private String outputFilename = "MmTestFile.data";
  private DataInput1 dataInput1 = null;
  private DataInput2 dataInput2 = null;
  private DataCompare1 dataCompare1 = null;
  private DataCompare2 dataCompare2 = null;
  private DataCompare4 dataCompare4 = null;


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
    dataCompare4 = new DataCompare4(compareFilename4);
    dataInput1.createFile();
    dataInput2.createFile();
    dataCompare1.createFile();
    dataCompare2.createFile();
    dataCompare4.createFile();
  }

  protected void tearDown() throws Exception {
    dataInput1 = null;
    dataCompare1 = null;
    dataInput2 = null;
    dataCompare2 = null;
    dataCompare4 = null;
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

  public void testMultiLineRecordBytes() throws IOException {
      String del = "||";
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename);
    RecordDataReader rdr = new RecordDataReader(in.getChannel(), del.getBytes(), 512000);
    while (rdr.hasNext()) {
      String s = rdr.next();
      if (s.getBytes().length > 1)
      {
          out.write(s);
          out.write("\n-----------------------------------------------");
      }
    }
    out.write("\n");
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

  public void testStartByteDelimiter() throws IOException {
      String beginDel = "ID  ";
      String endDel = "\nSEQUENCE";
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename2);
    RecordDataReader rdr =
        new RecordDataReader(in.getChannel(), beginDel.getBytes(),
                             endDel.getBytes(), 2);
    while (rdr.hasNext()) {
      String s = rdr.next();
      if (s != null)
      {
          out.write(s);
          out.write("-----------------------------------------------\n");
      }
    }
    out.close();
    rdr.closeResources();
    in.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename4));
  }

  public void testStartRegexDelimiter() throws IOException {
      String beginDel = "^ID";
      String endDel = "^SEQUENCE";
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename2);
    RecordDataReader rdr =
        new RecordDataReader(in.getChannel(), beginDel,
                             endDel, 2);
    while (rdr.hasNext()) {
      String s = rdr.next();
      if (s != null)
      {
          out.write(s);
          out.write("-----------------------------------------------\n");
      }
    }
    out.close();
    rdr.closeResources();
    in.close();
    assertTrue(FileUtility.compare(outputFilename, compareFilename4));
  }



  public void testSmallInternalBufferSize() throws IOException {
      String del = "\n";
    BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
    FileInputStream in = new FileInputStream(inputFilename2);
    RecordDataReader rdr =
        new RecordDataReader(in.getChannel(), del.getBytes(), 2);
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
