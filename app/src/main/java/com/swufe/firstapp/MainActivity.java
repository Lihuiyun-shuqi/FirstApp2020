package com.swufe.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView out;
    //EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        //获取控件 out
        TextView outobj = findViewById(R.id.textView);
        outobj.setText("Hello");
        //Log.i( tag:"loading",  msg:"onCreate: msg......");

        EditText edit = (EditText)findViewById(R.id.editTextTextPersonName);
        String str = edit.getText().toString();*/
        out = (TextView) findViewById(R.id.textView6);
        //edit = (EditText)findViewById(R.id.editTextNumberDecimal);
    }
    public void btna(View v){
        int score = Integer.parseInt(out.getText().toString());
        int t = score + 1;
        String str = String.valueOf(t);
        out.setText(str);
    }
    public void btnb(View v){
        int score = Integer.parseInt(out.getText().toString());
        int t = score + 2;
        String str = String.valueOf(t);
        out.setText(str);
    }
    public void btnc(View v){
        int score = Integer.parseInt(out.getText().toString());
        int t = score + 3;
        String str = String.valueOf(t);
        out.setText(str);
    }
    public void reset(View v){
        int t = 0;
        String str = String.valueOf(t);
        out.setText(str);
    }
}