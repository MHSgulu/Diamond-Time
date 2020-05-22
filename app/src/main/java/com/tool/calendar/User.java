package com.tool.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class User {


    private static final String TAG = User.class.getSimpleName();
    private Context mContext;
    private String userToken;
    private String userDeviceType;
    private String mUserNickName;
    private String userAvatarUrl;

    //private String mUserNickName;

    private User(){

    }

    private static class UserHolder{
        private static User instance = new User();
    }


    public static User getInstance() {
        return UserHolder.instance;
    }


    public void initContent(Context context){
        mContext = context.getApplicationContext();
        //Log.d(TAG, "User.mContext: "+ mContext);  TCApplication@7b02767
    }

    //退出时清楚内存中的用户数据
    public void logout() {
        userToken = "";
        userDeviceType = "";
        mUserNickName = "";
        userAvatarUrl = "";
        clearUserInfo();
    }



    //若遭遇闪退、崩溃等现象导致内存中用户数据为空，取出用户数据
    public void loadUserInfo() {
        //TODO: decrypt
        if (mContext == null) return;
        Log.d(TAG, "测试点位: 取数据");
        SharedPreferences settings = mContext.getSharedPreferences("MyUserInfo", Context.MODE_PRIVATE);
        userToken = settings.getString("token", "");
        userDeviceType = settings.getString("deviceType", "");
        mUserNickName = settings.getString("nickName", "");
        userAvatarUrl = settings.getString("avatarUrl", "");
    }

    //登录时调用存放用户数据
    public void saveUserInfo(String token, String deviceType, String name,String avatar) {
        //TODO: encrypt
        if (mContext == null) return;
        Log.d(TAG, "测试点位: 存数据");
        SharedPreferences settings = mContext.getSharedPreferences("MyUserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.putString("deviceType", deviceType);
        editor.putString("nickName", name);
        editor.putString("avatarUrl", avatar);
        editor.apply();
    }

    //退出时清楚轻量存储中的用户数据
    private void clearUserInfo() {
        if (mContext == null) return;
        Log.d(TAG, "测试点位: 清数据");
        SharedPreferences settings = mContext.getSharedPreferences("MyUserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", "");
        editor.putString("deviceType", "");
        editor.putString("nickName", "");
        editor.putString("avatarUrl", "");
        editor.apply();
    }



    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserDeviceType() {
        return userDeviceType;
    }

    public void setUserDeviceType(String userDeviceType) {
        this.userDeviceType = userDeviceType;
    }

    public String getmUserNickName() {
        return mUserNickName;
    }

    public void setmUserNickName(String mUserNickName) {
        this.mUserNickName = mUserNickName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }
}
