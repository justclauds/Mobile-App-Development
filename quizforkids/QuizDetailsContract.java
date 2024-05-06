package com.example.quizforkids;

import android.provider.BaseColumns;

public class QuizDetailsContract {

    private QuizDetailsContract() {
    }

    public static class QuizDetailsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_details";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_QUIZ_NAME = "quiz_name";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
