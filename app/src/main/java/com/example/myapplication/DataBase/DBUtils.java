package com.example.myapplication.DataBase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {
    public static final String DATABASE_NAME = "Notepad"; // 数据库名
    public static final String DATABASE_TABLE_NOTE = "Note"; // Note表名
    public static final String DATABASE_TABLE_USER = "User"; // User表名
    public static final int DATABASE_VERSION = 1; // 数据库版本

    // Note表中的列名
    public static final String NOTEPAD_ID = "id";
    public static final String NOTEPAD_CONTENT = "content";
    public static final String NOTEPAD_TIME = "notetime";

    // User表中的列名
    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SALT = "salt";

    // 获取当前日期
    public static final String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    // SQL语句创建Note表
    public static final String CREATE_NOTE_TABLE = "CREATE TABLE " + DATABASE_TABLE_NOTE + " ("
            + NOTEPAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOTEPAD_CONTENT + " TEXT NOT NULL, "
            + NOTEPAD_TIME + " TEXT NOT NULL);";

    // SQL语句创建User表
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + DATABASE_TABLE_USER + " ("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT NOT NULL, "
            + PASSWORD + " TEXT NOT NULL, "
            + SALT + " TEXT NOT NULL);";

}
