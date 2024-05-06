package com.example.quizforkids;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizforkids.CartoonQuizContracts.*;
import com.example.quizforkids.QuizDetailsContract.*;

import java.util.ArrayList;
import java.util.List;


public class CartoonQuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CartoonQuiz.db";
    private static final int DATABASE_VERSION = 4;

    private SQLiteDatabase db;

    public CartoonQuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_CARTOON_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_IMAGEID + " INTEGER, " +
                QuestionsTable.COLUMN_CARTOON_ANSWER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        CartoonQuestions q1 = new CartoonQuestions("Who is this character?", "Spongebob Squarepants", "Rapunzel", "Squidward", 1, R.drawable.spongebob);
        addQuestion(q1);
        CartoonQuestions q2 = new CartoonQuestions("Who is this character?", "Snowwhite", "Squidward", "Timmy", 2, R.drawable.squidward);
        addQuestion(q2);
        CartoonQuestions q3 = new CartoonQuestions("Which cartoon are they from?", "Pokemon", "Spongebob", "Fairly Odd Parents", 3, R.drawable.fairlyodd);
        addQuestion(q3);
        CartoonQuestions q4 = new CartoonQuestions("Who is this character?", "Pikachu", "Ash Ketchum", "Eevee", 1, R.drawable.pikachu);
        addQuestion(q4);
        CartoonQuestions q5 = new CartoonQuestions("Who is this princess?", "Snow White", "Cinderella", "Rapunzel", 2, R.drawable.cinderella);
        addQuestion(q5);
        CartoonQuestions q6 = new CartoonQuestions("Who is this princess?", "Snow White", "Cinderella", "Rapunzel", 1, R.drawable.snowwhite);
        addQuestion(q6);
        CartoonQuestions q7 = new CartoonQuestions("Who is this character?", "Squidward", "Sandy Cheeks", "Patrick", 2, R.drawable.sandy);
        addQuestion(q7);
        CartoonQuestions q8 = new CartoonQuestions("Who is this princess?", "Snow White", "Cinderella", "Rapunzel", 3, R.drawable.rapunzel);
        addQuestion(q8);
        CartoonQuestions q9 = new CartoonQuestions("Who is this character?", "Fix-it-Felix", "Pikachu", "Wreck-it-Ralph", 3, R.drawable.ralph);
        addQuestion(q9);
        CartoonQuestions q10 = new CartoonQuestions("Which cartoon are they from?", "Pokemon", "Spongebob Squarepants", "Moana", 1, R.drawable.pokemon);
        addQuestion(q10);

    }

    private void addQuestion(CartoonQuestions question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_CARTOON_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_CARTOON_ANSWER, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_IMAGEID, question.getImageResourceId());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<CartoonQuestions> getAllQuestions() {
        List<CartoonQuestions> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                CartoonQuestions question = new CartoonQuestions();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CARTOON_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CARTOON_ANSWER)));
                question.setImageResourceId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_IMAGEID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public void insertQuizDetails(String username, String quizName, int score) {
        ContentValues cv = new ContentValues();
        cv.put(QuizDetailsTable.COLUMN_USERNAME, username);
        cv.put(QuizDetailsTable.COLUMN_QUIZ_NAME, quizName);
        cv.put(QuizDetailsTable.COLUMN_SCORE, score);
        cv.put(QuizDetailsTable.COLUMN_TIMESTAMP, System.currentTimeMillis());
        db.insert(QuizDetailsTable.TABLE_NAME, null, cv);
    }

}
