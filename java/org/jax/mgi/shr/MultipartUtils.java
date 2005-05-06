package org.jax.mgi.shr;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MultipartUtils {

	protected static String readData(InputStream is) throws IOException{
		String oneLine;
        StringBuffer postedData = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //READ IN ALL THE DATA
        oneLine = br.readLine();
        while(oneLine!=null) {
            postedData.append(oneLine+"\n");
            oneLine = br.readLine();
        }
        return postedData.toString();
	}

	protected static String findBoundaryText(Enumeration contentType) {
		StringBuffer boundary = new StringBuffer();

      //FIND BOUNDARY
        while(contentType.hasMoreElements()) {
            boundary.append((String)contentType.nextElement());
        }
        // check to see if this is actually a multipart or just an empty submission
        if(boundary.indexOf("multipart/form-data")==-1) {
            return null;
        } else return boundary.substring(boundary.indexOf("=")+1)+"\n";
	}

	protected static String getFieldName(String firstLine) {
		int start, end;
		//FIND THE NAME OF THIS FIELD
		if(firstLine.startsWith("Content-Disposition")) {
			start = firstLine.indexOf("name=\"")+6;
			end = firstLine.indexOf("\"",start);
			return firstLine.substring(start,end);
		} else return null;

	}

	protected static int findStartLocation(String secondLine) {
		// See if it's a file...
		if(secondLine.startsWith("Content-Type")) {
			return 2;
		} else {
			return 1;
		}
	}

    public static Map parseMultiPart(HttpServletRequest sr) throws IOException{
        HashMap parms = new HashMap();
        String[] tmp;
        StringBuffer sb;
        String postedData;
        String field,fieldName;
        String boundary;
        String[] fields, lines, args;
        int start, end;


		postedData = readData(sr.getInputStream());
		boundary = findBoundaryText(sr.getHeaders("content-type"));

		if((postedData.length()==0) || (boundary == null))
			return parms;

        //SEPERATE FIELDS BY THE BOUNDARY
        fields = postedData.split(boundary);

        for(int i=0;i<fields.length;i++) {
            field = fields[i];
            sb = new StringBuffer();

            //Break entry up into lines
            lines = field.split("\n");
			//Make sure there is actual content
            if(lines.length<3) {
                continue;
            }

			fieldName = getFieldName(lines[0]);
			start = findStartLocation(lines[1]);

            //RUN THROUGH ALL THE LINES THAT HAVE DATA IN THEM
            //FIRST LINE AFTER START WILL BE BLANK, LAST LINE WILL BE JUST --
            args = new String[(lines.length-1)-(start+1)];
            for(int j=0;j<args.length;j++) {
                args[j] = lines[j+start+1];
            }
            if(parms.containsKey(fieldName)) {
                args = (String[])addArrays(args,(String[])parms.get(fieldName));
            }
            parms.put(fieldName,args);
        }
        return parms;

    }

    public static String[] addArrays(String[] a, String[] b) {

        String[] results = new String[a.length+b.length];

        for(int i=0;i<results.length;i++)
            if(i<a.length)
                results[i] = a[i];
            else results[i] = b[i-a.length];

        return results;
    }
}