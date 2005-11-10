/**
 * Title:        MGI Driver Manager
 * Description:  A convenience library to allow Java clients to easily use
 *               JNDI/LDAP or a direct database URL to connect to a database.
 * Limitations:  Currently Sybase-specific, but can probably be changed easily
 *               to support multiple database vendors.
 * Copyright:    Copyright (c) 2000 The Jackson Laboratory
 * Company:      Mouse Genome Informatics
 * @author       gld
 */

package org.jax.mgi.shr.dbutils;

// Standard packages needed for Sybase SQL

import java.util.*;
import java.sql.*;

// JNDI stuff
import javax.naming.*;

/**
* MGIDriverManager is a customized ConnectionManager class that MGIers use to
* get database connections. It is not a subclass of jdbc2.DriverManager because
* that class has a private constructor, and can thus not be derived from.
* @author gld
*/
public class MGIDriverManager implements ConnectionManager
{
   /**
    * Returns a database connection to the server/database implied by
    *  the ldapurl_or_hostport. Note that "dataserver" is ignored if a non-LDAP
    *  URL is provided (since the name is not needed, and can not be used
    *  without consulting LDAP services).
    *  @param dataserver the database server ("DEV_MGI")
    *  @param database the database          ("mgd")
    *  @param user the database user
    *  @param password the user's password
    *  @param ldapurl_or_hostport the LDAP URL or a direct database URL
    *   ("ldap://<host>:<port>" or "host:port")
    *  @return a database Connection
    *  @throws SQLException (if connection cannot be created)
    */
   public static Connection getConnection(String dataserver,
                            String database, String user, String password,
                            String ldapurl_or_hostport)
      throws SQLException
       {
         Properties props = new Properties();
         String connectionURL;

         if (isldapURL(ldapurl_or_hostport)){
            String ldapServerURL = ldapurl_or_hostport;
            props.put(Context.INITIAL_CONTEXT_FACTORY,
                     "com.sun.jndi.ldap.LdapCtxFactory");

            // identify the LDAP server
            props.put(Context.PROVIDER_URL, ldapServerURL);

            // "ldap://mishka:2021" is an example of an LDAP server url.
            connectionURL = "jdbc:sybase:jndi:" + ldapServerURL +
                            "/database=" + dataserver + ".." +
                            database + ",ou=Databases,o=MGI";
         }
         else {
            String directDatabaseURL = ldapurl_or_hostport;
            connectionURL = "jdbc:sybase:Tds:" +
                             directDatabaseURL + "/" + database;
         }

         // initialize the properties for the connection
         props.put("user",user);
         props.put("password",password);
         return DriverManager.getConnection(connectionURL, props);
      }

      /**
       *  Loads the database driver. Must be called prior to using
       *  getConnection().
       *  requires: nothing
       *  modifies: nothing
       */
      public static void init()
      {
          String classpath = System.getProperty("java.class.path",".");
          int jconn3 = classpath.indexOf("jconn3");
          int jconn2 = classpath.indexOf("jconn2");
          String driver = "com.sybase.jdbc2.jdbc.SybDriver";
          if ((jconn3 > 0 && (jconn2 < 0 || jconn3 < jconn2)))
              driver = "com.sybase.jdbc3.jdbc.SybDriver";

        try {
          Driver d =
          (Driver) Class.forName(driver).newInstance();
           DriverManager.registerDriver(d);
        }
        catch (Exception ex)
        {
           System.err.println("Error Message: " + ex.getMessage());
        }
      }

      /**
       * get a database connection
       * @param database the name of the database
       * @param user the name of the user for the login account
       * @param password the password for the login account
       * @param directDatabaseURL the url for the JDBC connection
       * @return the connection
       * @throws SQLException thrown if there is an error connecting to the
       * database
       */
      public Connection getConnection(String database,
                                      String user,
                                      String password,
                                      String directDatabaseURL)
          throws SQLException
      {
        init();
        return getConnection(null, database, user,
                             password, directDatabaseURL);
      }


      /**
       *  Identifies if "url" is an ldapURL or a direct database url.
       *  @param url a valid database connection url for JDBC
       *  @return true, if the URL starts with "ldap:", false otherwise.
       */
      private static boolean isldapURL(String url)
      {
         if (url.startsWith("ldap:"))
            return true;
         return false;
      }
}

// $Log$
// Revision 1.3.30.1  2005/09/07 15:09:28  mbw
// added support for JConnect 6.0 driver
//
// Revision 1.5  2005/09/07 15:04:19  mbw
// reverted previous commit to incorporate on branch tr7067 instead
//
// Revision 1.3  2004/07/26 16:22:49  mbw
// Formatting only
//
// Revision 1.2  2004/07/21 19:42:21  mbw
// javadocs edits only
//
// Revision 1.1  2003/12/30 16:50:30  mbw
// imported into this product
//
// Revision 1.4  2003/12/09 22:49:02  mbw
// merged jsam branch onto the trunk
//
// Revision 1.2.2.3  2003/06/17 20:21:36  mbw
// now implements the ConnectionManager interface
//
// Revision 1.2.2.2  2003/05/08 01:56:13  mbw
// incorporated changes from code review
//
// Revision 1.2.2.1  2003/03/21 16:52:58  mbw
// added standard header/footer
//
/**************************************************************************
*
* Warranty Disclaimer and Copyright Notice
*
*  THE JACKSON LABORATORY MAKES NO REPRESENTATION ABOUT THE SUITABILITY OR
*  ACCURACY OF THIS SOFTWARE OR DATA FOR ANY PURPOSE, AND MAKES NO WARRANTIES,
*  EITHER EXPRESS OR IMPLIED, INCLUDING MERCHANTABILITY AND FITNESS FOR A
*  PARTICULAR PURPOSE OR THAT THE USE OF THIS SOFTWARE OR DATA WILL NOT
*  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS, OR OTHER RIGHTS.
*  THE SOFTWARE AND DATA ARE PROVIDED "AS IS".
*
*  This software and data are provided to enhance knowledge and encourage
*  progress in the scientific community and are to be used only for research
*  and educational purposes.  Any reproduction or use for commercial purpose
*  is prohibited without the prior express written permission of The Jackson
*  Laboratory.
*
* Copyright \251 1996, 1999, 2002 by The Jackson Laboratory
*
* All Rights Reserved
*
**************************************************************************/
