package unlekker.util;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import unlekker.util.Str;

/**
 * Various utility functions related to file IO.
 * 
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 *
 */
public class IO {

  public static void mkdir(String name) {
    java.io.File f=new java.io.File(name);
    f.mkdir();
  }

  public static boolean deleteFile(String name) {
    return (new java.io.File(name).delete());
  }

  public static float getFileSize(String name) {
    java.io.File f=new java.io.File(name);
    if(f.exists()) return (float)f.length()/1024f;

    return 0;
  }

  public static String getFileSizeString(String name) {
    float kb=getFileSize(name);
    if(kb<1000) {
      return Str.numStr(kb)+" kb";
    }
    return Str.numStr(kb/1024f)+" MB";
  }


  // INCOMPLETE
  public static String getIncrementalFolder(String what,int lastnum) {
    String s="",prefix,suffix,padstr,numstr;
    int index,first,last,count;
    File f,parent;
    boolean ok;

    first=what.indexOf('#');
    last=what.lastIndexOf('#');
    count=last-first+1;

    if( (first!=-1)&& (last-first>0)) {
      prefix=what.substring(0, first);
      suffix=what.substring(last+1);

      index=lastnum;
      ok=false;

      do {
        padstr="";
        numstr=""+index;
        for(int i=0; i<count-numstr.length(); i++) padstr+="0";
        s=prefix+padstr+numstr+suffix;

        f=new File(s);
        parent=f.getParentFile();
        if(parent!=null) parent.mkdirs();
//       System.out.println(parent.toString());

        ok=!f.exists();
        index++;

        if(index%1000==0) System.out.println("index "+index+" '"+s+"'");
      } while(!ok);
    }
    f=new File(s);
    f.mkdirs();

    return s;
  }

  public static String getIncrementalFolder(String what) {
    return getIncrementalFolder(what,0);
  }

  public static String getIncrementalFilename(String what,int lastnum) {
    String s="",prefix,suffix,padstr,numstr;
    int index,first,last,count;
    File f,parent;
    boolean ok;

    first=what.indexOf('#');
    last=what.lastIndexOf('#');
    count=last-first+1;

    if( (first!=-1)&& (last-first>0)) {
      prefix=what.substring(0, first);
      suffix=what.substring(last+1);

      index=lastnum;
      ok=false;

      do {
        padstr="";
        numstr=""+index;
        for(int i=0; i<count-numstr.length(); i++) padstr+="0";
        s=prefix+padstr+numstr+suffix;

        f=new File(s);
        parent=f.getParentFile();
        if(parent!=null) parent.mkdirs();

        ok=!f.exists();
        index++;

//       if(index%1000==0) System.out.println("index "+index+" '"+s+"'");
      } while(!ok);
    }

    return s;
  }

  public static String getIncrementalFilename(String what) {
    return getIncrementalFilename(what, 0);
  }

  public void saveStringsGZIP(String filename, String strings[]) {
    try {
      String location=filename+".gz";
      GZIPOutputStream fos=
          new GZIPOutputStream(new FileOutputStream(location));
      PrintWriter writer=
          new PrintWriter(new OutputStreamWriter(fos));
      for (int i=0; i<strings.length; i++)
        if (strings[i]!=null)
          writer.println(strings[i]);
      writer.flush();
      fos.close();
    }
    catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("saveStringsGZIP() failed: "
                                 +e.getMessage());
    }
  }

}
