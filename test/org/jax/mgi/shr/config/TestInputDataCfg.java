package org.jax.mgi.shr.config;

import junit.framework.*;
import org.jax.mgi.shr.unitTest.*;

public class TestInputDataCfg
    extends TestCase {
  private InputDataCfg fileCfg = null;
  //private FileUtility fileUtility = new FileUtility();
  private String config1 = "config1";

  public static void main(String args[]) {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(TestInputDataCfg.class);
    return suite;
  }

  public TestInputDataCfg(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    String s =
        "#format: sh\n" +
        "INFILE_NAME=testfile\n" +
        "INFILE_BEGIN_DELIMITER=^//\n" +
        "INFILE_BUFFERSIZE=2222\n" +
        "SECONDARY_INFILE_NAME=testfile2\n" +
        "SECONDARY_INFILE_END_DELIMITER=\\|\n" +
        "SECONDARY_INFILE_BUFFERSIZE=333\n";

    FileUtility.createFile(config1, s);

  }

  protected void tearDown() throws Exception {
    if (fileCfg != null)
      fileCfg.reinit();
    fileCfg = null;
    FileUtility.delete(config1);
    super.tearDown();
  }

  public void testGets() throws Exception {
    System.setProperty("CONFIG", config1);
    fileCfg = new InputDataCfg();
    assertEquals(new Integer(2222), fileCfg.getBufferSize());
    assertEquals("^//", fileCfg.getBeginDelimiter());
    assertEquals("return value", "testfile", fileCfg.getInputFileName());
  }

  public void testPrefixing() throws Exception {
    System.setProperty("CONFIG", config1);
    fileCfg = new InputDataCfg("SECONDARY");
    assertEquals(new Integer(333), fileCfg.getBufferSize());
    assertEquals("\\|", fileCfg.getEndDelimiter());
    assertEquals("return value", "testfile2", fileCfg.getInputFileName());

  }

}
