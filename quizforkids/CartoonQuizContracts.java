package com.example.quizforkids;

import android.provider.BaseColumns;

import java.util.stream.BaseStream;

public class CartoonQuizContracts {

    private CartoonQuizContracts(){

    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "cartoon_quiz_questions";
        public static final String COLUMN_CARTOON_QUESTION = "cartoon_question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";

        public static final String COLUMN_IMAGEID = "imageResourceId";
        public static final String COLUMN_CARTOON_ANSWER = "cartoon_answer";

    }
}
