package com.swufe.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity implements Runnable{
    private static final String TAG = "MainActivity2";
    EditText dolRate,eurRate,woRate;
    Handler handler;
    float new_dollar,new_euro,new_won;
    String todayDate,lastUpdateDate;
    SharedPreferences sp2;

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

        //实现每天更新一次汇率，而不是每次打开都更新：从xml文件中取出上次更新的日期，若与今天日期不相同则更新数据，否则不更新
        sp2 = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        lastUpdateDate = sp2.getString("update_date", "");

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 5){
                    HashMap<String,String> dataMap = (HashMap<String,String>) msg.obj;
                    SharedPreferences.Editor editor2 = sp2.edit();
                    new_dollar = Float.parseFloat(dataMap.get("dollar_rate"));
                    new_euro = Float.parseFloat(dataMap.get("euro_rate"));
                    new_won = Float.parseFloat(dataMap.get("won_rate"));
                    editor2.putFloat("dollar_rate",new_dollar);
                    editor2.putFloat("euro_rate",new_euro);
                    editor2.putFloat("won_rate",new_won);
                    editor2.apply();
                }
                super.handleMessage(msg);
            }
        };
    }

    public void saveconfig_return(View btn){
        dolRate = (EditText)findViewById(R.id.dollarRate);
        eurRate = (EditText)findViewById(R.id.euroRate);
        woRate = (EditText)findViewById(R.id.wonRate);

        new_dollar = Float.parseFloat(dolRate.getText().toString());
        new_euro = Float.parseFloat(eurRate.getText().toString());
        new_won = Float.parseFloat(woRate.getText().toString());

        //使用SharedPreferences对象保存数据
        SharedPreferences sp1 = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putFloat("dollar_rate",new_dollar);
        editor1.putFloat("euro_rate",new_euro);
        editor1.putFloat("won_rate",new_won);
        editor1.apply();

        //使用Bundle传递参数
        Intent inte = getIntent();
        Bundle bdl_ret = new Bundle();
        bdl_ret.putFloat("dollar_rate_key",new_dollar);
        bdl_ret.putFloat("euro_rate_key",new_euro);
        bdl_ret.putFloat("won_rate_key",new_won);
        inte.putExtras(bdl_ret);
        setResult(1,inte);//设置resultCode及带回的数据

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
        Log.i(TAG, "run:run().....");

        //获取msg对象，用于返回主线程
        /*Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);*/

        /*//获取网络数据
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
        }*/

        HashMap<String, String> map = new HashMap<String, String>();

        todayDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run", "todayDateStr: " + todayDate + " lastUpdateDateStr: " + lastUpdateDate);

        if (todayDate.equals(lastUpdateDate)) {
            //日期如果相等，则不从网络中获取数据，从数据库中获取数据
            Log.i("run", "日期相等，不需要更新！从数据库中获取数据");
            RateManager rateManager1 = new RateManager(this);
            RateItem rateItem1 = new RateItem();

            rateItem1 = rateManager1.findByCurName("美元");
            dolRate.setText(rateItem1.getCurrate());
            map.put("dollar_rate", rateItem1.getCurrate());

            rateItem1 = rateManager1.findByCurName("欧元");
            eurRate.setText(rateItem1.getCurrate());
            map.put("euro_rate", rateItem1.getCurrate());

            rateItem1 = rateManager1.findByCurName("韩元");
            woRate.setText(rateItem1.getCurrate());
            map.put("won_rate", rateItem1.getCurrate());

        } else {
            //日期不相等，则从网络中获取数据
            Log.i("run", "日期不相等，需要更新！从网络中获取数据");

            //使用Jsoup解析网页数据
            String url = "https://www.usd-cny.com/bankofchina.htm";
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Log.i(TAG, "run:" + doc.title());
                Element table = doc.getElementsByTag("table").first();//获取标签为table的第一个元素，即上面两句话的意思

                //获取TD中的数据
                Elements trs = table.getElementsByTag("tr");
                List<RateItem> rateList = new ArrayList<RateItem>();
                for (Element tr : trs) {
                    Elements tds = tr.getElementsByTag("td");
                    if (tds.size() > 0) {
                        //get data
                        String td1 = tds.get(0).text();
                        String td2 = tds.get(5).text();
                        float v = 100f / Float.parseFloat(td2);
                        //float rate =(float)(Math.round(v*100))/100;//取两位小数
                        Log.i(TAG, "run:" + td1 + "==>" + v);

                        RateItem rateItem = new RateItem(td1,String.valueOf(v));
                        rateList.add(rateItem);

                        //获取数据并返回
                        if (td1.equals("美元")) {
                            new_dollar = v;
                            Log.i(TAG, "run:" + td1 + "==>" + new_dollar);
                            dolRate.setText(String.valueOf(new_dollar));
                            map.put("dollar_rate", String.valueOf(new_dollar));
                        }
                        if (td1.equals("欧元")) {
                            new_euro = v;
                            Log.i(TAG, "run:" + td1 + "==>" + new_euro);
                            eurRate.setText(String.valueOf(new_euro));
                            map.put("euro_rate", String.valueOf(new_euro));
                        }
                        if (td1.equals("韩元")) {
                            new_won = v;
                            Log.i(TAG, "run:" + td1 + "==>" + new_won);
                            woRate.setText(String.valueOf(new_won));
                            map.put("won_rate", String.valueOf(new_won));
                        }
                    }
                }

                RateManager rateManager2 = new RateManager(this);
                rateManager2.deleteAll();
                Log.i("db","删除所有记录");
                rateManager2.addAll(rateList);
                Log.i("db","添加新记录集");

            } catch (IOException e) {
                e.printStackTrace();
            }
            //更新记录日期
            SharedPreferences.Editor editor = sp2.edit();
            editor.putString("update_date", todayDate);
            editor.apply();
            Log.i("run","更新日期结束：" + todayDate);
        }
        Message msg = handler.obtainMessage(5);
        msg.obj = map;
        handler.sendMessage(msg);
    }

    /*//输入流转字符串
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
    }*/

    public void newRateList(View v){
        Intent config1 = new Intent(this,MainActivity4.class);
        startActivityForResult(config1,2);//打开可返回数据的窗口
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && resultCode==2){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}