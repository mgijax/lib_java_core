// $Header$
// $Name$

package org.jax.mgi.shr.ioutils;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.FileInputStream;


/**
 * @is An object that iterates through a file as a collection of
 * records.
 * @has a file and a record delimiter.
 * @does Manages the separating of data from an input file into records
 * based on a given record delimiter.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */

public class RecordDataReader {

  // max length allocated for one line of text
  private int MAX_LINE_SIZE = 512000;

  // used to convert bytes to chars
  private String CHARSET = "US-ASCII";

  // the delimiters used for regular expression matching
  private String beginDelimiter = null;
  private String endDelimiter = null;

  // the byte arrays used for delimiter sequence matching
  private byte[] matchSequenceEnd = null;
  private byte[] matchSequenceBegin = null;

  // decoder for decoding bytes into unicode characters
  private CharsetDecoder decoder = (Charset.forName(CHARSET)).newDecoder();

  // end of line character for UNIX and PC
  private int EOL = 10;

  // nio buffer
  private ByteBuffer byteBuffer = null;

  // nio channel
  private ReadableByteChannel channel = null;

  // one of the choice of inner classes determined by the constructor
  private BufferReader bufferReader = null;

  // indicator for whether or not to use a begin delimiter when extracting
  // records
  private boolean useBeginDelimiter;

  /**
   * constructor which allows controlling the size of the internal buffer and
   * forces regular expression matching
   * @param in the input stream to read
   * @param delimiter the regular expression used for delimiting records
   * @param bufferSize the value to set on the internal buffer size
   * @throws IOException thrown if the channel cannot be read from
   */

  public RecordDataReader(ReadableByteChannel in,
                          String delimiter,
                          int bufferSize)
  throws IOException {
    this.channel = in;
    this.endDelimiter = delimiter;
    this.useBeginDelimiter = false;
    this.bufferReader = new RegexReader();
    // create nio buffer to specified size
    this.byteBuffer = ByteBuffer.allocate(bufferSize);
    // clear and prepare the nio buffer for reading
    prepareNewBuffer();
  }

  /**
   * constructor which allows controlling the size of the internal buffer and
   * forces regular expression matching
   * @param in the input stream to read
   * @param delimiter the regular expression used for delimiting records
   * @param bufferSize the value to set on the internal buffer size
   * @throws IOException thrown if the channel cannot be read from
   */

  public RecordDataReader(ReadableByteChannel in,
                          String beginDelimiter,
                          String endDelimiter,
                          int bufferSize)
  throws IOException {
    this.channel = in;
    this.useBeginDelimiter = true;
    this.beginDelimiter = beginDelimiter;
    this.endDelimiter = endDelimiter;
    this.bufferReader = new RegexReader();
    // create nio buffer to specified size
    this.byteBuffer = ByteBuffer.allocate(bufferSize);
    // clear and prepare the nio buffer for reading
    prepareNewBuffer();
  }

  /**
   * constructor which allows controlling the size of the internal buffer and
   * providing the end delimiter as a byte arrays
   * @param in the input stream to read
   * @param matchSequence the byte array used for delimiting records
   * @param bufferSize the value to set on the internal buffer size
   * @throws IOException
   */

  public RecordDataReader(ReadableByteChannel in,
                          byte[] matchSequence,
                          int bufferSize)
  throws IOException {
    this.channel = in;
    this.useBeginDelimiter = false;
    this.matchSequenceEnd = matchSequence;
    this.bufferReader = new ByteReader();
    // create nio buffer to specified size
    this.byteBuffer = ByteBuffer.allocate(bufferSize);
    // clear and prepare the nio buffer for reading
    prepareNewBuffer();
  }

    /**
     * constructor which allows controlling the size of the internal buffer and
     * providing the begin and end delimiters as byte arrays
     * @param in the input stream to read
     * @param matchSequence the byte array used for delimiting records
     * @param bufferSize the value to set on the internal buffer size
     * @throws IOException
     */

