package org.jax.mgi.shr.dbutils;

import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import org.jax.mgi.shr.dto.DTO;

/**
 * @is a class for creating a string from a RowReference
 * @has a designated delimiter string for delimiting column output and
 * a ResultSetMetaData object for obtaining RowReference data
 * @does creates a String from a given RowReference
 */
public class RowToDTOInterpreter
    implements RowDataInterpreter {
  private ResultSetMetaData metadata = null;
  private String JDBCException = DBExceptionFactory.JDBCException;
  public RowToDTOInterpreter(ResultSetMetaData pMetadata) {
    metadata = pMetadata;
  }

  public Object interpret(RowReference rowReference) throws DBException {

    DTO dto = DTO.getDTO();
    if (metadata == null)
      throw new DBException("ResultSet metadata was null", false);
    try {
      int numColumns = metadata.getColumnCount();
      Object obj = null;
      for (int i = 1; i < numColumns + 1; i++) {
        dto.put(metadata.getColumnName(i), rowReference.getObject(i));
      }
    }
    catch (SQLException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(JDBCException, e);
      e2.bind("access RowReference columns for DTO creation");
      return e2;
    }
    return dto;
  }
}
