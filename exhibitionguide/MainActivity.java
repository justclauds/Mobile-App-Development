package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons
        ImageButton spaceShowButton = findViewById(R.id.spaceShowButton);
        ImageButton artGalleryButton = findViewById(R.id.artGalleryButton);
        ImageButton worldWarButton = findViewById(R.id.worldWarButton);
        ImageButton visualShowButton = findViewById(R.id.visualShowButton);

        // Set click listeners for each button using lambda expressions
        spaceShowButton.setOnClickListener(v -> openBookingActivity("Exploring the Space"));
        artGalleryButton.setOnClickListener(v -> openBookingActivity("Art Gallery"));
        worldWarButton.setOnClickListener(v -> openBookingActivity("WWI Exhibition"));
        visualShowButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, bookingActivity.class);
            intent.putExtra("EXHIBITION_NAME", "Visual Show");
            intent.putExtra("TIME_ARRAY", R.array.visual_time);
            startActivity(intent);
        });

        // Show a tip using Snackbar
        View mainLayout = findViewById(android.R.id.content);
        Snackbar.make(mainLayout, "Select an Exhibition", Snackbar.LENGTH_LONG).show();
    }

    // Method to open booking activity
    private void openBookingActivity(String exhibitionName) {
        Intent intent = new Intent(MainActivity.this, bookingActivity.class);
        intent.putExtra("EXHIBITION_NAME", exhibitionName);
        startActivity(intent);
    }
}