package utt.fr.if26.project.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;
import android.text.TextUtils;

public class InputValidation {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean isEmail(final String hex) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
	}

	/**
	 * Escape special chars in string to prevent injection attack
	 * 
	 * @param dirty
	 * @return
	 */
	public static String cleanString(String dirty) {
		return TextUtils.htmlEncode(dirty);

	}

	/**
	 * Retrieve original string
	 * 
	 * @param str
	 * @return
	 */
	public static String reverseString(String str) {
		return Html.fromHtml(str).toString();
	}
}
