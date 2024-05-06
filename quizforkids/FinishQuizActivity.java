package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

public class FinishQuizActivity extends AppCompatActivity {

    private Button retry_button;
    private Button newQuiz_button;

    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private TextView displayUsername;
    private TextView overallScoreMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quiz);

        TextView congratsMessageView = findViewById(R.id.CongratsMessageView);
        retry_button = findViewById(R.id.retry_button);
        newQuiz_button = findViewById(R.id.newQuiz_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menu);
        overallScoreMessage = findViewById(R.id.overallScore_message);

        // Retrieve the stored username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Load quiz details for the user
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        int totalScore = quizDetailsDBHelper.getTotalScoreForUser(username);

        Intent intent = getIntent();
        String quizName = getIntent().getStringExtra("quiz_name");
        int finalScore = intent.getIntExtra("final_score", 0);
        int correctAnswers = intent.getIntExtra("correct_answers", 0);
        int wrongAnswers = intent.getIntExtra("wrong_answers", 0);

        congratsMessageView.setText("Well done " + username +"! You have finished the " + quizName+ " with " + correctAnswers + " correct and " + wrongAnswers + " incorrect answers with " + finalScore + " points for this attempt");
        overallScoreMessage.setText("Overall, you have " + totalScore + " points!");

        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizName.equalsIgnoreCase("Cartoon")) {
                    Intent intent = new Intent(FinishQuizActivity.this, CartoonQuizActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FinishQuizActivity.this, AnimalQuizActivity.class);
                    startActivity(intent);
                }
            }
        });

        newQuiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizName.equalsIgnoreCase("Cartoon")) {
                    Intent intent = new Intent(FinishQuizActivity.this, AnimalQuizActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FinishQuizActivity.this, CartoonQuizActivity.class);
                    startActivity(intent);
                }
            }
        });
        // Set OnClickListener for the menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when the menu button is clicked
                drawerLayout.openDrawer(drawerLayout.getChildAt(1));

                // Retrieve the displayUsername TextView from the drawer layout
                displayUsername = drawerLayout.findViewById(R.id.displayUsername);

                // Set the retrieved username to the displayUsername textView
                displayUsername.setText(username);

                // Set OnClickListener for navigation items
                findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start MainActivity
                        startActivity(new Intent(FinishQuizActivity.this, MainActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(FinishQuizActivity.this, PastAttemptsActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_instructions).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(FinishQuizActivity.this, InstructionsAcivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLogoutMessage(username, totalScore);
                    }
                });
            }
        });
    }

    private void showLogoutMessage(String username, int score) {
        // Display the message
        Toast.makeText(FinishQuizActivity.this, username + ", you have overall " + score + " points.", Toast.LENGTH_SHORT).show();

        // Delay the redirection to the login page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity
                startActivity(new Intent(FinishQuizActivity.this, LoginActivity.class));
                // Close the drawer
                drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                // Finish MainActivity
                finish();
            }
        }, 2000); // Delay in milliseconds (2000 ms = 2 seconds)
    }
}