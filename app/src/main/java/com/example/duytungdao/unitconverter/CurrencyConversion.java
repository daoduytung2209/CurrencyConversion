package com.example.duytungdao.unitconverter;

public class CurrencyConversion {

    private double inputValue;
    private double outputValue;

    public void setInputValue(double inputValue) {
        this.inputValue = inputValue;
    }

    public double getOutputValue(double rate) {
        outputValue = inputValue * rate;
        return outputValue;
    }
}
