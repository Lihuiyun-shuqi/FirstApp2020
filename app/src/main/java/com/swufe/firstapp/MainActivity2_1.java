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
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2_1 extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener{
    private static final String TAG = "MainActivity2_1";
    ListView myList;
    Handler handler;
    List<String> dataList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_1);

        //获取ListView控件
        myList = (ListView) findViewById(R.id.mylist);
        //简单数据测试：使用ListView控件，把获取的数据以列表形式展示出来
        /*String data[] = {"one","two","three","four"};
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        myList.setAdapter(adapter);*/

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        //线程间消息同步
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 5){
                    //String str = (String)msg.obj;
                    //Log.i(TAG,"handleMessage:getMessage msg = " + str);
                    dataList = (List<String>) msg.obj;
                    //ListAdapter adapter = new ArrayAdapter<String>....
                    adapter = new ArrayAdapter<String>(MainActivity2_1.this,
                            android.R.layout.simple_list_item_1,
                            dataList);
                    myList.setAdapter(adapter);
                    myList.setOnItemClickListener(MainActivity2_1.this);//添加事件监听
                }
                super.handleMessage(msg);
            }
        };


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

        //获取msg对象，用于返回主线程
        /*Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);*/

        //使用Jsoup解析网页数据
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Log.i(TAG,"run:" + doc.title());
            Element table = doc.getElementsByTag("table").first();//获取标签为table的第一个元素，即上面两句话的意思

            //获取TD中的数据
            Elements trs = table.getElementsByTag("tr");
            List<String> list = new ArrayList<String>();
            for(Element tr : trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size() > 0){
                    //get data
                    String td1 = tds.get(0).text();
                    String td2 = tds.get(5).text();
                    float v = 100f / Float.parseFloat(td2);
                    //float rate =(float)(Math.round(v*100))/100;//取两位小数
                    Log.i(TAG,"run:" + td1 + "==>" + v);
                    list.add(td1 + "==>" + v);
                }
            }

            Message msg = handler.obtainMessage(5);
            msg.obj = list;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //ArrayAdapter类型的列表数据项删除(更新适配器的操作自动调用)
        adapter.remove(myList.getItemAtPosition(position));

        Log.i(TAG, "onItemClick: 已删除");
    }

}