    public RecordDataReader(ReadableByteChannel in,
                            byte[] matchSequenceBegin,
                            byte[] matchSequenceEnd,
                            int bufferSize)
    throws IOException {
      this.channel = in;
      this.useBeginDelimiter = true;
      this.matchSequenceBegin = matchSequenceBegin;
      this.matchSequenceEnd = matchSequenceEnd;
      this.bufferReader = new ByteReader();
      // create nio buffer to specified size
      this.byteBuffer = ByteBuffer.allocate(bufferSize);
      // clear and prepare the nio buffer for reading
      prepareNewBuffer();
    }


  /**
   * designates whether more records are available for reading
   * @return true or false
   */
  public boolean hasNext() {
    return byteBuffer.hasRemaining();
  }

  /**
   * returns the next record from the file
   * @return the record
   * @throws IOException
   */
  public String next() throws IOException {
    return bufferReader.getRecord();
  }

  /**
   * close the channel associated with the input file
   * @throws IOException
   */
  public void closeResources() throws IOException {
    byteBuffer = null;
    if (channel != null)
      channel.close();
    bufferReader.close();
  }


  /**
   * clears the existing buffer and prepares for another read from the channel
   * @throws IOException thrown if the channel cannot be read from
   */
  private void prepareNewBuffer() throws IOException {
    byteBuffer.clear();
    channel.read(byteBuffer);
    // prepare the buffer for reading
    byteBuffer.flip();
    bufferReader.init();
  }


  /**
   * gets a new channel from the input stream and prepares a buffer for
   * reading
   * @param in input stream
   * @param bufferSize the value to set for the buffer size
   * @throws IOException
   */
  private void setup(FileInputStream in, int bufferSize)
  throws IOException {

    // get nio channel from stream
    channel = in.getChannel();
    // create nio buffer to specified size
    byteBuffer = ByteBuffer.allocate(bufferSize);
    // clear and prepare the nio buffer for reading
    prepareNewBuffer();
  }

  /**
   * an interface defining the BufferReader class implemented by
   * the two inner classes, RegexReader and ByteReader
   */
  private interface BufferReader {
    /*
     * perform any initialitaion
     * @asumes nothing
     * @effects implementation specific
     */
    public void init();
    /**
     * get the next record from the byte buffer
     * @return the next record as a String
     * @throws IOException thrown if there is an error reading from the
     * byte buffer
     */
    public String getRecord() throws IOException;
    /**
     * perform any final functions
     * @assumes nothing
     * @effects nothing
     */
    public void close();
  }

