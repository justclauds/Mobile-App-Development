package com.example.quizforkids;

import android.provider.BaseColumns;

import java.util.stream.BaseStream;

public class AnimalQuizContracts {

    private AnimalQuizContracts(){

    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "animal_quiz_questions";
        public static final String COLUMN_ANIMAL_QUESTION = "animal_question";
        public static final String COLUMN_IMAGEID = "imageResourceId";
        public static final String COLUMN_ANIMAL_ANSWER = "animal_answer";

    }

}
