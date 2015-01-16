package utt.fr.if26.project.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefsHelper {
	public final static String PREFS = "PREFS";
	public final static String IS_LOGGED = "IS_LOGGED";
	public final static String TOKEN = "TOKEN";
	
	public static void setIsLogged(Context context, boolean value)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putBoolean(IS_LOGGED, value);
        edit.commit();
    }

    public static boolean getIsLogged(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(IS_LOGGED, false);
    }
    
    public static void setString(Context context, String value)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putString(TOKEN, value);
        edit.commit();
    }

    public static String getToken(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN, "");
    }
}
