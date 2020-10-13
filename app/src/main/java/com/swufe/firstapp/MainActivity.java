package com.swufe.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView out;
    EditText edit;
    DecimalFormat df;
    float dollar_rate,euro_rate,won_rate;
    float dollar_rate_in = (float) 0.15;
    float euro_rate_in = (float) 0.13;
    float won_rate_in = (float) 171.45;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText)findViewById(R.id.editTextNumberDecimal);
        out = (TextView) findViewById(R.id.textView6);
        df = new DecimalFormat( "0.0000");//设置double类型小数点后位数格式

        //获取SharedPreferences对象中保存的数据
        SharedPreferences sp1 = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        dollar_rate = sp1.getFloat("dollar_rate",0.0f);
        euro_rate = sp1.getFloat("euro_rate",0.0f);
        won_rate = sp1.getFloat("won_rate",0.0f);

    }

    public void btn_convert(View btn){
        String ss = edit.getText().toString();
        if(ss==null || ss.equals("") || ss.equals(R.string.textView6)){//no input
            Toast.makeText(this, "请输入人民币金额", Toast.LENGTH_SHORT).show();
        }else{
            //第一次使用时，避免汇率为0（即当dollar_rate=0时，使用默认汇率）
            if(dollar_rate == 0.0){
                dollar_rate = dollar_rate_in;
            }
            if(euro_rate == 0.0){
                euro_rate = euro_rate_in;
            }
            if(won_rate == 0.0){
                won_rate = won_rate_in;
            }
            float oc = Float.parseFloat(edit.getText().toString());
            if(btn.getId()==R.id.buttona){
                float t = oc * dollar_rate;
                String s = String.valueOf(df.format(t));
                String str = edit.getText().toString() + " RMB =" + s + " DOLLAR";
                out.setText(str);
            }else if(btn.getId()==R.id.buttonb){
                float t = oc * euro_rate;
                String s = String.valueOf(df.format(t));
                String str = edit.getText().toString() + " RMB =" + s + " EURO";
                out.setText(str);
            }else if(btn.getId()==R.id.buttonc){
                float t = oc * won_rate;
                String s = String.valueOf(df.format(t));
                String str = edit.getText().toString() + " RMB =" + s + " WON";
                out.setText(str);
            }
        }
    }

    public void open_new(View v){
        Intent config = new Intent(this,MainActivity2.class);
        //Intent config = new Intent(this,MainActivity3.class);
        //使用putExtra传递参数
        /*config.putExtra("dollar_rate_key",dollar_rate);
        config.putExtra("euro_rate_key",euro_rate);
        config.putExtra("won_rate_key",won_rate);*/

        //使用Bundle传递参数
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate_key",dollar_rate);
        bdl.putFloat("euro_rate_key",euro_rate);
        bdl.putFloat("won_rate_key",won_rate);
        config.putExtras(bdl);

        Log.i(TAG,"openOne:dollarRate="+dollar_rate);
        Log.i(TAG,"openOne:euroRate="+euro_rate);
        Log.i(TAG,"openOne:wonRate="+won_rate);

        //startActivity(config);//打开一个新页面
        startActivityForResult(config,1);//打开可返回数据的窗口
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==3){
            Bundle bdl_gain_new = data.getExtras();
            dollar_rate = bdl_gain_new.getFloat("dollar_rate_key",0.1f);
            euro_rate = bdl_gain_new.getFloat("euro_rate_key",0.1f);
            won_rate = bdl_gain_new.getFloat("won_rate_key",0.1f);

            Log.i(TAG,"onActivityResult:dollarRate="+dollar_rate);
            Log.i(TAG,"onActivityResult:euroRate="+euro_rate);
            Log.i(TAG,"onActivityResult:wonRate="+won_rate);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//启用菜单项
        getMenuInflater().inflate(R.menu.first_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//处理菜单事件，设置菜单项中每一个item的功能
        if(item.getItemId()==R.id.menu1){
            //设置功能，与设置按钮的事件一样
        }
        return super.onOptionsItemSelected(item);
    }
}