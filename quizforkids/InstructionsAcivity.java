package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

public class InstructionsAcivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private TextView displayUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_acivity);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menu);

        // Retrieve the stored username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Load quiz details for the user
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        int totalScore = quizDetailsDBHelper.getTotalScoreForUser(username);

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
                        startActivity(new Intent(InstructionsAcivity.this, MainActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(InstructionsAcivity.this, PastAttemptsActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_instructions).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(InstructionsAcivity.this, InstructionsAcivity.class));
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
        Toast.makeText(InstructionsAcivity.this, username + ", you have overall " + score + " points.", Toast.LENGTH_SHORT).show();

        // Delay the redirection to the login page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity
                startActivity(new Intent(InstructionsAcivity.this, LoginActivity.class));
                // Close the drawer
                drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                // Finish MainActivity
                finish();
            }
        }, 2000); // Delay in milliseconds (2000 ms = 2 seconds)
    }
}