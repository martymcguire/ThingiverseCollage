package unlekker.util;

import processing.core.*;

/**
 * Utility class for manipulating and generating text.
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 */


public class Str {
	public static StringBuffer buf;
	
  /**
   * Shortens the string "s" to a length of "len" characters.
   */
  public static String shorten(String s,int len) {
    if(s==null) return null;
    if(s.length()>len) s=s.substring(0,len-3)+"...";
    return s;
  }

  /**
   * Shortens the string "s" to a length of 70 characters.
   */
  public static String shorten(String s) {
    if(s==null) return null;
    if(s.length()>70) s=s.substring(0,67)+"...";
    return s;
  }

  /**
   * Pads the number string given by the number "num" with zero 
   * characters if it is shorter than "numlen". If "prefix" is not null, 
   * it will be prefixed to the result. 
   */
  public static String padNumStr(String prefix,int num,int numlen) {
  	if(buf==null) buf=new StringBuffer();
  	buf.setLength(0);
  	buf.append(num);
  	
    for(int i=0; i<numlen-buf.length(); i++) buf.append('0');
    if(prefix!=null) buf.insert(0, prefix);
    return buf.toString();
   }

   public static String shortFilename(String name) {
  	 int pos=name.lastIndexOf(java.io.File.pathSeparatorChar);
  	 if(pos!=-1) return name.substring(pos+1);
  	 else return name;
   }

	 public static String [] wrapTextArray(String s,int len) {
		 String wrapped[],tmp[],ins;
		 int pos,num=0;
		 
		 if(buf==null) buf=new StringBuffer();
		 buf.setLength(0);
	
		 buf.append(s);
		 wrapped=new String[10];
		 while(buf.length()>0) {
			 if(buf.length()>len) {
				 pos=len-1;
				 while(pos>0 && !Character.isWhitespace(buf.charAt(pos))) pos--;
				 if(pos==0) pos=len-1;
				 ins=buf.substring(0,pos);
				 buf.delete(0, pos);
			 }
			 else {
				 ins=buf.toString();
				 buf.setLength(0);
			 }

			 if(wrapped.length==num) {
				 tmp=new String[wrapped.length*2];
				 System.arraycopy(wrapped, 0, tmp, 0, wrapped.length);
				 wrapped=tmp;
			 }			 
		 } 		 
		 
		 tmp=new String[num];
		 System.arraycopy(wrapped, 0, tmp, 0, num);
		 		 
		 return tmp;
	 }
	 
	 /**
	  * Returns a string containing the number "num", shortened to maximum 3
	  * decimal points.
	  */
	public static String numStr(double num) {
		if (buf==null) buf=new StringBuffer();
		buf.setLength(0);
		buf.append(num);

		int pos=buf.indexOf(".");
		if (buf.length()-pos>4)
			buf.setLength(pos+4);

		return buf.toString();
	}

	 public static String wrapText(String s,int len) {
		 int pos,pos2;
		 
		 if(buf==null) buf=new StringBuffer();
		 buf.setLength(0);
	
		 buf.append(s);
		 pos=0;
		 pos2=0;
		 
		 while(buf.length()-pos>len && pos<1000) {
			 pos2=pos+len;
			 while(pos2>pos && !Character.isWhitespace(buf.charAt(pos2))
					 && buf.charAt(pos2)!=';' && buf.charAt(pos2)!=',') {
				 pos2--;
//				 System.out.println("pos2 "+pos2+" pos "+pos+" "+buf.length()+" - "+buf.charAt(pos2));
			 }
			 if(pos2==0) pos2=len-1;
			 else {
				 if(buf.charAt(pos2)==';' || buf.charAt(pos2)==',') pos2++;
			 }
					 
			 buf.setCharAt(pos2, '\n');
			 pos=pos2+1;
		 } 		 
		 		 		 
		 return buf.toString();
	 }

   public static String timeStr(long t) {
  	if(buf==null) buf=new StringBuffer();
  	buf.setLength(0);
  	
    double T=(double)t*0.001;
    if(T>60) {
      if(T>360) {
      	if(T/360<10) buf.append('0');
      	buf.append(Math.floor(T/360)).append(':'); 
      	T/=360;
      }
    	if(T/60<10) buf.append('0');
    	buf.append(Math.floor(T/60)).append(':'); 
    	T/=60;
    }
    buf.append((int)T);
    return buf.toString();
   }

   public static String getDateStr(java.util.Calendar date) {
     return "" + date.get(date.YEAR) + "." +
         padNumStr("", date.get(date.MONTH)+1, 2) +
         padNumStr("", date.get(date.DAY_OF_MONTH), 2);
   }

   public static String getDateStrConcise() {
     return getDateStrConcise(java.util.Calendar.getInstance());
   }

   public static String getDateStrConcise(java.util.Calendar date) {
     int year=date.get(date.YEAR);
     if(year>2000) year-=2000;
     else if(year>1900) year-=1900;
     return "" + padNumStr("", year, 2)+
         padNumStr("", date.get(date.MONTH)+1, 2) +
         padNumStr("", date.get(date.DAY_OF_MONTH), 2);
   }

   public static String getTimeStr() {
     return getTimeStr(java.util.Calendar.getInstance());
   }

   public static String getTimeStr(java.util.Calendar date) {
     return "" + padNumStr("", date.get(date.HOUR_OF_DAY),2) +
         ":"+padNumStr("", date.get(date.MINUTE),2);
   }

   public static String getTimeStrConcise() {
     return getTimeStrConcise(java.util.Calendar.getInstance());
   }

   public static String getTimeStrConcise(java.util.Calendar date) {
     return "" + padNumStr("", date.get(date.HOUR_OF_DAY),2) +
         ""+padNumStr("", date.get(date.MINUTE),2)+
         ""+padNumStr("", date.get(date.SECOND),2);
   }

  
}