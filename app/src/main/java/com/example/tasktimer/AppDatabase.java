package com.example.tasktimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.core.content.ContextCompat;
/**
basic database class for the application he only class tht should use this is {@link AppProvider}.
*/
class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "TaskTimer.db";
    public static final int DATABSE_VERSION = 1;

    //Implementing AppDatabase as singlton

    private static AppDatabase instance = null;

    private AppDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABSE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     * Get an instance of the app's singleton databse helper object
     * @param context the content providers context
     * @return a SQLITE databse helper object
     */
    static AppDatabase getInstance(Context context){
        if (instance == null){
            Log.d(TAG, "getINstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        String sSQL;
        // sSQL = "CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL, Name TEXT NOT NULL, Description TEXT, SortOrder INTEGER, CategoryID INTEGER);";
        sSQL = "CREATE TABLE " + TasksContract.TABLE_NAME + "("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL , "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORTORDER + " INTEGER);";
        Log.d(TAG,sSQL);
        db.execSQL(sSQL);
        Log.d(TAG, "onCreate:  ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch(oldVersion) {
            case 1:
                //u[grade logic fro version
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);

        }
        Log.d(TAG, "onUpgrade: ends");
    }
}
