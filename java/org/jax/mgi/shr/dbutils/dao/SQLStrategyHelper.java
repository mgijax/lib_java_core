package org.jax.mgi.shr.dbutils.dao;

/**
 * static methods for various SQLStrategy classes
 * @has satic helper methods
 * @does nothing
 * @company The Jackson Laboratory
 * @author M Walker
 *
 */

public class SQLStrategyHelper
{
    /**
     * return whether or not the given sql is a call to a stored procedure by
     * evaluating the string and determining if it starts with the 'exec'
     * keyword
     * @param sql the sql to evaluate
     * @return true if the sql is a call to a stored procedure, false otherwise
     */
    public static String convertToOtherProc(String sql)
    {
        if (sql.startsWith("{call "))
        {
            StringBuffer newSql = new StringBuffer("exec ");
            String[] tokens = sql.split("\\(");
            String[] subTokens = tokens[0].split(" ");
            String procName = subTokens[1];
            newSql.append(procName + " ");
            subTokens = tokens[1].split("\\)");
            newSql.append(subTokens[0]);
            return new String(newSql);
        }
        else
            return sql;
    }

    public static String convertToJDBCProc(String sql)
    {
        if (sql.startsWith("exec "))
        {
            StringBuffer newSql = new StringBuffer("{call ");
            String[] tokens = sql.split(" ");
            newSql.append(tokens[1]);
            newSql.append("(");
            for (int i = 2; i < tokens.length; i++)
            {
                newSql.append(tokens[i] + " ");
            }
            // lop off last space
            newSql.deleteCharAt(newSql.length() - 1);
            newSql.append(")}");
            return new String(newSql);
        }
        else
            return sql;
    }

}