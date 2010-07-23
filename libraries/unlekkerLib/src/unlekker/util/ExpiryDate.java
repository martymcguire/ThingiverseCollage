package unlekker.util;

import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Provides an easy way to build an expiry date into an application. Useful when you send work out to exhibitions
 * and you don't want people to keep "live" copies.
 * @author <a href="http://workshop.evolutionzone.com/">Marius Watz</a>
 *
 */
public class ExpiryDate {
	Calendar expirydate,now;
	public boolean expired;
	
	public ExpiryDate(int year,int month,int day) {
		expirydate=Calendar.getInstance();
		expirydate.set(year, month-1, day);
		
		
		
		now=Calendar.getInstance();
		
/*		
 		String myString = SimpleDateFormat.getDateInstance().format(expirydate.getTime());
		System.out.println("Exp: "+myString);
		myString = SimpleDateFormat.getDateInstance().format(now.getTime());
		System.out.println("Now: "+myString);
*/
		
		if(now.getTime().compareTo(expirydate.getTime())>0) {
			expired=true;
			System.err.println("Application expired:");
		}
	} 
}
