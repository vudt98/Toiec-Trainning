package com.example.toeictraining.widget.seekbar.formatter;

public class DateTextFormatter implements TextFormatter {
    @Override
    public String format(float value) {

        return "Ngày thứ " + (int) value;
    }
}
