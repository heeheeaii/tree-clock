package com.treevalue.clock.func;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimeDigits {
    ArrayList<Integer> getTime() {
        LocalTime now = LocalTime.now();

        // Format the time as a string (HHmmss)
        String timeString = now.format(DateTimeFormatter.ofPattern("HHmmss"));

        // Create an ArrayList to store each digit
        ArrayList<Integer> digits = new ArrayList<>();

        // Iterate over each character in the time string
        for (char ch : timeString.toCharArray()) {
            // Convert character to integer and add to the list
            digits.add(Character.getNumericValue(ch));
        }
        return digits;
    }

    public static void main(String[] args) {

    }
}
