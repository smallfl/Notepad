package com.example.myapplication.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private final SQLiteDatabase sqLiteDatabase;


    //创建数据库
    public MyDBOpenHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
        sqLiteDatabase = this.getWritableDatabase();
    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 Note 表
        db.execSQL(DBUtils.CREATE_NOTE_TABLE);
        // 创建 User 表
        db.execSQL(DBUtils.CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 插入 Note 数据
    public boolean insertNoteData(String userContent, String userTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT, userContent);
        contentValues.put(DBUtils.NOTEPAD_TIME, userTime);
        try {
            long result = sqLiteDatabase.insert(DBUtils.DATABASE_TABLE_NOTE, null, contentValues);
            return result != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 删除 Note 数据
    public boolean deleteNoteData(String id) {
        String sql = DBUtils.NOTEPAD_ID + "=?";
        String[] contentValuesArray = new String[]{String.valueOf(id)};
        return sqLiteDatabase.delete(DBUtils.DATABASE_TABLE_NOTE, sql, contentValuesArray) > 0;
    }

    // 修改 Note 数据
    public boolean updateNoteData(String id, String content, String userTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT, content);
        contentValues.put(DBUtils.NOTEPAD_TIME, userTime);
        String sql = DBUtils.NOTEPAD_ID + "=?";
        String[] strings = new String[]{id};
        return sqLiteDatabase.update(DBUtils.DATABASE_TABLE_NOTE, contentValues, sql, strings) > 0;
    }

    // 查询 Note 数据
    public List<Note> queryNotes() {
        List<Note> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(
                DBUtils.DATABASE_TABLE_NOTE,
                null,
                null,
                null,
                null,
                null,
                DBUtils.NOTEPAD_ID + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Note noteInfo = new Note();
                int idIndex = cursor.getColumnIndex(DBUtils.NOTEPAD_ID);
                int contentIndex = cursor.getColumnIndex(DBUtils.NOTEPAD_CONTENT);
                int timeIndex = cursor.getColumnIndex(DBUtils.NOTEPAD_TIME);

                // 检查列索引是否有效
                if (idIndex != -1 && contentIndex != -1 && timeIndex != -1) {
                    String id = String.valueOf(cursor.getInt(idIndex));
                    String content = cursor.getString(contentIndex);
                    String time = cursor.getString(timeIndex);
                    noteInfo.setId(id);
                    noteInfo.setNotepadContent(content);
                    noteInfo.setNotepadTime(time);
                    list.add(noteInfo);
                } else {

                }
            }
            cursor.close();
        }
        return list;
    }


    //用户登录
    public Cursor getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();  // 获取数据库实例
        String[] projection = {
                DBUtils.USER_ID,
                DBUtils.USERNAME,
                DBUtils.PASSWORD,
                DBUtils.SALT
        };
        // 定义选择条件，使用占位符 "?" 防止SQL注入
        String selection = DBUtils.USERNAME + " = ?";
        String[] selectionArgs = {username};

        // 执行查询并返回结果Cursor
        Cursor cursor = db.query(
                DBUtils.DATABASE_TABLE_USER,  // 表名
                projection,                   // 要返回的列
                selection,                    // 列的选择条件
                selectionArgs,                // 选择条件的参数
                null,                  // 不分组
                null,                   // 不过滤组
                null                   // 不排序
        );

        return cursor;
    }


    //// 修改 User 数据
    //public boolean updateUserData(String id, String username, String password, String salt) {
    //    ContentValues contentValues = new ContentValues();
    //    contentValues.put(DBUtils.USERNAME, username);
    //    contentValues.put(DBUtils.PASSWORD, password);
    //    contentValues.put(DBUtils.SALT, salt);
    //    String sql = DBUtils.USER_ID + "=?";
    //    String[] strings = new String[]{id};
    //    return sqLiteDatabase.update(DBUtils.DATABASE_TABLE_USER, contentValues, sql, strings) > 0;
    //}
    //
    //// 查询 User 数据
    //public List<User> queryUsers() {
    //    List<User> list = new ArrayList<>();
    //    Cursor cursor = sqLiteDatabase.query(DBUtils.DATABASE_TABLE_USER, null, null, null,
    //            null, null, DBUtils.USER_ID + " desc");
    //    if (cursor != null) {
    //        while (cursor.moveToNext()) {
    //            User userInfo = new User();
    //            int idIndex = cursor.getColumnIndex(DBUtils.USER_ID);
    //            int usernameIndex = cursor.getColumnIndex(DBUtils.USERNAME);
    //            int passwordIndex = cursor.getColumnIndex(DBUtils.PASSWORD);
    //            int saltIndex = cursor.getColumnIndex(DBUtils.SALT);
    //
    //            if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && saltIndex != -1) {
    //                String id = String.valueOf(cursor.getInt(idIndex));
    //                String username = cursor.getString(usernameIndex);
    //                String password = cursor.getString(passwordIndex);
    //                String salt = cursor.getString(saltIndex);
    //                userInfo.setId(id);
    //                userInfo.setUsername(username);
    //                userInfo.setPassword(password);
    //                userInfo.setSalt(salt);
    //                list.add(userInfo);
    //            }
    //        }
    //        cursor.close();
    //    }
    //    return list;
    //}

    // 注册用户
    public boolean registerUser(String username, String password, String salt) {
        // 检查用户名是否已存在
        if (isUserExist(username)) {
            return false; // 用户名已存在
        }

        // 插入新用户数据
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.USERNAME, username);
        contentValues.put(DBUtils.PASSWORD, password);
        contentValues.put(DBUtils.SALT, salt);
        return sqLiteDatabase.insert(DBUtils.DATABASE_TABLE_USER, null, contentValues) > 0;
    }

    private boolean isUserExist(String username) {
        Cursor cursor = sqLiteDatabase.query(DBUtils.DATABASE_TABLE_USER, null, DBUtils.USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


}
