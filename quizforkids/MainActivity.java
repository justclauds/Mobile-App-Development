package com.example.quizforkids;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import androidx.drawerlayout.widget.DrawerLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private TextView displayUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menu);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Load quiz details for the user
        QuizDetailsDBHelper quizDetailsDBHelper = new QuizDetailsDBHelper(this);
        int totalScore = quizDetailsDBHelper.getTotalScoreForUser(username);


        findViewById(R.id.animalQuizButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                startActivity(new Intent(MainActivity.this, AnimalQuizActivity.class));
                // Finish MainActivity
                finish();
            }
        });

        findViewById(R.id.cartoonQuizButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                startActivity(new Intent(MainActivity.this, CartoonQuizActivity.class));
                // Finish MainActivity
                finish();
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

                // Retrieve the username from the intent extras
                String username = getIntent().getStringExtra("username");

                // Set the retrieved username to the displayUsername textView
                displayUsername.setText(username);

                // Set OnClickListener for navigation items
                findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start MainActivity
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(MainActivity.this, PastAttemptsActivity.class));
                        // Close the drawer
                        drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                    }
                });

                findViewById(R.id.nav_instructions).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start PastAttemptsActivity
                        startActivity(new Intent(MainActivity.this, InstructionsAcivity.class));
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
    }
    private void showLogoutMessage(String username, int score) {
        // Display the message
        Toast.makeText(MainActivity.this, username + ", you have overall " + score + " points.", Toast.LENGTH_SHORT).show();

        // Delay the redirection to the login page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                // Close the drawer
                drawerLayout.closeDrawer(drawerLayout.getChildAt(1));
                // Finish MainActivity
                finish();
            }
        }, 2000); // Delay in milliseconds (2000 ms = 2 seconds)
    }
}

