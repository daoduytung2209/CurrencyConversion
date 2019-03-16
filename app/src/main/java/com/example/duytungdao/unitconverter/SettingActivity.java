package com.example.duytungdao.unitconverter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button buttonRed = (Button) findViewById(R.id.buttonRed);
        buttonRed.setOnClickListener(this);
        Button buttonGreen = (Button) findViewById(R.id.buttonGreen);
        buttonGreen.setOnClickListener(this);
        Button buttonYellow = (Button) findViewById(R.id.buttonYellow);
        buttonYellow.setOnClickListener(this);
        Button buttonWhite = (Button) findViewById(R.id.buttonWhite);
        buttonWhite.setOnClickListener(this);
    }

    public void onClick(View view){
        Intent returnIntent = new Intent();
        String color = "";
        switch (view.getId()){
            case R.id.buttonRed:
                color = "Red";
                break;
            case R.id.buttonYellow:
                color = "Yellow";
                break;
            case R.id.buttonGreen:
                color = "Green";
                break;
            case R.id.buttonWhite:
                color = "White";
                break;
        }
        returnIntent.putExtra("KeyForReturning", color);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
