package unlekker.util;

import java.util.Calendar;

/**
 * Utility class for doing calculations with dates.
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 *
 */
public class DateTool {
 public int y,m,d;
 public int hour,minute;
 static public String dayName[]={"Sunday","Monday","Tuesday",
     "Wednesday","Thursday","Friday","Saturday"};
 static public String monthName[]={"January","February","March",
     "April","May","June","July","August","September","October",
     "November","December"};
 static public String monthNameShort[]={"Jan","Feb","Mar",
     "Apr","May","Jun","Jul","Aug","Sep","Oct",
     "Nov","Dec"};

 public DateTool(String s) {
  y=(int)Integer.parseInt(s.substring(0,4));
  m=(int)Integer.parseInt(s.substring(5,7))-1;
  d=(int)Integer.parseInt(s.substring(7,9));
 }

 public DateTool(DateTool date) {
  y=date.y;
  m=date.m;
  d=date.d;
 }

 public DateTool(int _y,int _m,int _d) {
  y=_y;
  m=_m-1;
  d=_d;
 }

 public DateTool(Calendar cal) {
  y=cal.get(cal.YEAR);
  m=cal.get(cal.MONTH);
  d=cal.get(cal.DATE);
  hour=cal.get(cal.HOUR_OF_DAY);
  minute=cal.get(cal.MINUTE);
 }

 public void addDays(int inc) {
  Calendar cal=Calendar.getInstance();
  setCal(cal);
  cal.add(cal.DATE,inc);
  setCal(cal);
 }

 public void addMonths(int inc) {
  Calendar cal=Calendar.getInstance();
  setCal(cal);
  cal.add(cal.MONTH,inc);
  setCal(cal);
 }


 public void setCal(Calendar cal) {
  y=cal.get(cal.YEAR);
  m=cal.get(cal.MONTH);
  d=cal.get(cal.DATE);
 }

 public void set(DateTool date) {
  y=date.y;
  m=date.m;
  d=date.d;
 }

 /**
  * Set the date from a String with the format "yyyy:mm:dd";
  * @param s
  */
 public void set(String s) {
  y=(int)Integer.parseInt(s.substring(0,4));
  m=(int)Integer.parseInt(s.substring(5,7))-1;
  d=(int)Integer.parseInt(s.substring(7,9));
 }

 public void set(int _y,int _m,int _d) {
  y=_y;
  m=_m-1;
  d=_d;
 }

 public int compare(DateTool date) {
  int val;


  if(y==date.y) {
   if(m==date.m) {
    if(d==date.d) val=0;
    else if(d<date.d) val=1;
    else val=-1;
   }
   else if(m<date.m) val=1;
   else val=-1;
  }
  else if(y<date.y) val=1;
  else val=-1;

//  Tool.outMsg(this.toString()+" compared to "+date+" == "+val);
  return val;
 }

 public static String getDiffMsec(long start) {
   start=System.currentTimeMillis()-start;
   double s=(double)start/1000.0;

   return Str.padNumStr("",(int)(s/3600),2)+":"+
       Str.padNumStr("",(int)(s/3600),2)+
       ":"+Str.padNumStr("",(int)(s),2);
}

 public int getDiff(DateTool date) {
  DateTool start,end;
  int val;

  start=new DateTool(this);
  end=new DateTool(date);

//  Tool.outMsg("DateTool.getDiff -- this "+this+" date "+date);
  if(compare(date)==0) return 0;
  else if(compare(date)<0) { // If date is less that this date, reverse the order
   start.set(date);
   end.set(this);
  }

  val=0;
  while(start.compare(end)!=0) {
   val++;
   start.addDays(1);
//  Tool.outMsg("start "+start+" end "+end+" diff "+val);
  }

  return val;
 }

 static public String calToString(Calendar cal,boolean printTime) {
  String s=dayName[cal.get(cal.DAY_OF_WEEK)-1]+" "+cal.get(cal.DATE)+" "+
      monthNameShort[cal.get(cal.MONTH)]+" "+
      cal.get(cal.YEAR);
  if(printTime) s+=" ("+cal.get(cal.MINUTE)+":"+cal.get(cal.SECOND)+")";
  return s;
 }

 static public void printCal(Calendar cal,boolean printTime) {
  System.out.println(calToString(cal,printTime));
 }

 public String timeStamp() {
  return y+"."+Str.padNumStr("",m+1,2)+""+Str.padNumStr("",d,2)+
   " - "+Str.padNumStr("",hour,2)+":"+Str.padNumStr("",minute,2);
 }

 public String toString() {
  String s=y+"."+Str.padNumStr("",m+1,2)+""+Str.padNumStr("",d,2);
  return s;
 }

 static public String currentTime() {
   Calendar c=Calendar.getInstance();
   return Str.padNumStr("",c.get(c.HOUR_OF_DAY),2)+":"+
       Str.padNumStr("",c.get(c.MINUTE),2)+
       ":"+Str.padNumStr("",c.get(c.SECOND),2);
 }

 static public String dateForFilename() {
   Calendar c=Calendar.getInstance();
//   System.out.println("Month 1: "+c.get(c.MONTH));
   return Str.padNumStr("",c.get(c.YEAR),2).substring(2)+""+
       Str.padNumStr("",c.get(c.MONTH)+1,2)+
       ""+Str.padNumStr("",c.get(c.DAY_OF_MONTH),2);
 }
 static public String dateForFilename(long time) {
   Calendar c=Calendar.getInstance();
   c.setTimeInMillis(time);
//   System.out.println("Month 2: "+c.get(c.MONTH));
   return Str.padNumStr("",c.get(c.YEAR),2).substring(2)+""+
       Str.padNumStr("",c.get(c.MONTH),2)+
       ""+Str.padNumStr("",c.get(c.DAY_OF_MONTH),2);
 }


 static public String timestampToString(java.sql.Timestamp stamp) {
   Calendar c=Calendar.getInstance();
   c.setTime(new java.util.Date(stamp.getTime()));
   return Str.padNumStr("",c.get(c.YEAR),2)+
       Str.padNumStr("",c.get(c.MONTH),2)+
       Str.padNumStr("",c.get(c.DAY_OF_MONTH),2)+" "+
       Str.padNumStr("",c.get(c.HOUR_OF_DAY),2)+":"+
       Str.padNumStr("",c.get(c.MINUTE),2);
 }

 static public long millisSinceMidnight() {
   return millisSinceMidnight(System.currentTimeMillis());
 }

 static public long millisSinceMidnight(long t) {
   Calendar c=Calendar.getInstance();
   c.setTimeInMillis(t);
   long l=c.get(c.HOUR)*60*60*1000;
   l+=c.get(c.MINUTE)*60*1000;
   l+=c.get(c.SECOND)*1000+c.get(c.MILLISECOND);
   return l;
 }
}