  /**
   * @is implements the BufferReader interface and reads the byte buffer
   * byte by byte and finds the record seperator by a direct comparison to
   * the given byte sequence. This class provides greater performance over
   * the RegexReader
   * @has a match sequence and a byte buffer
   * @does reads a record from the byte buffer
   */
  private class ByteReader
      implements BufferReader
  {

      /**
       * the match sequence index pointers which increment on each
       * consecutive byte match of a match sequence. When an index reaches
       * the length of the match sequence, the match is complete. When there is
       * a byte mismatch before an index reaches the length of the sequence,
       * the index is reset to zero
       */
      private int endDelIndex = 0;
      private int beginDelIndex = 0;

      private CharBuffer charBuffer = null;

      /**
       * required for the interface but nothing is performed
       */
      public void init()
      {}

      /**
       * returns the next record from the byte buffer
       * @return the record as a String
       * @throws IOException thrown if there is an eroor reading from the byte
       * buffer
       */
      public String getRecord()
          throws IOException
      {
          /**
           * the record to be built
           */
          String record = null;
          /**
           * Care must be taken when the end of the byte buffer is reached and
           * new data must be read into the buffer.
           * The following constants define three states this method can be in
           * when a buffer runs out of characters and requires refilling:
           * still looking for start delimiter, looking for end delimiter,
           * currently evaluating the start delimiter (that is, byte buffer
           * characters are matching the start delimiter, but we havent reached
           * the end of the start delimiter yet), and the end delimiter was
           * found.
           */
          final int LOOKING_BEGINDEL = 1;
          final int LOOKING_ENDDEL = 2;
          final int EVALUATING_BEGINDEL = 3;
          final int FOUND_ENDDEL = 4;

          /**
           * indicator for whether or no a begin delimiter was partially
           * read when the internal buffer reaches the end
           */
          boolean partialBeginDel = false;

          int state = LOOKING_BEGINDEL;
          byte b = 0;
          while (state != FOUND_ENDDEL && byteBuffer.hasRemaining())
          {
              int startPosition = byteBuffer.position();
              if (useBeginDelimiter &&
                  (state == LOOKING_BEGINDEL || state == EVALUATING_BEGINDEL))
              {
                  // read byte by byte from buffer comparing each byte to the
                  // begin match sequence until the beginning of record is
                  // found.
                  // If the buffer needs refilling from the channel, then stop
                  // parsing and refill the buffer
                  while (byteBuffer.hasRemaining())
                  {
                      b = byteBuffer.get();
                      // are the incoming bytes still matching the match
                      // sequence?
                      if (b == matchSequenceBegin[beginDelIndex])
                      {
                          beginDelIndex++; // increment the match sequence
                          state = EVALUATING_BEGINDEL;
                      }
                      else // no longer matching
                      {
                          beginDelIndex = 0; // reset the match sequence
                          state = LOOKING_BEGINDEL;
                          if (partialBeginDel) // clear the saved partial
                          {
                              record = null;
                              partialBeginDel = false;
                          }
                      }
                      if (beginDelIndex == matchSequenceBegin.length)
                      { // we reached the end of the match sequence for the
                        // begin delimiter
                          startPosition = byteBuffer.position() - beginDelIndex;
                          if (startPosition < 0) // buffer was refilled
                              startPosition = 0;
                          state = LOOKING_ENDDEL;
                          beginDelIndex = 0; // reset the match sequence
                          break;
                      }
                  }
                  /**
                   * if we are in the middle of evaluating a begin sequence
                   * when the buffer runs out, then we need to set the
                   * start position to the beginning of the begin delimiter
                   */
                  if (state == EVALUATING_BEGINDEL)
                  {
                      startPosition = byteBuffer.position() - beginDelIndex;
                      if (startPosition < 0)
                          startPosition = 0;
                  }
              }
              else // not using a begin delimiter
              {
                  state = LOOKING_ENDDEL;
                  startPosition = byteBuffer.position();
              }
              /**
               * read byte by byte from buffer comparing each byte to the match
               * sequence until the end of record delimiter is found.
               * If the buffer needs refilling from the channel, then store the
               * current characters for the current line and record in a cache
               * before refilling the buffer and later append to them when
               * processing the new buffer.
               */
              while (byteBuffer.hasRemaining())
              {
                  b = byteBuffer.get();
                  // are the incoming bytes still matching the match sequence?
                  if (b == matchSequenceEnd[endDelIndex])
                      endDelIndex++; // increment the match sequence
                  else
                      endDelIndex = 0; // reset the match sequence
                  if (endDelIndex == matchSequenceEnd.length)
                  { // we reached the end of the match sequence for the
                    // end delimiter
                      state = FOUND_ENDDEL;
                      endDelIndex = 0; // reset the match sequence
                      break;
                  }
              }
              /**
               * to get here indicates either end of buffer or end of record or
               * both. What happens at this point depends on the state the
               * process is in. If it is currently searching for the begin
               * delimiter, then just refill the buffer without saving any
               * record data. If the state is currently searching for the end
               * delimiter, then store the buffer characters currently read for
               * this record into storage. This storage can then be used to
               * append charcters from subsequent buffers. If the state is
               * currently in the process of evaluating the start buffer, then
               * store the start delimiter characters read so far into storage
               * (this storage be set to null if the start delimiter is not
               * found afterall). If we are actually at the end of the file,
               * then the current record will be returned to the caller
               * regardless of whether or not the end of record delimiter was
               * reached.
               */
              if (state != LOOKING_BEGINDEL)
              {
                  charBuffer = CharBuffer.allocate(byteBuffer.position() -
                      startPosition);
                  int currentPosition = byteBuffer.position();
                  byteBuffer.position(startPosition);
                  decoder.decode(byteBuffer, charBuffer, true);
                  byteBuffer.position(currentPosition);
                  /**
                   * set or append the current record instance variable.
                   * partial records could have been created if a previous
                   * buffer filled up and we had to temporarily store a partial
                   * record and refill the buffer.
                   */
                  if (record == null)
                      record =
                          ((CharBuffer) charBuffer.position(0)).toString();
                  else
                      record =
                          record.concat(
                          ((CharBuffer) charBuffer.position(0)).toString());
                    /**
                     * if the state is currently evaluating a begin delimiter
                     * then the partial record being cached contains the
                     * partial begin delimiter read so far. This will have to
                     * cleared if it turns out that the begin delimiter does
                     * not match afterall
                     */
                  if (state == EVALUATING_BEGINDEL)
                      partialBeginDel = true;
              }
              if (!byteBuffer.hasRemaining()) // buffer needs to be refilled
                  prepareNewBuffer();
          }
          return record;
      }

      public void close()
      {}
  }  // end ByteReader class

