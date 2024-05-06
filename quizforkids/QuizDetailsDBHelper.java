package com.example.quizforkids;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizforkids.QuizDetailsContract;

import java.util.ArrayList;
import java.util.List;

public class QuizDetailsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz_details.db";
    private static final int DATABASE_VERSION = 5;

    public QuizDetailsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_QUIZ_DETAILS_TABLE = "CREATE TABLE " + QuizDetailsContract.QuizDetailsTable.TABLE_NAME + " ("
                + QuizDetailsContract.QuizDetailsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME + " TEXT, "
                + QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME + " TEXT, "
                + QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE + " INTEGER, "
                + QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP + " INTEGER);";

        db.execSQL(SQL_CREATE_QUIZ_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_QUIZ_DETAILS_TABLE = "DROP TABLE IF EXISTS " + QuizDetailsContract.QuizDetailsTable.TABLE_NAME;
        db.execSQL(SQL_DELETE_QUIZ_DETAILS_TABLE);
        onCreate(db);
    }

    public List<QuizDetails> getAllQuizDetailsForUser(String username) {
        List<QuizDetails> quizList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selection = QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.rawQuery("SELECT * FROM " + QuizDetailsContract.QuizDetailsTable.TABLE_NAME + " WHERE " + selection, selectionArgs);

        while (cursor.moveToNext()) {
            QuizDetails quizDetails = new QuizDetails();
            quizDetails.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME)));
            quizDetails.setQuizType(cursor.getString(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME)));
            quizDetails.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE)));
            quizDetails.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP)));
            quizList.add(quizDetails);
        }

        cursor.close();
        return quizList;
    }

    public int getTotalScoreForUser(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.rawQuery("SELECT SUM(" + QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE + ") FROM " + QuizDetailsContract.QuizDetailsTable.TABLE_NAME + " WHERE " + selection, selectionArgs);

        int totalScore = 0;
        if (cursor.moveToFirst()) {
            totalScore = cursor.getInt(0);
        }

        cursor.close();
        return totalScore;
    }

    public List<QuizDetails> getQuizDetailsSortedByDate(String username) {
        List<QuizDetails> quizList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selection = QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                QuizDetailsContract.QuizDetailsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP + " DESC" // Order by timestamp in descending order
        );

        while (cursor.moveToNext()) {
            QuizDetails quizDetails = new QuizDetails();
            quizDetails.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME)));
            quizDetails.setQuizType(cursor.getString(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME)));
            quizDetails.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE)));
            quizDetails.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP)));
            quizList.add(quizDetails);
        }

        cursor.close();
        return quizList;
    }

    public List<QuizDetails> getQuizDetailsSortedByQuizType(String username) {
        List<QuizDetails> quizList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selection = QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                QuizDetailsContract.QuizDetailsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME // Order by quiz type
        );

        while (cursor.moveToNext()) {
            QuizDetails quizDetails = new QuizDetails();
            quizDetails.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME)));
            quizDetails.setQuizType(cursor.getString(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME)));
            quizDetails.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE)));
            quizDetails.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP)));
            quizList.add(quizDetails);
        }

        cursor.close();
        return quizList;
    }

}