package com.example.duytungdao.unitconverter;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Test
    public void conversion_isCorrect() throws Exception {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        String[] units = {"AUD","CAD","CHF","EUR","GBP","HKD","JPY","NZD","SGD","USD"};
        String fromUnit = "AUD";
        String[] audRates = {"1","0.99706","0.73209","0.62523","0.54647","6.0271","81.593","1.0728",
                "1.0127","0.76816"};

        currencyConversion.setInputValue(10);

        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[0]))).equals("10"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[1]))).equals("9.97"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[2]))).equals("7.32"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[3]))).equals("6.25"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[4]))).equals("5.46"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[5]))).equals("60.27"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[6]))).equals("815.93"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[7]))).equals("10.73"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[8]))).equals("10.13"));
        assertTrue(df2.format(currencyConversion.getOutputValue(Double.parseDouble(audRates[9]))).equals("7.68"));
    }
}