  /**
   * @is implements the BufferReader interface and provides an interpretation
   * of the byte buffer as characters and uses regular expressions as record
   * delimiters
   * @has a character view of the byte buffer and a regular expression for
   * the begin and end delimiters
   * @does reads a record from the byte buffer by matching the line contents
   * with the regular expressions for the begin and end delimiters.
   */
  private class RegexReader implements BufferReader {

    // the current record being read (returned on call to next())
    private String currentRecord = null;

    // the current line being read.
    // the record delimiter is tested on a line by line basis
    private CharSequence currentLine = null;

    private Pattern regexEndDel= null;
    private Pattern regexBeginDel = null;

    private Matcher matcher = null;

    // another view of the nio buffer as the current line being read.
    private ByteBuffer lineBuffer = null;

    // another view of the nio buffer as the current record being read.
    private ByteBuffer recordBuffer = null;


    public void init() {
      // create two buffer views for line and records.
      // these buffer positions are independent of the byte buffer, so they
      // remain static as we read from the byte buffer.
      // when we want to refer to a line or a record we can take the position
      // of these buffers as the start positions. The position of the byte
      // buffer will indicate the end of the line and record.
      lineBuffer = byteBuffer.duplicate();
      recordBuffer = byteBuffer.duplicate();
      regexEndDel = Pattern.compile(endDelimiter);
      if (useBeginDelimiter)
      {
          regexBeginDel = Pattern.compile(beginDelimiter);
      }

    }

