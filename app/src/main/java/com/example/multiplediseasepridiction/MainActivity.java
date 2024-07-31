package com.example.multiplediseasepridiction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextInputEditText preg, glu, bp, skin, insulin, bmi, dpf, age;
    Button predictButton;
    TextView result;
    String url = "https://diabetes-predi.onrender.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preg = findViewById(R.id.preg);
        glu = findViewById(R.id.glu);
        bp = findViewById(R.id.bp);
        skin = findViewById(R.id.skin);
        insulin = findViewById(R.id.insulin);
        bmi = findViewById(R.id.bmi);
        dpf = findViewById(R.id.dpf);
        age = findViewById(R.id.age);

        predictButton = findViewById(R.id.predictButton);
        result = findViewById(R.id.result);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                result.setVisibility(View.VISIBLE);

                                Log.d("Response", response);  // Log response for debugging
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log the entire JSON object for debugging
                                    Log.d("JSON Response", jsonObject.toString());

                                    // Check if the key "The Pers" exists in the JSON response
                                    if (jsonObject.has("The Pers")) {
                                        String data = jsonObject.getString("The Pers");

                                        if (data.equals("1")) {
                                            result.setText("The Person is Diabetic");
                                        } else {
                                            result.setText("The Person is Not Diabetic");
                                        }
                                    } else {
                                        result.setText("Unexpected response format");
                                        Toast.makeText(MainActivity.this, "Key 'The Pers' not found in the response", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Response parsing error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());  // Log error for debugging
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Pregnancies", preg.getText().toString());
                        params.put("Glucose", glu.getText().toString());
                        params.put("BloodPressure", bp.getText().toString());
                        params.put("SkinThickness", skin.getText().toString());
                        params.put("Insulin", insulin.getText().toString());
                        params.put("BMI", bmi.getText().toString());
                        params.put("DiabetesPedigreeFunction", dpf.getText().toString());
                        params.put("Age", age.getText().toString());

                        return params;
                    }

                };
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stringRequest);
            }
        });
    }
}