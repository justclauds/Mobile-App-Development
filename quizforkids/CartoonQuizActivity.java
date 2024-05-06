package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class CartoonQuizActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonNext;
    private Button buttonPrevious;

    private ImageView cartoonImageView;

    private ColorStateList textColorDefaultRb;

    private List<CartoonQuestions> questionList;
    private int questionCounter;
    private int questionCountTotal = 4;
    private CartoonQuestions currentQuestion;

    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        cartoonImageView = findViewById(R.id.animalImageView);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonNext = findViewById(R.id.button_next);
        buttonPrevious = findViewById(R.id.button_previous);

        // Retrieve the stored username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");


        textColorDefaultRb = rb1.getTextColors();

        CartoonQuizDBHelper dbHelper = new CartoonQuizDBHelper(this);
        questionList = dbHelper.getAllQuestions();
        Collections.shuffle(questionList);

        showNextQuestion();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(CartoonQuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
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
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            cartoonImageView.setImageResource(currentQuestion.getImageResourceId());

            // Restore the selected option
            switch (currentQuestion.getSelectedOption()) {
                case 1:
                    rb1.setChecked(true);
                    break;
                case 2:
                    rb2.setChecked(true);
                    break;
                case 3:
                    rb3.setChecked(true);
                    break;
            }

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
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
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            cartoonImageView.setImageResource(currentQuestion.getImageResourceId());

            // Restore the selected option
            switch (currentQuestion.getSelectedOption()) {
                case 1:
                    rb1.setChecked(true);
                    break;
                case 2:
                    rb2.setChecked(true);
                    break;
                case 3:
                    rb3.setChecked(true);
                    break;
            }

            textViewQuestionCount.setText("Question: " + (questionCounter + 1) + "/" + questionCountTotal);
            answered = false;
        }
    }

    private void checkAnswer() {
        answered = true;

        // Clear the selected option for the current question
        currentQuestion.setSelectedOption(0);

        // Update the selected option based on the user's choice
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        currentQuestion.setSelectedOption(answerNr);

        // Update the correctAnswers and wrongAnswers fields
        if (answerNr == currentQuestion.getAnswerNr()) {
            currentQuestion.setCorrectAnswers(currentQuestion.getCorrectAnswers() + 1);
        } else {
            currentQuestion.setWrongAnswers(currentQuestion.getWrongAnswers() + 1);
        }
    }

    private void finishQuiz() {
        int finalScore = 0;
        int correctAnswers = 0;
        int wrongAnswers = 0;
        String quizName = "Cartoon";
        for (CartoonQuestions question : questionList) {
            if (question.getAnswerNr() == question.getSelectedOption()) {
                correctAnswers++;
            } else {
                wrongAnswers = 4 - correctAnswers;
            }
        }

        finalScore = correctAnswers * 3 - wrongAnswers;
        // Retrieve the stored username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Insert quiz details into the database
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        SQLiteDatabase db = quizDetailsDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_USERNAME, username);
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_QUIZ_NAME, "Cartoon");
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_SCORE, finalScore);
        values.put(QuizDetailsContract.QuizDetailsTable.COLUMN_TIMESTAMP, System.currentTimeMillis());
        db.insert(QuizDetailsContract.QuizDetailsTable.TABLE_NAME, null, values);
        db.close();

        Intent intent = new Intent(CartoonQuizActivity.this, FinishQuizActivity.class);
        intent.putExtra("quiz_name", quizName);
        intent.putExtra("final_score", finalScore);
        intent.putExtra("correct_answers", correctAnswers);
        intent.putExtra("wrong_answers", wrongAnswers);
        intent.putExtra("username", getIntent().getStringExtra("username")); // Pass the username as an extra
        startActivity(intent);
    }
}