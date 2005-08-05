package org.jax.mgi.shr.ioutils;

import org.jax.mgi.shr.exception.ExceptionFactory;

   /**
    *  An ExceptionFactory for file input errors.
    * @has a hashmap of predefined IOUExceptions stored by a name key
    * @does looks up IOUExceptions by name
    * @company The Jackson Laboratory
    * @author M Walker
    */

    public class IOUExceptionFactory extends ExceptionFactory {
      /**
       * a file could not be found
       */
      public static final String FileNotFoundErr =
          "org.jax.mgi.shr.ioutils.FileNotFoundErr";
      static {
        exceptionsMap.put(FileNotFoundErr, new IOUException (
            "File named ?? could not be found", false));
      }
      /**
       * a file could not be opened
       */
      public static final String FileOpenErr =
          "org.jax.mgi.shr.ioutils.FileOpenErr";
      static {
        exceptionsMap.put(FileOpenErr, new IOUException (
            "Could not open file ??", false));
      }
      /**
       * a file could not be read from
       */
      public static final String FileReadErr =
          "org.jax.mgi.shr.ioutils.FileReadErr";
      static {
        exceptionsMap.put(FileReadErr, new IOUException (
            "Error reading from file ??", false));
      }
      /**
       * a file could not be written to
       */
      public static final String FileWriteErr =
          "org.jax.mgi.shr.ioutils.FileWriteErr";
      static {
        exceptionsMap.put(FileWriteErr, new IOUException (
            "Could not write to file ??", false));
      }
      /**
       * a file could not be created
       */
      public static final String FileCreateErr =
          "org.jax.mgi.shr.ioutils.FileCreateErr";
      static {
        exceptionsMap.put(FileCreateErr, new IOUException (
            "Could not create file ??", false));
      }
      /**
       * a file could not be closed
       */
      public static final String FileCloseErr =
          "org.jax.mgi.shr.ioutils.FileCloseErr";
      static {
        exceptionsMap.put(FileCloseErr, new IOUException (
            "Could not close file named ??", false));
      }
      /**
       * tried to read beyond end of file
       */
      public static final String NoRecordFound =
          "org.jax.mgi.shr.ioutils.NoRecordFound";
      static {
        exceptionsMap.put(NoRecordFound, new IOUException (
            "No record found", false));
      }
      /**
       * could not read source data
       */
      public static final String DataReadEOF =
          "org.jax.mgi.shr.ioutils.DataReadEOF";
      static {
        exceptionsMap.put(DataReadEOF, new IOUException (
            "Could not read data source", false));
      }
      /**
       * a data source could not be closed
       */
      public static final String CloseErr =
          "org.jax.mgi.shr.ioutils.CloseErr";
      static {
        exceptionsMap.put(CloseErr, new IOUException (
            "Could not close the data source", false));
      }
      /**
       * an error occured while transfering data between files
       */
      public static final String TransferErr =
          "org.jax.mgi.shr.ioutils.TransferErr";
      static {
        exceptionsMap.put(TransferErr, new IOUException (
            "Could not transfer data from file ?? to file ??", false));
      }

      /**
       * an object could not be instantiated based on a badly formattted
       * record
       */
      public static final String FormatErr =
          "org.jax.mgi.shr.ioutils.FormatErr";
      static {
        exceptionsMap.put(FormatErr, new IOUException (
            "The following record was found to badly formatted:\n??"
            , false));
      }
      /**
       * an object could not be instantiated based on a badly formattted
       * record
       */
      public static final String InterpretErr =
          "org.jax.mgi.shr.ioutils.InterpretErr";
      static {
        exceptionsMap.put(InterpretErr, new IOUException (
            "Could not instatiate new object based on the input data"
            , false));
      }
      /**
       * the input filename was not specified
       */
      public static final String NullFilename =
          "org.jax.mgi.shr.ioutils.NullFilename";
      static {
        exceptionsMap.put(NullFilename, new IOUException (
            "The input filename was not configured. Expected either " +
            "INFILE_NAME or OUTFILE_NAME.",
            false));
      }
      /**
       * the output filename was not specified
       */
      public static final String NullOutFilename =
          "org.jax.mgi.shr.ioutils.NullOutFilename";
      static {
        exceptionsMap.put(NullOutFilename, new IOUException (
            "The output filename was not configured. Expected OUTFILE_NAME.",
            false));
      }

      /**
       * could not read from standard in
       */
      public static final String StdioErr =
          "org.jax.mgi.shr.ioutils.StdioErr";
      static {
        exceptionsMap.put(StdioErr, new IOUException (
            "Cannot read from standard in.", false));
      }

      /**
       * sort command was interrupted
       */
      public static final String SortInterrupt =
          "org.jax.mgi.shr.ioutils.SortInterrupt";
      static {
        exceptionsMap.put(SortInterrupt, new IOUException(
            "The following sort command was interrupted:\n ??", false));
      }

      /**
       * io error during sort operation
       */
      public static final String SortIOErr =
          "org.jax.mgi.shr.ioutils.SortIOErr";
      static {
        exceptionsMap.put(SortIOErr, new IOUException(
            "The sort command threw IO error for the following command:\n ??",
            false));
      }

      /**
       * An interrupt signal was received during the execution of sort command
       */
      public static final String SortNonZero =
          "org.jax.mgi.shr.ioutils.SortNonZero";
      static {
        exceptionsMap.put(SortNonZero, new IOUException(
            "Sort command returned non-zero status when " +
            "executing the following command: ??\n??", false));
      }

      /**
       * An error occurred during a format call to an OutputFormatter
       */
      public static final String FormatterErr =
          "org.jax.mgi.shr.ioutils.FormatterErr";
      static {
        exceptionsMap.put(FormatterErr, new IOUException(
            "The following formatter failed on file ??: ??", false));
      }

      /**
       * Tried to run post formatting on an open file
       */
      public static final String FormatUnClosed =
          "org.jax.mgi.shr.ioutils.FormatUnClosed";
      static {
        exceptionsMap.put(FormatUnClosed, new IOUException(
            "Cannot post format the currently open file: ??", false));
      }

      /**
       * Errot running post formatting of data file
       */
      public static final String PostFormatErr =
          "org.jax.mgi.shr.ioutils.PostFormatErr";
      static {
        exceptionsMap.put(PostFormatErr, new IOUException(
            "Error running post format for file ??", false));
      }





}