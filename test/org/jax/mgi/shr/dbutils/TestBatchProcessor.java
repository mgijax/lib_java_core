package org.jax.mgi.shr.dbutils;

import junit.framework.TestCase;

import org.jax.mgi.shr.unitTest.*;
import org.jax.mgi.shr.log.*;



public class TestBatchProcessor extends TestCase
{
  private SQLDataManager sqlMgr = null;
  private TableCreator tc = null;
	
	public TestBatchProcessor(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		sqlMgr = new SQLDataManager();
		tc = new TableCreator(sqlMgr.getUrl(),sqlMgr.getDatabase(),
		                      sqlMgr.getUser(), sqlMgr.getPassword());
		tc.createDBsimple();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		sqlMgr = null;
		tc.dropDBsimple();
		tc = null;
		super.tearDown();
	}

	public void testExecuteBatch() throws Exception
	{
		BatchProcessor bp = sqlMgr.getBatchProcessor();
		bp.setLogger(new ConsoleLogger());
		bp.addBatch("insert into TEST_DBsimple values (1, 'uno')");
		bp.executeBatch();
	  ResultsNavigator nav = sqlMgr.executeQuery("select * from TEST_DBsimple");
		nav.next();
	  RowReference row = (RowReference)nav.getCurrent();
	  assertEquals(row.getInt(1).intValue(), 1);
	  assertEquals(row.getString(2), "uno");
	}
	
	public void testRollback() throws Exception
	{
		BatchProcessor bp = sqlMgr.getBatchProcessor();
		bp.setLogger(new ConsoleLogger());
		bp.addBatch("insert into TEST_DBsimple values (1, 'uno')");
		// force error
		bp.addBatch("bad sql");
		bp.addBatch("insert into TEST_DBsimple values (2, 'duece')");
		bp.addBatch("update TEST_DBsimple set columnB = 'wildcard'");
		try 
		{
			bp.executeBatch();
		}
		catch (BatchException e) 
		{
			int[] results = e.getUpdateCounts();
      assertEquals(1, results[0]);
      assertEquals(results[1], BatchException.ERROR);
      assertEquals(results[2], 1);
			assertEquals(results[3], 2);
		}
		ResultsNavigator nav = sqlMgr.executeQuery("select * from TEST_DBsimple");
		nav.next();
		RowReference row = (RowReference)nav.getCurrent();
		assertEquals(row.getInt(1).intValue(), 1);
		assertEquals(row.getString(2), "wildcard");
		nav.next();
		row = (RowReference)nav.getCurrent();
		assertEquals(row.getInt(1).intValue(), 2);
		assertEquals(row.getString(2), "wildcard");
	}

	public void testClose() throws Exception
	{
		BatchProcessor bp = sqlMgr.getBatchProcessor();
		bp.close();
		try
		{
			bp.addBatch("");
			//expect exception block to take over
			assertTrue(false);
		}
		catch (DBException e)
		{
			// this is expected
			assertTrue(true);
		}
	}

}
