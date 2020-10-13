package com.swufe.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity5 extends AppCompatActivity {

    private static final String TAG = "MainActivity5";
    TextView out,title;
    EditText in;
    DecimalFormat df;
    float currency_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Intent intent = getIntent();
        //使用Bundle传递参数时获取数据
        Bundle bdl_gain = intent.getExtras();
        String itemTitle = bdl_gain.getString("itemTitle","");
        float rate = bdl_gain.getFloat("itemDetail",0.1f);
        Log.i(TAG,"oneCreate:itemTitle = " + itemTitle);
        Log.i(TAG,"oneCreate:itemDetail = " + rate);

        title = (TextView) findViewById(R.id.countryTitle);
        in = (EditText)findViewById(R.id.inDetail);
        out = (TextView) findViewById(R.id.outDetail);
        df = new DecimalFormat( "0.0000");//设置double类型小数点后位数格式

        title.setText(itemTitle);
        currency_rate = rate;
    }

    public void convert(View btn){
        String ss = in.getText().toString();
        if(ss==null || ss.equals("")){//no input
            Toast.makeText(this, "请输入人民币金额", Toast.LENGTH_SHORT).show();
        }else{
            float oc = Float.parseFloat(in.getText().toString());
            float t = oc * currency_rate;
            String s = String.valueOf(df.format(t));
            String str = in.getText().toString() + " 人民币 = " + s + title.getText();
            out.setText(str);
        }
    }
}