    /**
     * returns the next record from the buffer
     * @return the record
     * @throws IOException
     */
    public String getRecord() throws IOException {
      // if multiple buffers have to be read from the channel then some records
      // and some lines may be only partially read. Boolean indicators
      // endOfRecord and endOfLine have been defined to indicate this.
      boolean endOfRecord = false;
      boolean endOfLine = false;

      // start a new record
      currentRecord = null;

      byte b = 0;
      // read byte by byte from buffer until end of record or end of buffer
      // has been reached. The end of record delimiter is tested against each
      // line read. Check each byte for the end of line (EOL) character and
      // test each line for end of record delimiter.
      // If the buffer needs refilling from the channel, then store the
      // characters for the current line and record in a cache before
      // refilling the buffer and later append to them when processing the
      // new buffer.
      while (!endOfRecord && byteBuffer.hasRemaining()) {
        while (byteBuffer.hasRemaining()) {
          b = byteBuffer.get();
          if (b == EOL) {
            endOfLine = true;
            readCurrentLine();
            // found end of line...check if end of record
            matcher = regexEndDel.matcher(currentLine);
            if (matcher.find()) {
              endOfRecord = true;
              currentLine = null;
              endOfLine = false;
              break;
            }
            currentLine = null;
            endOfLine = false;
          }
        }
        // to get here indicates either end of buffer or end of record or both.
        // read the buffer characters into the current record variable and
        // if we are at the end of the buffer, also read buffer characters
        // into current line variable and refill buffer.
        // If we are actually at the end of the file then the current record
        // will be returned.
        readCurrentRecord(); // sets value of the currentRecord instance
        if (!byteBuffer.hasRemaining()) { // buffer needs to be refilled
          if (!endOfLine)
            readCurrentLine(); // store partial line before refilling buffer
          prepareNewBuffer();
        }
      }
      // getting here indicates end of record.
      // this could be null if no records exist in the file.
      if (currentRecord == null) {
        throw new IOException("Attempting to read past end of file");
      }
      return currentRecord;
    }

    public void close() {}

    /**
     * reads a record from the nio buffer and stores it as an instance variable
     */
    private void readCurrentRecord() throws IOException {
      // the record has been determined and the size is known
      // therefore create a CharBuffer and decode the record from the raw bytes.
      // the record consist of characters between the record buffer position
      // which was created at the start of the record and the current byte
      // buffer position which is currently at the end of the record.
      CharBuffer charBuffer = CharBuffer.allocate(
            byteBuffer.position() - recordBuffer.position());
      decoder.decode(recordBuffer, charBuffer, true);
      // set or append the current record instance variable.
      // partial records could have been created if a previous buffer filled up
      // and we had to temporarily store a partial record and refill the buffer.
      if (currentRecord == null)
        currentRecord = ((CharBuffer)charBuffer.position(0)).toString();
      else
        currentRecord =
            currentRecord.concat(
              ((CharBuffer)charBuffer.position(0)).toString());
    }

    private void readCurrentLine() throws IOException {
      // create a CharBuffer and decode the line from the raw bytes.
      // the line consist of characters between the line buffer position
      // which was created at the start of the line and the current byte
      // buffer position which is currently at the end of the line.

      CharBuffer charBuffer = CharBuffer.allocate(
          byteBuffer.position() - lineBuffer.position());
      decoder.decode(lineBuffer, charBuffer, true);
      if (currentLine == null)
        currentLine = (CharBuffer)charBuffer.position(0);
      else // join buffers together as strings
        currentLine = currentLine.toString().concat(
            ( (CharBuffer) charBuffer.position(0)).toString());

    }
  } // end RegexReader class

}
// $Log$
// Revision 1.1  2003/12/30 16:56:38  mbw
// imported into this product
//
// Revision 1.6  2003/11/04 15:53:14  mbw
// added new functionality for turning on/off use of regular expression matching and for optionally obtaining input through standard in
//
// Revision 1.5  2003/10/27 18:05:21  mbw
// now accepting delimiters of type byte[] to bypass regular expression functionality
//
// Revision 1.4  2003/06/04 15:06:35  mbw
// javadoc edits
//
// Revision 1.3  2003/05/20 15:21:07  mbw
// merged code from branch lib_java_ioutils-1-0-5-jsam
//
// Revision 1.2.2.8  2003/05/16 15:10:39  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.2.2.7  2003/04/29 17:26:02  mbw
// added changes from code reviews
//
// Revision 1.2.2.6  2003/04/15 11:58:10  mbw
// javadoc edits
//
// Revision 1.2.2.5  2003/04/10 17:17:11  mbw
// removed class from import
//
// Revision 1.2.2.4  2003/03/21 16:12:30  mbw
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
