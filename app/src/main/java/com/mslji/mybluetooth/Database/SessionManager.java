package com.mslji.mybluetooth.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;



    // Shared preferences file name
    private static final String PREF_NAME = "Login";



    private static final String KEY_SOCIETY_ID = "s_id";
    private static final String KEY_CHECK = "s_check";
    private static final String KEY_GOTO = "s_gotodata";

    private static final String KEY_ID= "id";




    private static final String KEY_T_NUM = "t_num";
    private static final String KEY_T_ID = "t_id";
    private static final String KEY_T_DATE = "t_date";
    private static final String KEY_T_TAKEN = "t_taken";
    private static final String KEY_D_NAME = "d_name";
    private static final String KEY_D_ADD = "d_name";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDAdd(String dAdd) {

        editor.putString(KEY_D_ADD, dAdd);
        editor.commit();
        editor.apply();
    }


    public String getDAdd() {
        return pref.getString(KEY_D_ADD, "12345");
    }






    public void setDName(String dName) {

        editor.putString(KEY_D_NAME, dName);
        editor.commit();
        editor.apply();
    }


    public String getDName() {
        return pref.getString(KEY_D_NAME, "12345");
    }
    public void setTTaken(String tTaken) {

        editor.putString(KEY_T_TAKEN, tTaken);
        editor.commit();
        editor.apply();
    }


    public String getTTaken() {
        return pref.getString(KEY_T_TAKEN, "12345");
    }
    public void setTDate(String tdate) {

        editor.putString(KEY_T_DATE, tdate);
        editor.commit();
        editor.apply();
    }


    public String getTdate() {
        return pref.getString(KEY_T_DATE, "12345");
    }

    public void setTId(String tid) {

        editor.putString(KEY_T_ID, tid);
        editor.commit();
        editor.apply();
    }


    public String getTId() {
        return pref.getString(KEY_T_ID, "12345");
    }
    public void setTNum(String tNum) {

        editor.putString(KEY_T_NUM, tNum);
        editor.commit();
        editor.apply();
    }


    public String getTNum() {
        return pref.getString(KEY_T_NUM, "12345");
    }

    public void setAppId(String id) {

        editor.putString(KEY_ID, id);
        editor.commit();
        editor.apply();
    }


    public String getAppId() {
        return pref.getString(KEY_ID, "12345");
    }

    public void setGotodata(String id) {

        editor.putString(KEY_GOTO, id);
        editor.commit();


        editor.apply();
    }


    public String getGotodata() {
        return pref.getString(KEY_GOTO, null);
    }

    public void setCheck(Boolean check){
        editor.putBoolean(KEY_CHECK,check);
        editor.commit();
        editor.apply();
    }


    public Boolean getCheck(){
        return pref.getBoolean(KEY_CHECK,false);
    }

    public void setSid(String id) {

        editor.putString(KEY_SOCIETY_ID, id);
        editor.commit();


        editor.apply();
    }


    public String getSid() {
        return pref.getString(KEY_SOCIETY_ID, null);
    }
}
