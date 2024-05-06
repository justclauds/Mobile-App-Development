package com.example.quizforkids;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizforkids.AnimalQuizContracts.*;
import com.example.quizforkids.QuizDetailsContract.*;

import java.util.ArrayList;
import java.util.List;


public class AnimalQuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AnimalQuiz.db";
    private static final int DATABASE_VERSION = 4;

    private SQLiteDatabase db;

    public AnimalQuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_ANIMAL_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_IMAGEID + " INTEGER, " +
                QuestionsTable.COLUMN_ANIMAL_ANSWER + " TEXT" +
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
        AnimalQuestions q1 = new AnimalQuestions("The ___ is the largest land animal.", "elephant", R.drawable.elephant);
        addQuestion(q1);
        AnimalQuestions q2 = new AnimalQuestions("The ___ is called the king of the jungle.", "lion", R.drawable.lion);
        addQuestion(q2);
        AnimalQuestions q3 = new AnimalQuestions("The ___ produces milk.", "cow", R.drawable.cow);
        addQuestion(q3);
        AnimalQuestions q4 = new AnimalQuestions("The ___ can store lots or nuts in its cheek.", "squirrel", R.drawable.squirrel);
        addQuestion(q4);
        AnimalQuestions q5 = new AnimalQuestions("The ___ is a large orange cat with black stripes.", "tiger", R.drawable.tiger);
        addQuestion(q5);

        AnimalQuestions q6 = new AnimalQuestions("The ___ is white with black stripes.", "zebra", R.drawable.zebra);
        addQuestion(q6);
        AnimalQuestions q7 = new AnimalQuestions("The ___ has a really long neck", "giraffe", R.drawable.giraffe);
        addQuestion(q7);
        AnimalQuestions q8 = new AnimalQuestions("The ___ neighs", "horse", R.drawable.horse);
        addQuestion(q8);
        AnimalQuestions q9 = new AnimalQuestions("The ___ loves eating carrots.", "rabbit", R.drawable.rabbit);
        addQuestion(q9);
        AnimalQuestions q10 = new AnimalQuestions("The ___ is said to be man's best friend", "dog", R.drawable.dog);
        addQuestion(q10);
    }

    private void addQuestion(AnimalQuestions question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_ANIMAL_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_ANIMAL_ANSWER, question.getAnswer());
        cv.put(QuestionsTable.COLUMN_IMAGEID, question.getImageResourceId());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<AnimalQuestions> getAllQuestions() {
        List<AnimalQuestions> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                AnimalQuestions question = new AnimalQuestions();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANIMAL_QUESTION)));
                question.setAnswer(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANIMAL_ANSWER)));
                question.setImageResourceId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_IMAGEID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

}
