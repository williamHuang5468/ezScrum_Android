package ntut.mobile.ezscrum.util;

import java.util.Calendar;

public class EzScrumAppUtil {
	
	/***
	 * 取得系統時間
	 */
	public static String getCurrentSystemTime(){
		//	取得地區時間
		long time = System.currentTimeMillis();
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(time);
//		mCalendar.setTimeZone( TimeZone.getTimeZone("Asia/Taipei") );
		int mYear = mCalendar.get(Calendar.YEAR);
		int mMonth = mCalendar.get(Calendar.MONTH) + 1;
		int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		int mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int mMinute = mCalendar.get(Calendar.MINUTE);
		int mSecond = mCalendar.get(Calendar.SECOND);
		String cityTime = mYear + "-" + mMonth + "-" + mDay + " ( "
				+ getDayOfWeek(mWeek) + " ) " + mHour + ":" + mMinute + ":"
				+ mSecond;
		
		return cityTime;
	}
	private static String getDayOfWeek( int index ){
		final int SUNDAY    = 1;
		final int MONDAY    = 2;
		final int TUESDAY   = 3;
		final int WEDNESDAY = 4;
		final int THURSDAY  = 5;
		final int FRIDAY    = 6;
		final int SATURDAY  = 7;
    	String day = "";
    	switch( index ){
	    	case SUNDAY		:	day = "SUNDAY";		break;
	    	case MONDAY		:	day = "MONDAY";		break;
	    	case TUESDAY	:	day = "TUESDAY";	break;
	    	case WEDNESDAY	:	day = "WEDNESDAY";	break;
	    	case THURSDAY	:	day = "THURSDAY";	break;
	    	case FRIDAY		:	day = "FRIDAY";		break;
	    	case SATURDAY	:	day = "SATURDAY";	break;
    	}
    	return day;
    }

}
