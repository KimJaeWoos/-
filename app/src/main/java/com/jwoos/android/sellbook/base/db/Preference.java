package com.jwoos.android.sellbook.base.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.jwoos.android.sellbook.base.BaseApplication;

import java.util.HashSet;
import java.util.Set;

/**
 // 선언
 Preference pref = new Preference(this);

 // set
 pref.put(Preference.PREF_USER_AGREEMENT, true);

 // get
 pref.getValue(Preference.PREF_USER_AGREEMENT, false);

 */
public class Preference {

    static Context mContext;

    public static final String PREF_COOKIES = "PREF_COOKIES";
    public static final String PREF_NAME = "COOKIES";

    public Preference(Context c) {
        mContext = c;
    }

    public void setCookie(HashSet<String> cookie) {
        SharedPreferences ss = BaseApplication.getInstance().getSharedPreferences(PREF_COOKIES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ss.edit();
        editor.putStringSet(PREF_NAME,cookie);
        editor.commit();
    }

    public Set<String> getCookie(){
        SharedPreferences ss = BaseApplication.getInstance().getSharedPreferences(PREF_COOKIES,Context.MODE_PRIVATE);
        return ss.getStringSet(PREF_NAME,new HashSet<String>());
    }

    public void resetCookie(){
        SharedPreferences ss = BaseApplication.getInstance().getSharedPreferences(PREF_COOKIES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ss.edit();
        editor.remove(PREF_NAME);
        //editor.putStringSet(PREF_NAME,new HashSet<String>());
        editor.commit();
    }

}
