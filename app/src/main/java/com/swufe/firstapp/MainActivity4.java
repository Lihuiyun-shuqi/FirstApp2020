package com.swufe.firstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener{
    private static final String TAG = "MainActivity4";
    ListView myList;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        //获取ListView控件
        myList = (ListView) findViewById(R.id.mylist2);

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        //线程间消息同步
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 6){
                    ArrayList<HashMap<String,String>> dataList = (ArrayList<HashMap<String,String>>) msg.obj;

                    MyAdapter myAdapter = new MyAdapter(MainActivity4.this,
                            R.layout.list_item,
                            dataList);
                    myList.setAdapter(myAdapter);
                    myList.setOnItemClickListener(MainActivity4.this);//添加事件监听
                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void run() {
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            //Log.i(TAG,"run:" + doc.title());
            Element table = doc.getElementsByTag("table").first();

            //获取TD中的数据
            Elements trs = table.getElementsByTag("tr");
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
            for(Element tr : trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size() > 0){
                    //get data
                    String td1 = tds.get(0).text();
                    String td2 = tds.get(5).text();
                    float v = 100f / Float.parseFloat(td2);
                    //float rate =(float)(Math.round(v*100))/100;//取两位小数
                    //Log.i(TAG,"run:" + td1 + "==>" + v);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("ItemTitle",td1);
                    map.put("ItemDetail",String.valueOf(v));
                    list.add(map);
                }
            }

            Message msg = handler.obtainMessage(6);
            msg.obj = list;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //重写方法，对点击ListView后进行操作
        Object itemAtPosition = myList.getItemAtPosition(position);//获取ListView中点击的数据
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Float curr_rate = Float.parseFloat(detailStr);
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);


        /*TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick: title2=" + title2);
        Log.i(TAG, "onItemClick: detail2=" + detail2);*/


        Intent config_new = new Intent(this,MainActivity5.class);
        //传递参数
        Bundle bdl = new Bundle();
        bdl.putString("itemTitle",titleStr);
        bdl.putFloat("itemDetail",curr_rate);
        config_new.putExtras(bdl);

        Log.i(TAG,"openOne:itemTitle="+titleStr);
        Log.i(TAG,"openOne:itemDetail="+curr_rate);
        //打开新页面
        startActivity(config_new);
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

    public void save_return(View btn){
        Intent inte = getIntent();
        setResult(2,inte);

        finish();//返回到调用页面
    }
}