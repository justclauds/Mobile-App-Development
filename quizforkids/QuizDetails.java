package com.example.quizforkids;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class QuizDetails {
    private String username;
    private String quizType;
    private int score;
    private long timestamp;

    public QuizDetails(){}

    public QuizDetails(String username, String quizType, int score, long timestamp) {
        this.username = username;
        this.quizType = quizType;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return quizType + " - attempted on " + getFormattedDateTime() + " - points earned " + score;
    }

    private String getFormattedDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date(timestamp));
    }
}
