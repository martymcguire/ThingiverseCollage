package unlekker.util;

import java.lang.*;
import java.lang.reflect.*;

/**
 * Set of static utility functions. 
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 */


public class Util {
	private final static String NULLSTR="NULL",EQUALSTR=" = ",COMMASTR=", ";
	private final static Class [] types={
		Float.class, Integer.class, Double.class, 
		Long.class, String.class, Boolean.class};
	
	
  /**
	 * Returns a String representation of object "o", containing the values 
	 * of all its fields. 
	 */

	public static String listObjectFields(Object o) {
		Object otmp;
		Class ctmp;
		
		try {
			Class cl=o.getClass();
			Field[] f=cl.getFields();

			final StringBuffer buf=new StringBuffer(500);
			Object value=null;
			buf.append(cl.getSimpleName());
			buf.append('@');
			buf.append(o.hashCode());
			buf.append(" = {");

			for (int idx=0; idx<f.length; idx++) {

				if (idx!=0)
					buf.append(COMMASTR);

				buf.append(f[idx].getName());
				buf.append(EQUALSTR);
				
				ctmp=null;
				otmp=f[idx].get(o);
				if(otmp!=null) {
					ctmp=otmp.getClass();
					if(ctmp.equals(types[0])) buf.append(Str.numStr(f[idx].getFloat(o)));
					else if(ctmp.equals(types[1])) buf.append(f[idx].getInt(o));
					else if(ctmp.equals(types[2])) buf.append(Str.numStr(f[idx].getDouble(o)));
					else if(ctmp.equals(types[3])) buf.append(f[idx].getLong(o));
					else if(ctmp.equals(types[4])) {
						buf.append('"');
						buf.append((String)otmp);
						buf.append('"');
					}
					else if(ctmp.equals(types[5])) buf.append(f[idx].getBoolean(o));
					else buf.append(ctmp.getName());
				}
				else buf.append(NULLSTR);
			}

			buf.append("}");

			return Str.wrapText(buf.toString(),70);

		} catch (Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
			//throw new RuntimeException(ex);
		}
		
		return null;
	}
}
