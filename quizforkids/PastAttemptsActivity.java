package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.os.Handler;

public class PastAttemptsActivity extends AppCompatActivity {

    private TextView userGreeting;
    private TextView noAttempt;

    private TextView userScore;
    private TableLayout quizDetailsTable;

    private ImageButton menuButton;
    private DrawerLayout drawerLayout;
    private TextView displayUsername;
    private Switch sortBySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_attempts);

        userGreeting = findViewById(R.id.user_greeting_text);
        noAttempt = findViewById(R.id.no_attempt_text);
        quizDetailsTable = findViewById(R.id.quiz_details_table);
        userScore = findViewById(R.id.total_user_score);
        sortBySwitch = findViewById(R.id.sortSwitchButton);
        menuButton = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Set default text to "Most Recent"
        sortBySwitch.setText("Most Recent");

        // Listen for switch state changes
        sortBySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If switch is checked, set text to "Quiz Type"
                    sortBySwitch.setText("Quiz Type");
                    // Perform sorting by quiz type
                    sortByQuizType();
                } else {
                    // If switch is unchecked, set text to "Most Recent"
                    sortBySwitch.setText("Most Recent");
                    // Perform sorting by date
                    sortByDate();
                }
            }
        });

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Load quiz details for the user
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        List<QuizDetails> quizDetailsList = quizDetailsDBHelper.getAllQuizDetailsForUser(username);
        int totalScore = quizDetailsDBHelper.getTotalScoreForUser(username);

        // Set OnClickListener for the menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when the menu button is clicked
                drawerLayout.openDrawer(drawerLayout.getChildAt(1));

                // Retrieve the displayUsername TextView from the drawer layout
                displayUsername = drawerLayout.findViewById(R.id.displayUsername);

                // Retrieve the username from the intent extras
                String username = getIntent().getStringExtra("username");

                // Set the retrieved username to the displayUsername textView
                displayUsername.setText(username);

                // Set OnClickListener for navigation items
                findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start MainActivity
                        startActivity(new Intent(PastAttemptsActivity.this, MainActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(PastAttemptsActivity.this, PastAttemptsActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_instructions).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(PastAttemptsActivity.this, InstructionsAcivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start LoginActivity
                        showLogoutMessage(username, totalScore);
                    }
                });
            }
        });


        if (!quizDetailsList.isEmpty()) {
            QuizDetails quizDetails = quizDetailsList.get(0);
            String greeting = "Hi " + username + "!";
            String scoreText = "You have a total of " + totalScore + " points!";

            userGreeting.setText(greeting);
            userScore.setText(scoreText);
            noAttempt.setText(" ");

            sortByDate();

            // Set the text color of the TextView to white
            userGreeting.setTextColor(getResources().getColor(R.color.white));
            this.noAttempt.setTextColor(getResources().getColor(R.color.white));

        } else {
            // No quiz details found for the user
            userGreeting.setText("Hi " + username + "!");
            userScore.setText("You have 0 points. Attempt quizzes to get some!");
            noAttempt.setText("No past attempts found.");
        }
    }

    private void sortByQuizType() {
        // Clear the existing rows in the table
        quizDetailsTable.removeAllViews();

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Load quiz details for the user sorted by quiz type
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        List<QuizDetails> quizDetailsList = quizDetailsDBHelper.getQuizDetailsSortedByQuizType(username);

        if (!quizDetailsList.isEmpty()) {
            // Iterate through the sorted quiz details list
            for (QuizDetails quiz : quizDetailsList) {
                // Create a new row for each quiz detail
                TableRow row = new TableRow(this);

                // Create TextViews for quiz type, date, and score
                TextView quizTypeTextView = new TextView(this);
                quizTypeTextView.setText(quiz.getQuizType());
                quizTypeTextView.setPadding(16, 16, 16, 16);
                quizTypeTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(quizTypeTextView);

                TextView dateTextView = new TextView(this);
                dateTextView.setText(getFormattedDateTime(quiz.getTimestamp()));
                dateTextView.setPadding(16, 16, 16, 16);
                dateTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(dateTextView);

                TextView scoreTextView = new TextView(this);
                scoreTextView.setText("Points earned: " + quiz.getScore());
                scoreTextView.setPadding(16, 16, 16, 16);
                scoreTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(scoreTextView);

                // Add the row to the table layout
                quizDetailsTable.addView(row);
            }
        } else {
            noAttempt.setText("No past attempts found.");
        }
    }

    private void sortByDate() {
        // Clear the existing rows in the table
        quizDetailsTable.removeAllViews();

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Load quiz details for the user sorted by date
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        List<QuizDetails> quizDetailsList = quizDetailsDBHelper.getQuizDetailsSortedByDate(username);

        if (!quizDetailsList.isEmpty()) {
            // Iterate through the sorted quiz details list
            for (QuizDetails quiz : quizDetailsList) {
                // Create a new row for each quiz detail
                TableRow row = new TableRow(this);

                // Create TextViews for quiz type, date, and score
                TextView quizTypeTextView = new TextView(this);
                quizTypeTextView.setText(quiz.getQuizType());
                quizTypeTextView.setPadding(16, 16, 16, 16);
                quizTypeTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(quizTypeTextView);

                TextView dateTextView = new TextView(this);
                dateTextView.setText(getFormattedDateTime(quiz.getTimestamp()));
                dateTextView.setPadding(16, 16, 16, 16);
                dateTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(dateTextView);

                TextView scoreTextView = new TextView(this);
                scoreTextView.setText("Points earned: " + quiz.getScore());
                scoreTextView.setPadding(16, 16, 16, 16);
                scoreTextView.setTextColor(getResources().getColor(R.color.white));
                row.addView(scoreTextView);

                // Add the row to the table layout
                quizDetailsTable.addView(row);
            }
        } else {
            noAttempt.setText("No past attempts found.");
        }
    }

    private String getFormattedDateTime(long timestamp) {
        Date date = new Date(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void showLogoutMessage(String username, int score) {
        // Display the message
        Toast.makeText(PastAttemptsActivity.this, username + ", you have overall " + score + " points.", Toast.LENGTH_SHORT).show();

        // Delay the redirection to the login page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity
                startActivity(new Intent(PastAttemptsActivity.this, LoginActivity.class));
                // Close the drawer
                drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                // Finish MainActivity
                finish();
            }
        }, 2000);
    }
}