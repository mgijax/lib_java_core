package org.jax.mgi.shr.dbutils;

import junit.framework.*;
import java.util.*;
import org.jax.mgi.shr.config.DatabaseCfg;

public class TestDBSchema
    extends TestCase {
  private DBSchema dBSchema = null;
  private DatabaseCfg dbConfig = null;
  private SQLDataManager sqlManager = null;

  public TestDBSchema(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    sqlManager = new SQLDataManager();
    dBSchema = sqlManager.getDBSchema();
  }

  protected void tearDown() throws Exception {
    dBSchema = null;
    dbConfig = null;
    super.tearDown();
  }


  public void testCreateIndexCommand() throws Exception {
    Vector returnVector = dBSchema.getCreateIndexCommands("ACC_AccessionMax");
    String expectedReturn = "create unique clustered index idx_prefixPart on ACC_AccessionMax (prefixPart) on seg0";
    assertEquals(expectedReturn, returnVector.get(0));
    expectedReturn = "create nonclustered index idx_modification_date on ACC_AccessionMax (modification_date) on seg1";
    assertEquals(expectedReturn, returnVector.get(1));
  }

  public void testDropIndexCommand() throws Exception {
    Vector returnVector = dBSchema.getDropIndexCommands("ACC_MGIType");
    String expectedReturn = "drop index ACC_MGIType.idx_MGIType_key";
    assertEquals(expectedReturn, returnVector.get(0));
    expectedReturn = "drop index ACC_MGIType.idx_name";
    assertEquals(expectedReturn, returnVector.get(1));
    expectedReturn = "drop index ACC_MGIType.idx_modification_date";
    assertEquals(expectedReturn, returnVector.get(2));
  }

  public void testCreateTableCommand() throws Exception {
    String actualReturn = dBSchema.getCreateTableCommand("ACC_AccessionMax");
    String expectedReturn = "create table ACC_AccessionMax ( prefixPart                     varchar(20)     not null, maxNumericPart                 int             not null, creation_date                  datetime        not null, modification_date              datetime        not null ) on seg0";
    //System.out.println(expectedReturn);
    //System.out.println(actualReturn);
    assertEquals(expectedReturn, actualReturn);
  }

  public void testCreatePartitionCommand() throws Exception {
   String actualReturn = dBSchema.getCreatePartitionCommand("ACC_Accession");
   String expectedReturn = "alter table ACC_Accession partition 3";
   assertEquals(expectedReturn, actualReturn);
 }

 public void testDropPartitionCommand() throws Exception {
  String actualReturn = dBSchema.getDropPartitionCommand("ACC_Accession");
  String expectedReturn = "alter table ACC_Accession unpartition";
  assertEquals(expectedReturn, actualReturn);
}





}
