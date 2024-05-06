package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.Calendar;
import java.text.DecimalFormat;

public class bookingActivity extends AppCompatActivity {

    private String chosenDay;
    private TextView totalFeeAmount;
    private TextView discountAmount;
    private TextView amountDue;
    private EditText visitorInputText;
    private TextView exhibitionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Spinner spinnerDays = findViewById(R.id.spinner_days);
        Spinner spinnerTime = findViewById(R.id.spinner_time);
        exhibitionText = findViewById(R.id.exhibitionText);
        totalFeeAmount = findViewById(R.id.totalFeeAmount);
        discountAmount = findViewById(R.id.discountAmount);
        amountDue = findViewById(R.id.amountDue);
        visitorInputText = findViewById(R.id.visitorInputText);

        TextView selectedExhibition = findViewById(R.id.selectedExhibition);
        TextView selectedDay = findViewById(R.id.selectedDay);
        TextView selectedTime = findViewById(R.id.selectedTime);
        TextView selectedVisitors = findViewById(R.id.selectedVisitors);

        selectedVisitors.setText("1");
        // Retrieve the exhibition name from the Intent extras
        String exhibitionName = getIntent().getStringExtra("EXHIBITION_NAME");
        if (exhibitionName != null) {
            exhibitionText.setText(exhibitionName);
            selectedExhibition.setText(exhibitionName);
        }

        // Create ArrayAdapter for days spinner using the string array
        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(daysAdapter);

        // Set default selection for spinnerDays based on the current day
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        spinnerDays.setSelection(currentDay - 1); // Days in Calendar start from Sunday as 1

        // Retrieve the resource ID of the time array from the Intent extras
        int timeArrayResourceId = getIntent().getIntExtra("TIME_ARRAY", -1);
        if (timeArrayResourceId != -1) {
            if (timeArrayResourceId == R.array.visual_time) {
                // Create ArrayAdapter for time spinner using the visual_time array
                ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                        timeArrayResourceId, android.R.layout.simple_spinner_item);
                timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTime.setAdapter(timeAdapter);

                // Set default selection for spinnerTime based on the current time
                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

                // Calculate the current time in minutes
                int currentTimeInMinutes = currentHour * 60 + currentMinute;

                // Calculate the index of the earliest time slot in the time array
                int earliestTimeIndex = -1;

                // Determine the earliest time slot based on current time
                if (currentTimeInMinutes >= (15 * 60) && currentTimeInMinutes < (17 * 60)) {
                    earliestTimeIndex = 1; // If current time is between 3:00 PM to 5:00 PM
                } else if (currentTimeInMinutes >= (17 * 60) && currentTimeInMinutes < (19 * 60)) {
                    earliestTimeIndex = 2; // If current time is between 5:00 PM to 7:00 PM
                } else if (currentTimeInMinutes >= (19 * 60) && currentTimeInMinutes < (21 * 60)) {
                    earliestTimeIndex = 3; // If current time is between 7:00 PM to 9:00 PM
                }

                // If the current time has passed the last available slot, move to the next day's first slot
                if (earliestTimeIndex == -1) {
                    // Increment the day
                    //int nextDay = (currentDay % 7) + 1; // Loop back to Sunday if it's currently Saturday
                    //spinnerDays.setSelection(nextDay - 1);

                    // Set the earliest time index to the first slot of the next day
                    earliestTimeIndex = 0; // Start with the first slot of the visual_time array
                }

                // Set the selection to the earliest available time slot
                spinnerTime.setSelection(earliestTimeIndex);
            } else {
                // Create ArrayAdapter for time spinner using the specified time array
                ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                        timeArrayResourceId, android.R.layout.simple_spinner_item);
                timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTime.setAdapter(timeAdapter);
            }
        } else {
            // Default to using time_regular array if no array is specified
            ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                    R.array.time_regular, android.R.layout.simple_spinner_item);
            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTime.setAdapter(timeAdapter);

            // Set default selection for spinnerTime based on the current time
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

            // Calculate the current time in minutes
            int currentTimeInMinutes = currentHour * 60 + currentMinute;

            // Calculate the index of the earliest time slot in the time array
            int earliestTimeIndex = (currentTimeInMinutes - 540 + 30) / 30;

            // If the current time has passed the last available slot, move to the next day's first slot
            if (earliestTimeIndex >= timeAdapter.getCount()) {
                // Increment the day
                int nextDay = (currentDay % 7) + 1; // Loop back to Sunday if it's currently Saturday
                spinnerDays.setSelection(nextDay - 1);

                // Set the earliest time index to the first slot of the next day
                earliestTimeIndex = 0;
            }

            // Set the selection to the earliest available time slot
            spinnerTime.setSelection(earliestTimeIndex);
        }

        // Set default values for selected day and time TextViews
        selectedDay.setText(spinnerDays.getSelectedItem().toString());
        selectedTime.setText(spinnerTime.getSelectedItem().toString());

        // Set up listener for spinnerDays to update chosenDay
        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenDay = parent.getItemAtPosition(position).toString();
                selectedDay.setText(chosenDay); // Update selectedDay TextView
                calculateTotalFee(); // Calculate total fee when day changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up listener for spinnerTime to update selectedTime
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Listener for changes in the visitor input
        visitorInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed, but required to implement the TextWatcher interface
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotalFee();

                String visitorInput = s.toString();

                selectedVisitors.setText(visitorInput);

            }
        });
    }
        // Method to calculate total fee based on the number of visitors, chosen day, and exhibition
        private void calculateTotalFee() {
            String visitorInput = visitorInputText.getText().toString();
            if (!visitorInput.isEmpty()) {
                int numberOfVisitors = Integer.parseInt(visitorInput);

                // Retrieve the selected exhibition
                String chosenExhibition = exhibitionText.getText().toString();

                // Determine the base fee based on the selected exhibition and chosenDay
                int baseFee = ExhibitionFees.determineBaseFee(chosenExhibition, chosenDay);

                // Determine the total fee and discount amount
                int totalFee = baseFee * numberOfVisitors;
                double discountPercentage = (numberOfVisitors >= 4) ? 0.1 : 0.0; // 10% discount if 4 or more visitors
                double discount = totalFee * discountPercentage;
                double amountTotal = totalFee - discount;

                // Display total fee and discount amount
                totalFeeAmount.setText(totalFee + " AUD");
                discountAmount.setText("-" + new DecimalFormat("0.0").format(discount) + " AUD");
                amountDue.setText(new DecimalFormat("0.0").format(amountTotal) + " AUD");
            }
        }
    }

