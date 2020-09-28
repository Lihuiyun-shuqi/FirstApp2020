package com.swufe.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity implements Runnable{
    private static final String TAG = "MainActivity2";
    EditText dolRate,eurRate,woRate;
    Handler handler;

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
        float dollar2 = bdl_gain.getFloat("dollar_rate_key",0.1f);
        float euro2 = bdl_gain.getFloat("euro_rate_key",0.1f);
        float won2 = bdl_gain.getFloat("won_rate_key",0.1f);

        Log.i(TAG,"oneCreate:dollar2="+dollar2);
        Log.i(TAG,"oneCreate:euro2="+euro2);
        Log.i(TAG,"oneCreate:won2="+won2);

        dolRate = (EditText)findViewById(R.id.dollarRate);
        eurRate = (EditText)findViewById(R.id.euroRate);
        woRate = (EditText)findViewById(R.id.wonRate);

        dolRate.setText(String.valueOf(dollar2));
        eurRate.setText(String.valueOf(euro2));
        woRate.setText(String.valueOf(won2));

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        //线程间消息同步
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 5){
                    String str = (String)msg.obj;
                    Log.i(TAG,"handleMessage:getMessage msg = " + str);
                }
                super.handleMessage(msg);
            }
        };
    }

    public void saveconfig_return(View btn){
        dolRate = (EditText)findViewById(R.id.dollarRate);
        eurRate = (EditText)findViewById(R.id.euroRate);
        woRate = (EditText)findViewById(R.id.wonRate);

        float new_dollar = Float.parseFloat(dolRate.getText().toString());
        float new_euro = Float.parseFloat(eurRate.getText().toString());
        float new_won = Float.parseFloat(woRate.getText().toString());

        //使用SharedPreferences对象保存数据
        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate",new_dollar);
        editor.putFloat("euro_rate",new_euro);
        editor.putFloat("won_rate",new_won);
        editor.apply();

        //使用Bundle传递参数
        Intent inte = getIntent();
        Bundle bdl_ret = new Bundle();
        bdl_ret.putFloat("dollar_rate_key",new_dollar);
        bdl_ret.putFloat("euro_rate_key",new_euro);
        bdl_ret.putFloat("won_rate_key",new_won);
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

    @Override
    public void run() {
        Log.i(TAG,"run:run().....");

        //获取网络数据
        URL url = null;
        try{
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpsURLConnection http = (HttpsURLConnection)url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,"run:html = " + html);

            //获取msg对象，用于返回主线程
            Message msg = handler.obtainMessage(5);
            //msg.what = 5;
            //msg.obj = "Hello from run()";
            msg.obj = html;
            handler.sendMessage(msg);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        /*//获取msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);*/
    }

    //输入流转字符串
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        while(true){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz < 0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}