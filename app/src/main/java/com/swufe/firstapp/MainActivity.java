package com.swufe.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView out;
    EditText edit;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText)findViewById(R.id.editTextNumberDecimal);
        out = (TextView) findViewById(R.id.textView);
        df = new DecimalFormat( "0.0000");//设置double类型小数点后位数格式
    }
    public void btna(View v){
        double oc = Double.parseDouble(edit.getText().toString());
        double t = oc * 0.1471;
        String s = String.valueOf(df.format(t));
        String str = edit.getText().toString() + " RMB =" + s + " DOLLAR";
        out.setText(str);
    }
    public void btnb(View v){
        double oc = Double.parseDouble(edit.getText().toString());
        double t = oc * 0.125;
        String s = String.valueOf(df.format(t));
        String str = edit.getText().toString() + " RMB =" + s + " EURO";
        out.setText(str);
    }
    public void btnc(View v){
        double oc = Double.parseDouble(edit.getText().toString());
        double t = oc * 171.4571;
        String s = String.valueOf(df.format(t));
        String str = edit.getText().toString() + " RMB =" + s + " WON";
        out.setText(str);
    }
}