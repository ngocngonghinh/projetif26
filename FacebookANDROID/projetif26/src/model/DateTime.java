package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * DateTime is an object which allows convert date/time, if it is today show
 * only time. If is is in this year, show date and month If not, show complete
 * date time
 * 
 * @author NNN
 *
 */
public class DateTime {
	@SuppressLint("SimpleDateFormat")
	public String convertDate(String dateTime) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date d = format.parse(dateTime);
			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat dateTimeformat = new SimpleDateFormat(
					"dd MMM yyyy");
			SimpleDateFormat year = new SimpleDateFormat("yyyy");
			SimpleDateFormat date = new SimpleDateFormat("dd MMM");
			SimpleDateFormat time = new SimpleDateFormat("HH:mm");
			if (dateTimeformat.format(today).equals(dateTimeformat.format(d)))
				return time.format(d);
			else if (year.format(today).equals(year.format(d)))
				return date.format(d);
			else
				return dateTimeformat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
