/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jax.mgi.shr.dbutils;

import junit.framework.TestCase;

import java.util.Vector;

import org.jax.mgi.shr.dbutils.dao.DAO;
import org.jax.mgi.shr.dbutils.dao.Script_Stream;
import org.jax.mgi.shr.exception.MGIException;

/**
 * @author mbw
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestScriptWriter extends TestCase
{

    ScriptWriter writer = null;
    SQLDataManager sqlMgr = null;
    Script_Stream stream = null;

    /**
     * Constructor for TestScriptWriter.
     * @param arg0
     */
    public TestScriptWriter(String arg0)
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
        writer = new ScriptWriter(sqlMgr);
        stream = new Script_Stream(writer);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        sqlMgr = null;
        writer = null;
        stream = null;
        super.tearDown();
    }

    public void testWrite() throws Exception
    {
        SimpleDAO dao = new SimpleDAO();
        //writer.write("This is string one");
        //writer.write("This is string one");
        //writer.go();
        //writer.execute();
        stream.insert(dao);
        stream.close();
    }

    public class SimpleDAO extends DAO
    {

        public String getUpdateSQL()
        {
            return new String("Update dao with this string");
        }

        public String getInsertSQL()
        {
            return new String("Insert dao with this string");
        }

        public String getDeleteSQL()
        {
            return new String("Delete dao with this string");
        }

        public Vector getBCPVector(Table table)
        {
            throw MGIException.getUnsupportedMethodException();
        }

        public Vector getBCPSupportedTables()
        {
            throw MGIException.getUnsupportedMethodException();
        }

    }

}
