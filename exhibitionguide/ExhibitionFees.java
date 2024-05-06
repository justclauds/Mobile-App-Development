package com.example.exhibitionguide;

public class ExhibitionFees {
    // Define constants for the fees
    public static final int ART_GALLERY_WEEKDAY = 25;
    public static final int ART_GALLERY_WEEKEND = 30;
    public static final int WWI_EXHIBITION_WEEKDAY = 20;
    public static final int WWI_EXHIBITION_WEEKEND = 25;
    public static final int EXPLORING_THE_SPACE_WEEKDAY = 30;
    public static final int EXPLORING_THE_SPACE_WEEKEND = 35;
    public static final int VISUAL_SHOW_WEEKDAY = 40;
    public static final int VISUAL_SHOW_WEEKEND = 40;

    // Define method to determine the base fee based on the exhibition and day
    public static int determineBaseFee(String exhibition, String day) {
        int baseFee;
        switch (exhibition) {
            case "Art Gallery":
                baseFee = (day.equals("Saturday") || day.equals("Sunday")) ? ART_GALLERY_WEEKEND : ART_GALLERY_WEEKDAY;
                break;
            case "WWI Exhibition":
                baseFee = (day.equals("Saturday") || day.equals("Sunday")) ? WWI_EXHIBITION_WEEKEND : WWI_EXHIBITION_WEEKDAY;
                break;
            case "Exploring the Space":
                baseFee = (day.equals("Saturday") || day.equals("Sunday")) ? EXPLORING_THE_SPACE_WEEKEND : EXPLORING_THE_SPACE_WEEKDAY;
                break;
            case "Visual Show":
                baseFee = (day.equals("Saturday") || day.equals("Sunday")) ? VISUAL_SHOW_WEEKEND : VISUAL_SHOW_WEEKDAY;
                break;
            default:
                baseFee = 0; // Default to 0 if the exhibition is not recognized
                break;
        }
        return baseFee;
    }
}