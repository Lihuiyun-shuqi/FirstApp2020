package com.swufe.firstapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2";
    EditText dolRate,eurRate,woRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        //使用putExtra传递参数时获取数据
        /*double dollar2 = intent.getDoubleExtra("dollar_rate_key",0.0f);
        double euro2 = intent.getDoubleExtra("euro_rate_key",0.0f);
        double won2 = intent.getDoubleExtra("won_rate_key",0.0f);*/

        //使用Bundle传递参数时获取数据
        Bundle bdl_gain = intent.getExtras();
        double dollar2 = bdl_gain.getDouble("dollar_rate_key",0.1f);
        double euro2 = bdl_gain.getDouble("euro_rate_key",0.1f);
        double won2 = bdl_gain.getDouble("won_rate_key",0.1f);

        Log.i(TAG,"oneCreate:dollar2="+dollar2);
        Log.i(TAG,"oneCreate:euro2="+euro2);
        Log.i(TAG,"oneCreate:won2="+won2);

        dolRate = (EditText)findViewById(R.id.dollarRate);
        eurRate = (EditText)findViewById(R.id.euroRate);
        woRate = (EditText)findViewById(R.id.wonRate);

        dolRate.setText(String.valueOf(dollar2));
        eurRate.setText(String.valueOf(euro2));
        woRate.setText(String.valueOf(won2));
    }

    public void saveconfig_return(View btn){
        dolRate = (EditText)findViewById(R.id.dollarRate);
        eurRate = (EditText)findViewById(R.id.euroRate);
        woRate = (EditText)findViewById(R.id.wonRate);

        double new_dollar = Double.parseDouble(dolRate.getText().toString());
        double new_euro = Double.parseDouble(eurRate.getText().toString());
        double new_won = Double.parseDouble(woRate.getText().toString());

        Intent inte = getIntent();
        Bundle bdl_ret = new Bundle();
        bdl_ret.putDouble("dollar_rate_key",new_dollar);
        bdl_ret.putDouble("euro_rate_key",new_euro);
        bdl_ret.putDouble("won_rate_key",new_won);
        inte.putExtras(bdl_ret);
        setResult(3,inte);//设置resultCode及带回的数据

        finish();//返回到调用页面
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