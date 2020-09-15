package com.swufe.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView out;
    EditText edit;
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
        out = (TextView) findViewById(R.id.textView3);
        edit = (EditText)findViewById(R.id.editTextNumberDecimal);
    }
    public void btn(View v){
        String s = edit.getText().toString();
        double te = Double.parseDouble(s);
        double en = te * 1.8 + 32;
        String str = String.valueOf(en);
        out.setText("结果为："+str);
    }
}