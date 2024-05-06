package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.List;

public class AnimalQuizActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;

    private Button buttonNext;
    private Button buttonPrevious;

    private EditText userAnswer;

    private ImageView animalImageView;

    private List<AnimalQuestions> questionList;
    private int questionCounter;
    private int questionCountTotal = 4;
    private AnimalQuestions currentQuestion;

    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        animalImageView = findViewById(R.id.animalImageView);
        buttonNext = findViewById(R.id.button_next);
        buttonPrevious = findViewById(R.id.button_previous);
        userAnswer = findViewById(R.id.answer_input);

        // Retrieve the stored username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        AnimalQuizDBHelper dbHelper = new AnimalQuizDBHelper(this);
        questionList = dbHelper.getAllQuestions();
        Collections.shuffle(questionList);

        showNextQuestion();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store user's answer
                currentQuestion.setUserAnswer(userAnswer.getText().toString());
                showNextQuestion();
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousQuestion();
            }
        });
    }

    private void showNextQuestion() {
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            animalImageView.setImageResource(currentQuestion.getImageResourceId());
            // store user's answer
            currentQuestion.setUserAnswer(userAnswer.getText().toString());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            userAnswer.setText("");
            answered = false;

        } else {
            finishQuiz();
        }
    }

    private void showPreviousQuestion() {
        if (questionCounter > 0) {
            currentQuestion = questionList.get(questionCounter - 1);

            questionCounter--;

            textViewQuestion.setText(currentQuestion.getQuestion());
            animalImageView.setImageResource(currentQuestion.getImageResourceId());

            textViewQuestionCount.setText("Question: " + (questionCounter + 1) + "/" + questionCountTotal);
            userAnswer.setText("");
            answered = false;
        }
    }

    private void finishQuiz() {
        int finalScore = 0;
        int correctAnswers = 0;
        int wrongAnswers = 0;

        String quizName = "Animal"; // Set the quiz name here

        // Retrieve the stored username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        for (AnimalQuestions question : questionList) {
            if (question.getAnswer().equalsIgnoreCase(question.getUserAnswer())) {
                correctAnswers++;
            } else {
                wrongAnswers = 4 - correctAnswers;
            }
        }

        finalScore = correctAnswers * 3 - wrongAnswers;

        // Insert quiz details into the database
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        SQLiteDatabase db = quizDetailsDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME, username);
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME, quizName);
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE, finalScore);
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP, System.currentTimeMillis());
        db.insert(QuizDetailsContract.QuizDetailsTable.TABLE_NAME, null, values);
        db.close();

        Intent intent = new Intent(AnimalQuizActivity.this, FinishQuizActivity.class);
        intent.putExtra("quiz_name", quizName);
        intent.putExtra("final_score", finalScore);
        intent.putExtra("correct_answers", correctAnswers);
        intent.putExtra("wrong_answers", wrongAnswers);
        intent.putExtra("username", getIntent().getStringExtra("username")); // Pass the username as an extra
        startActivity(intent);
    }
}