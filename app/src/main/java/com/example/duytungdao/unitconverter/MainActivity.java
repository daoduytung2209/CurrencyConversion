package com.example.duytungdao.unitconverter;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    CurrencyConversion currencyConversion;
    LinearLayout mainLayout;
    Spinner unitsSpinner;
    EditText inputText;
    TextView audValue, cadValue, chfValue, eurValue, gbpValue, hkdValue, jpyValue, nzdValue,
            sgdValue, usdValue;
    TextWatcher textWatcher = null;
    FloatingActionButton settingButton;

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    public static final int DETAIL_REQUEST = 1;
    public int currentSpinnerPosition;
    public int selectedSpinnerPosition;
    public boolean isJustStarted = true;
    private int currentBackgroundColor;
    String[] currencyUnits = {"AUD","CAD","CHF","EUR","GBP","HKD","JPY","NZD","SGD","USD"};
    String[] currencyRates = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main_layout);
        unitsSpinner = findViewById(R.id.currency_spinner);
        inputText = findViewById(R.id.input_text);
        settingButton = findViewById(R.id.setting_button);

        audValue = findViewById(R.id.aud_value);
        cadValue = findViewById(R.id.cad_value);
        chfValue = findViewById(R.id.chf_value);
        eurValue = findViewById(R.id.eur_value);
        gbpValue = findViewById(R.id.gbp_value);
        hkdValue = findViewById(R.id.hkd_value);
        jpyValue = findViewById(R.id.jpy_value);
        nzdValue = findViewById(R.id.nzd_value);
        sgdValue = findViewById(R.id.sgd_value);
        usdValue = findViewById(R.id.usd_value);

        if (savedInstanceState != null) {
            currentSpinnerPosition = savedInstanceState.getInt("currentSpinnerPosition");
            selectedSpinnerPosition = savedInstanceState.getInt("selectedSpinnerPosition");
            isJustStarted = savedInstanceState.getBoolean("isJustStarted");
            currencyRates = savedInstanceState.getStringArray("currencyRates");
            mainLayout.setBackgroundColor(savedInstanceState.getInt("backgroundColor"));
        }

        currencyConversion = new CurrencyConversion();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.commonUnits, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        unitsSpinner.setAdapter(adapter);

        unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnerPosition = position;
                new calculate().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    currencyConversion.setInputValue(0);
                }
                else {
                    currencyConversion.setInputValue(Double.parseDouble(charSequence.toString()));
                }
                new calculate().execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        inputText.addTextChangedListener(textWatcher);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, DETAIL_REQUEST);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("currentSpinnerPosition",currentSpinnerPosition);
        savedInstanceState.putInt("selectedSpinnerPosition",selectedSpinnerPosition);
        savedInstanceState.putBoolean("isJustStarted", isJustStarted);
        savedInstanceState.putStringArray("currencyRates",currencyRates);
        savedInstanceState.putInt("backgroundColor", currentBackgroundColor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String backgroundColor;
        if (resultCode == RESULT_OK && requestCode == DETAIL_REQUEST){
            backgroundColor = data.getExtras().getString("KeyForReturning");
            if (backgroundColor != null) {
                switch (backgroundColor) {
                    case "Red":
                        currentBackgroundColor = Color.parseColor("#ffd9b3");
                        Log.i("Test",""+currentBackgroundColor);
                        break;
                    case "Yellow":
                        currentBackgroundColor = Color.parseColor("#ffff99");
                        Log.i("Test",""+currentBackgroundColor);
                        break;
                    case "Green":
                        currentBackgroundColor = Color.parseColor("#b3ffb3");
                        Log.i("Test",""+currentBackgroundColor);
                        break;
                    case "White":
                        currentBackgroundColor = Color.WHITE;
                        Log.i("Test",""+currentBackgroundColor);
                        break;
                }
                mainLayout.setBackgroundColor(currentBackgroundColor);
            }
        }
    }

    public class calculate extends AsyncTask<String, String, String[]> {
        JSONObject jsonObject;
        JSONObject rateObj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String uRl;
            if (currentSpinnerPosition != selectedSpinnerPosition || isJustStarted) {
                try {
                    uRl = getJson("http://api.exchangeratesapi.io/latest?base=" + currencyUnits[selectedSpinnerPosition]);
                    jsonObject = new JSONObject(uRl);

                    rateObj = jsonObject.getJSONObject("rates");

                    for (int index = 0; index < currencyRates.length; index++) {
                        if (currencyUnits[index].equals(currencyUnits[selectedSpinnerPosition])) {
                            currencyRates[index] = "1";
                        } else {
                            currencyRates[index] = rateObj.getString(currencyUnits[index]);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isJustStarted = false;
                currentSpinnerPosition = selectedSpinnerPosition;
                return null;
            }
            else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            double audOutputValue, cadOutputValue, chfOutputValue, eurOutputValue, gbpOutputValue,
                    hkdOutputValue, jpyOutputValue, nzdOutputValue, sgdOutputValue, usdOutputValue;

            audOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[0]));
            audValue.setText("" + df2.format(audOutputValue));

            cadOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[1]));
            cadValue.setText("" + df2.format(cadOutputValue));

            chfOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[2]));
            chfValue.setText("" + df2.format(chfOutputValue));

            eurOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[3]));
            eurValue.setText("" + df2.format(eurOutputValue));

            gbpOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[4]));
            gbpValue.setText("" + df2.format(gbpOutputValue));

            hkdOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[5]));
            hkdValue.setText("" + df2.format(hkdOutputValue));

            jpyOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[6]));
            jpyValue.setText("" + df2.format(jpyOutputValue));

            nzdOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[7]));
            nzdValue.setText("" + df2.format(nzdOutputValue));

            sgdOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[8]));
            sgdValue.setText("" + df2.format(sgdOutputValue));

            usdOutputValue = currencyConversion.getOutputValue(Double.parseDouble(currencyRates[9]));
            usdValue.setText("" + df2.format(usdOutputValue));
        }

        private String getJson(String url) throws IOException {
            StringBuilder build = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String con;
            while ((con = reader.readLine()) != null) {
                build.append(con);
            }
            return build.toString();
        }
    }
}
