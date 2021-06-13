package com.example.toeictraining.widget.seekbar.formatter;

public class EurosTextFormatter implements TextFormatter {

    @Override
    public String format(float value) {
        return String.format("%d €", (int) value);
    }
}
