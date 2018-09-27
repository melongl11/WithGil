package com.example.melon.withgil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by melon on 2018-09-27.
 */

public class UserInfoDatabase extends SQLiteOpenHelper {
    public UserInfoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USERINFO( uId text PRIMARY KEY, phone_number text, name text, sex text, birthday text, email text, nation text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addUser(UserInfo userInfo) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO USERINFO(uid, phone_number, name, sex, birthday, email, nation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        //db.execSQL(sql, new Object[]{userInfo.getUid(), userInfo.getPhone_number(), userInfo.getName(), userInfo.getSex(), userInfo.getBirthday(), userInfo.getEmail(), userInfo.getNation()});
        //Log.d("userInfoDatabase", userInfo.getUid());
    }

    public UserInfo getUserInfo(String uid) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM USERINFO WHERE uId = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{uid});

        UserInfo userInfo = new UserInfo();
        if(cursor.moveToFirst()) {
            //Log.d("userInfoDatabase2", userInfo.getUid());
        }
        cursor.close();

        return userInfo;
    }

    public void updateUser(String uid, String attribute,  String updateValue) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "";
        switch (attribute){
            case "name":
                sql = "UPDATE USERINFO SET name = '" + updateValue + "' WHERE uId = '" + uid +"'";
            case "email":
                sql = "UPDATE USERINFO SET email = '" + updateValue + "' WHERE uId = '" + uid +"'";
        }

        db.execSQL(sql);
    }
}
