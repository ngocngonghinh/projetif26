package utt.fr.if26.project.model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utt.fr.if26.project.support.ExceptionHandler;

/**
 * DateTime is an object which allows convert date/time, if it is today show only time. If is is in this year, show date and month If not, show complete date time
 * 
 * @author NNN
 * 
 */
public class DateTime {
	public static final String DATE_FORMAT = "dd MMM yyyy";
	public static final String YEAR = "yyyy";
	public static final String DATE_NO_YEAR = "dd MMM";
	public static final String TIME = "HH:mm";
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	@SuppressLint("SimpleDateFormat")
	public String convertDate(String dateTime) {
		SimpleDateFormat dateTimeformat = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat year = new SimpleDateFormat(YEAR);
		SimpleDateFormat date = new SimpleDateFormat(DATE_NO_YEAR);
		SimpleDateFormat time = new SimpleDateFormat(TIME);
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME);

		try {
			Date d = format.parse(dateTime);
			Date today = Calendar.getInstance().getTime();

			if (dateTimeformat.format(today).equals(dateTimeformat.format(d))) {
				return time.format(d);
			} else if (year.format(today).equals(year.format(d))) {
				return date.format(d);
			} else {
				return dateTimeformat.format(d);
			}
		} catch (ParseException e) {
			ExceptionHandler.getInstance().mHandler(e);
			return "";
		}
	}
}
