package org.jax.mgi.shr.ioutils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataCompare3
{
   private BufferedWriter writer = null;

   public DataCompare3(String name) throws IOException
   {
      writer =
         new BufferedWriter(new FileWriter(name));

   }


   public void createFile() throws IOException
   {
      writer.write("ID          MM.1\n");
      writer.write("-----------------------------------------------\n");
      writer.write("TITLE       S100 CALCIUM BINDING PROTEIN A10 (CALPACTIN)\n");
      writer.write("-----------------------------------------------\n");
      writer.write("||\n");
      writer.write("-----------------------------------------------\n");
      writer.write("ID          MM.5\n");
      writer.write("-----------------------------------------------\n");
      writer.write("TITLE       HOMEO BOX A10\n");
      writer.write("-----------------------------------------------\n");
      writer.close();
   }
}
