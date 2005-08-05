package org.jax.mgi.shr.ioutils;

/**
 * An object which can format data objects for a specific report 
 * format and specifies headers, trailers and file name suffix
 * @has nothing.
 * @does formats report files
 * @company Jackson Laboratory
 * @author M. Walker
 */


public interface OutputFormatter {

    public void preprocess() throws IOUException;
    public void postprocess() throws IOUException;
    public String getHeader();
    public String getTrailer();
    public String getFileSuffix();
    public String format(Object data);

}
