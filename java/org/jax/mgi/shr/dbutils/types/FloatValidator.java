package org.jax.mgi.shr.dbutils.types;

import java.sql.Timestamp;

import org.jax.mgi.shr.dbutils.ColumnDef;
import org.jax.mgi.shr.dbutils.DataException;
import org.jax.mgi.shr.types.Converter;
import org.jax.mgi.shr.types.TypesException;

/**
 *  A TypeValidator object for validating objects against a
 * column definition of type float
 * @has a column definition for type float and methods for validating
 * a given object against the represented column definition.
 * @does validates objects to be converted to floats
 * if validation fails.
 * @company Jackson Laboratory
 * @author M Walker
 */
public class FloatValidator extends TypeValidator
{

	/**
	 * default constructor which accepts the ColumnDefinition object for the
	 * represented column definition
	 * @param cd the given ColumnDefinition
	 */
	protected FloatValidator(ColumnDef cd)
	{
		super(cd);
	}

	/**
	 * validates a String object against the column definition for float
	 * @param s the given String
	 * @throws DataException
	 */
	public void validateString(String s) throws DataException
	{
		if (!columnDef.isNullable() && s.equals(""))
			failNullValue();
		try
		{
			Float f = Converter.toFloat(s);
		}
		catch (TypesException e)
		{
			failTypeConversion(s, "float");
		}

	}

	/**
	 * validates a Integer object against the column definition for float
	 * @param i the given Integer
	 * @throws DataException thrown if a validation error occurs
	 */
	public void validateInteger(Integer i) throws DataException
	{}

	/**
	 * validates a Float object against the column definition for float
	 * @param d the given Float
	 * @throws DataException thrown if a validation error occurs
	 */
	public void validateFloat(Float d) throws DataException
	{}

	/**
	 * validates a Boolean object against the column definition for float
	 * @param b the given Boolean
	 * @throws DataException thrown if a validation error occurs
	 */
	public void validateBoolean(Boolean b) throws DataException
	{
		failUnhandledConversion("boolean", "float");
	}

	/**
	 * validates a Timestamp object against the column definition for float
	 * @param t the given Timestamp
	 * @throws DataException thrown if a validation error occurs
	 */
	public void validateTimestamp(Timestamp t) throws DataException
	{
		failUnhandledConversion("timestamp", "float");
	}

}
