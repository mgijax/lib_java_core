package org.jax.mgi.shr.config;

import junit.framework.*;
import org.jax.mgi.shr.unitTest.*;

public class TestDatabaseCfg
    extends TestCase {
  private DatabaseCfg databaseCfg = null;
  private FileUtility fileUtility = new FileUtility();
  private String config1 = "config1";

  public TestDatabaseCfg(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    String s =
        "#format: sh\n" +
        "DBSERVER=PROD_MGI\n" +
        "DBNAME=mass_t\n" +
        "DBUSER=scott\n" +
        "DBURL=rohan.informatics.jax.org:4101\n" +
        "DBPASSWORD=tiger\n" +
        "DBPASSWORDFILE=/usr/local/mgi/dbutilities/.mgd_dbo_password\n" +
        "SECONDARY_DBSERVER=PUB_MGI\n" +
        "SECONDARY_DBNAME=radar\n" +
        "SECONDARY_DBUSER=mickey\n" +
        "SECONDARY_DBURL=rohan.informatics.jax.org:4100\n" +
        "SECONDARY_DBPASSWORD=mantle\n" +
        "SECONDARY_DBPASSWORDFILE=/usr/local/mgi/dbutilities/.mgd_dbo_password\n" +
        "SECONDARY_DBSCHEMADIR=/usr/tmp\n";

    FileUtility.createFile(config1, s);

  }

  protected void tearDown() throws Exception {
    databaseCfg.reinit();
    databaseCfg = null;
    FileUtility.delete(config1);
    super.tearDown();
  }

  public void testDefaults() throws Exception {
    databaseCfg = new DatabaseCfg();
    assertEquals("DEV_MGI", databaseCfg.getServer());
    assertEquals("mgd", databaseCfg.getDatabase());
    assertEquals("mgd_dbo", databaseCfg.getUser());
    assertEquals("rohan.informatics.jax.org:4100", databaseCfg.getUrl());
    assertNull(databaseCfg.getPassword());
    assertEquals("/usr/local/mgi/dbutils/mgidbutilities/.mgd_dbo_password", databaseCfg.getPasswordFile());
    // use secondary database
    databaseCfg = new DatabaseCfg("SECONDARY");
    assertEquals("DEV_MGI", databaseCfg.getServer());
    assertEquals("mgd", databaseCfg.getDatabase());
    assertEquals("mgd_dbo", databaseCfg.getUser());
    assertEquals("rohan.informatics.jax.org:4100", databaseCfg.getUrl());
    assertNull(databaseCfg.getPassword());
    assertEquals("/usr/local/mgi/dbutils/mgidbutilities/.mgd_dbo_password", databaseCfg.getPasswordFile());
    assertEquals("/usr/local/mgi/dbutils/mgd/mgddbschema", databaseCfg.getDBSchemaDir());
  }

  public void testGets() throws Exception {
    System.setProperty("CONFIG", config1);
    databaseCfg = new DatabaseCfg();
    databaseCfg.reinit();
    assertEquals("PROD_MGI", databaseCfg.getServer());
    assertEquals("mass_t", databaseCfg.getDatabase());
    assertEquals("scott", databaseCfg.getUser());
    assertEquals("rohan.informatics.jax.org:4101", databaseCfg.getUrl());
    assertEquals("tiger", databaseCfg.getPassword());
    assertEquals("/usr/local/mgi/dbutilities/.mgd_dbo_password", databaseCfg.getPasswordFile());
    // use secondary database
    databaseCfg = new DatabaseCfg("SECONDARY");
    assertEquals("PUB_MGI", databaseCfg.getServer());
    assertEquals("radar", databaseCfg.getDatabase());
    assertEquals("mickey", databaseCfg.getUser());
    assertEquals("rohan.informatics.jax.org:4100", databaseCfg.getUrl());
    assertEquals("mantle", databaseCfg.getPassword());
    assertEquals("/usr/local/mgi/dbutilities/.mgd_dbo_password", databaseCfg.getPasswordFile());
    assertEquals("/usr/tmp", databaseCfg.getDBSchemaDir());
  }

}

