package org.jax.mgi.shr.ioutils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataInput3
{
   private BufferedWriter writer = null;

   public DataInput3(String name) throws IOException
   {
      writer =
         new BufferedWriter(new FileWriter(name));

   }


   public void createFile() throws IOException
   {
      writer.write("ID          Mm.1\n");
      writer.write("TITLE       S100 calcium binding protein A10 (calpactin)\n");
      writer.write("||\n");
      writer.write("ID          Mm.5\n");
      writer.write("TITLE       Homeo Box A10\n");
      writer.close();
   }
}
