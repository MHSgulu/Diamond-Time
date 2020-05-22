package com.tool.calendar.SQLite;


import android.provider.BaseColumns;


public final class FeedReaderContract {
    //为防止某人意外实例化Contract类，//将构造函数设为私有
    private FeedReaderContract() {}

    /*  定义表内容的内部类 */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
    }



    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_START_DATE + " TEXT," +
                    FeedEntry.COLUMN_NAME_END_DATE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;








}




