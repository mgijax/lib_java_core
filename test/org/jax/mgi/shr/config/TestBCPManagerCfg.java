package org.jax.mgi.shr.config;

import junit.framework.*;
import org.jax.mgi.shr.unitTest.*;

public class TestBCPManagerCfg
    extends TestCase {
  private BCPManagerCfg bCPManagerCfg = null;
  private FileUtility fileUtility = new FileUtility();
  private String config1 = "config1";

  public TestBCPManagerCfg(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    String s =
        "#format: sh\n" +
        "BCP_PATH=bcppath\n" +
        "BCP_DELIMITER=space\n" +
        "BCP_PREVENT_EXECUTE=yes\n" +
        "BCP_OK_TO_OVERWRITE=NO\n" +
        "BCP_USE_TEMPFILE=true\n" +
        "BCP_REMOVE_AFTER_EXECUTE=FALSE\n" +
        "BCP_RECORD_STAMPING=true\n" +
        "BCP_TRUNCATE_TABLE=1\n" +
        "BCP_TRUNCATE_LOG=mispelled\n" +
        "BCP_DROP_INDEXES=true\n" +
        "JOBSTREAM=some loader\n" +
        "SECONDARY_BCP_PATH=bcppath2\n" +
        "SECONDARY_BCP_DELIMITER=tab\n" +
        "SECONDARY_BCP_PREVENT_EXECUTE=no\n" +
        "SECONDARY_BCP_OK_TO_OVERWRITE=YES\n" +
        "SECONDARY_BCP_USE_TEMPFILE=false\n" +
        "SECONDARY_BCP_REMOVE_AFTER_EXECUTE=1\n" +
        "SECONDARY_BCP_RECORD_STAMPING=0\n" +
        "SECONDARY_BCP_TRUNCATE_TABLE=false\n" +
        "SECONDARY_BCP_TRUNCATE_LOG=1\n" +
        "SECONDARY_BCP_DROP_INDEXES=true\n" +
        "SECONDARY_CREATED_BY=secondary loader\n" +
        "SECONDARY_MODIFIED_BY=secondary loader"
;

    FileUtility.createFile(config1, s);
  }

  protected void tearDown() throws Exception {
    if (bCPManagerCfg != null) {
      bCPManagerCfg.reinit();
      bCPManagerCfg = null;
    }
    FileUtility.delete(config1);
    super.tearDown();
  }

  public void testDefaults() throws Exception {
    bCPManagerCfg = new BCPManagerCfg();
    assertEquals(".", bCPManagerCfg.getPathname());
    assertEquals("\t", bCPManagerCfg.getDelimiter());
    assertEquals(new Boolean(false), bCPManagerCfg.getPreventExecute());
    assertEquals(new Boolean(false), bCPManagerCfg.getOkToOverwrite());
    assertEquals(new Boolean(false), bCPManagerCfg.getUseTempFile());
    assertEquals(new Boolean(false), bCPManagerCfg.getRemoveAfterExecute());
    assertEquals(new Boolean(false), bCPManagerCfg.getOkToRecordStamp());
    assertEquals(System.getProperty("user.name"), bCPManagerCfg.getJobStream());
  }

  public void testGets() throws Exception {
    System.setProperty("CONFIG", config1);
    bCPManagerCfg = new BCPManagerCfg();
    bCPManagerCfg.reinit();
    assertEquals("bcppath", bCPManagerCfg.getPathname());
    assertEquals("space", bCPManagerCfg.getDelimiter());
    assertEquals(new Boolean(true), bCPManagerCfg.getPreventExecute());
    assertEquals(new Boolean(false), bCPManagerCfg.getOkToOverwrite());
    assertEquals(new Boolean(true), bCPManagerCfg.getUseTempFile());
    assertEquals(new Boolean(false), bCPManagerCfg.getRemoveAfterExecute());
    assertEquals(new Boolean(true), bCPManagerCfg.getOkToRecordStamp());
    assertEquals("some loader", bCPManagerCfg.getJobStream());
  }

  public void testPrefixing() throws Exception {
    System.setProperty("CONFIG", config1);
    bCPManagerCfg = new BCPManagerCfg("SECONDARY");
    assertEquals("bcppath2", bCPManagerCfg.getPathname());
    assertEquals("tab", bCPManagerCfg.getDelimiter());
    assertEquals(new Boolean(false), bCPManagerCfg.getPreventExecute());
    assertEquals(new Boolean(true), bCPManagerCfg.getOkToOverwrite());
    assertEquals(new Boolean(false), bCPManagerCfg.getUseTempFile());
    assertEquals(new Boolean(true), bCPManagerCfg.getRemoveAfterExecute());
    assertEquals(new Boolean(true), bCPManagerCfg.getOkToTruncateLog());
    assertEquals(new Boolean(false), bCPManagerCfg.getOkToRecordStamp());
    assertEquals("some loader", bCPManagerCfg.getJobStream());
  }

  public void testBadBooleanString() throws Exception {
    System.setProperty("CONFIG", config1);
    bCPManagerCfg = new BCPManagerCfg();
    try {
      Boolean test = bCPManagerCfg.getOkToTruncateLog();
    }
    catch (Exception e) {
      // this is what we expect
      assertTrue(true);
      return;
    }
    // fail if we get here
    assertTrue(false);
  }



}
