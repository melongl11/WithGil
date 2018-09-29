package com.example.melon.withgil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MapInfoDatabase extends SQLiteOpenHelper {
    public MapInfoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE REGIONINFO(idx integer PRIMARY KEY,district text ,region text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addRegion(int idx, String district, String region) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO REGIONINFO(idx, district, region) VALUES (" + idx + ", '" + district + "', '" + region + "')";
        db.execSQL(sql);
        //db.execSQL(sql, new Object[]{userInfo.getUid(), userInfo.getPhone_number(), userInfo.getName(), userInfo.getSex(), userInfo.getBirthday(), userInfo.getEmail(), userInfo.getNation()});
        //Log.d("userInfoDatabase", userInfo.getUid());
    }

    public int getRegionInfo(String district, String region) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM REGIONINFO WHERE district = '" + district + "' AND region = '" + region + "'";

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        cursor.close();

        return 0;
    }

}

