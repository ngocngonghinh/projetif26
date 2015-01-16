package utt.fr.if26.project.support;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
	/**
	 * Save String data as a pair of key and value
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStringData(Context context, String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Get String data with a key
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getStringData(Context context, String key) {
		return context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).getString(key, "");
	}

	/**
	 * Remove String data
	 * 
	 * @param context
	 * @param keys
	 */
	public static void removeStringData(Context context, String... keys) {
		SharedPreferences.Editor editor = context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).edit();
		for (String key : keys) {
			editor.remove(key);
			editor.commit();
		}
	}

	/**
	 * Update String data with a key
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void updateStringData(Context context, String key, String value) {
		removeStringData(context, key);
		saveStringData(context, key, value);
	}

	/**
	 * Save Boolean data as a pair of key and value
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBooleanData(Context context, String key, boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * Get Boolean data with a key
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBooleanData(Context context, String key) {
		return context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
	}

	/**
	 * Save integer data as a pair of key and value
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveIntData(Context context, String key, int value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * Get interger data with a key
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getIntData(Context context, String key) {
		return context.getSharedPreferences(Keys.FILE_NAME, Context.MODE_PRIVATE).getInt(key, -1);
	}
}
