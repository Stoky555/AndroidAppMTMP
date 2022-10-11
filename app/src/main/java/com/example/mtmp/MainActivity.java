package com.example.mtmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText speedField, angleField;
    private Switch switchInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calculateButton = findViewById(R.id.toCalculationActivityButton);
        Button animationButton = findViewById(R.id.animationButton);
        Button graphButton = findViewById(R.id.graphButton);
        speedField = findViewById(R.id.speedField);
        angleField = findViewById(R.id.angleField);
        switchInternet = findViewById(R.id.switch1);

        calculateButton.setOnClickListener(view -> toCalculationActivity());
        animationButton.setOnClickListener(view -> toAnimationActivity());
        graphButton.setOnClickListener(view -> toGraphActivity());
    }

    public void toCalculationActivity(){
        if(speedField.getText().toString().isEmpty() || angleField.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "BOTH FIELDS HAS TO BE FILLED!!!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.parseDouble(angleField.getText().toString()) > 90){
            Toast.makeText(MainActivity.this, "BETWEEN 0 AND 90 PLEASE!!!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, CalculationActivity.class);
        intent.putExtra("speed", Double.parseDouble(speedField.getText().toString()));
        intent.putExtra("angle", Double.parseDouble(angleField.getText().toString()));
        intent.putExtra("switch", switchInternet.isChecked());
        startActivity(intent);
    }

    public void toAnimationActivity(){
        if(speedField.getText().toString().isEmpty() || angleField.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "BOTH FIELDS HAS TO BE FILLED!!!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.parseDouble(angleField.getText().toString()) > 90){
            Toast.makeText(MainActivity.this, "BETWEEN 0 AND 90 PLEASE!!!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, AnimationActivity.class);
        intent.putExtra("speed", Double.parseDouble(speedField.getText().toString()));
        intent.putExtra("angle", Double.parseDouble(angleField.getText().toString()));
        intent.putExtra("switch", switchInternet.isChecked());
        startActivity(intent);
    }

    public void toGraphActivity(){
        if(speedField.getText().toString().isEmpty() || angleField.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "BOTH FIELDS HAS TO BE FILLED!!!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.parseDouble(angleField.getText().toString()) > 90){
            Toast.makeText(MainActivity.this, "BETWEEN 0 AND 90 PLEASE!!!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("speed", Double.parseDouble(speedField.getText().toString()));
        intent.putExtra("angle", Double.parseDouble(angleField.getText().toString()));
        intent.putExtra("switch", switchInternet.isChecked());
        startActivity(intent);
    }
}