package com.swufe.firstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class MainActivity4 extends AppCompatActivity implements Runnable{
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
                if(msg.what == 5){
                    ArrayList<HashMap<String,String>> dataList = (ArrayList<HashMap<String,String>>) msg.obj;

                    MyAdapter myAdapter = new MyAdapter(MainActivity4.this,
                            R.layout.list_item,
                            dataList);
                    myList.setAdapter(myAdapter);
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

            Message msg = handler.obtainMessage(5);
            msg.obj = list;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //自定义适配器Adapter
    public class MyAdapter extends ArrayAdapter {
        private static final String TAG = "MyAdapter";
        public MyAdapter(Context context,
                         int resource,
                         ArrayList<HashMap<String,String>> list) {
            super(context, resource, list);
        }
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(MainActivity4.this).inflate(R.layout.list_item,
                    parent,
                    false);
        }
        Map<String,String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);
        title.setText(map.get("ItemTitle"));
        detail.setText(map.get("ItemDetail"));
        return itemView;
    }*/


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