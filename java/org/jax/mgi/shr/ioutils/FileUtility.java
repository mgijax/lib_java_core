package org.jax.mgi.shr.ioutils;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FileUtility {

    // the following are the exceptions that are thrown
    private static final String FileOpenErr =
        IOUExceptionFactory.FileOpenErr;
    private static final String FileReadErr =
        IOUExceptionFactory.FileReadErr;
    private static final String FileWriteErr =
        IOUExceptionFactory.FileWriteErr;
    private static final String FileCloseErr =
        IOUExceptionFactory.FileCloseErr;
    private static final String TransferErr =
        IOUExceptionFactory.TransferErr;
    private static final String FileCreateErr =
        IOUExceptionFactory.FileCreateErr;


    public static void deleteFile(String filename)
    {
        File file = new File(filename);
        file.delete();
    }

    public static void createFile(String filename)
    throws IOUException
    {
        File file = new File(filename);
        try
        {
            file.createNewFile();
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new
                IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(FileCreateErr, e);
            e2.bind(filename);
            throw e2;
        }
    }


    public static void copyFile(String infilename, String outfilename)
    throws IOUException
    {

        File infile = new File(infilename);
        File outfile = new File(outfilename);
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try
        {
            sourceChannel = new FileInputStream(infile).getChannel();
        }
        catch (FileNotFoundException e)
        {
            IOUExceptionFactory exceptionFactory = new
                IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(FileOpenErr, e);
            e2.bind(infilename);
            throw e2;
        }
        try
        {
            destinationChannel =
                new FileOutputStream(outfile).getChannel();
        }
        catch (FileNotFoundException e)
        {
           IOUExceptionFactory exceptionFactory = new
               IOUExceptionFactory();
           IOUException e2 = (IOUException)
               exceptionFactory.getException(FileOpenErr, e);
           e2.bind(outfilename);
           throw e2;
        }
        try
        {
            destinationChannel.transferFrom(sourceChannel, 0,
                                            sourceChannel.size());
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new
                IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(TransferErr, e);
            e2.bind(infilename);
            e2.bind(outfilename);
            throw e2;
        }
        try
        {
            sourceChannel.close();
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new
                IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(FileCloseErr, e);
            e2.bind(infilename);
            throw e2;
        }
        try
        {
            destinationChannel.close();
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new
                IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(FileCloseErr, e);
            e2.bind(outfilename);
            throw e2;
        }

    }

}