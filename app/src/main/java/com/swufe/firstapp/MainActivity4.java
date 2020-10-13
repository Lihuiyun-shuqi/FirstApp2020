package com.swufe.firstapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private static final String TAG = "MainActivity4";
    ListView myList;
    Handler handler;
    ArrayList<HashMap<String,String>> dataList;
    MyAdapter myAdapter;

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
                    dataList = (ArrayList<HashMap<String,String>>) msg.obj;

                    myAdapter = new MyAdapter(MainActivity4.this,
                            R.layout.list_item,
                            dataList);//自定义适配器
                    myList.setAdapter(myAdapter);

                    myList.setOnItemClickListener(MainActivity4.this);//添加单击事件监听
                    myList.setOnItemLongClickListener(MainActivity4.this);//添加长按事件监听
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
        Object itemAtPosition1 = myList.getItemAtPosition(position);//获取ListView中点击的数据
        HashMap<String,String> map1 = (HashMap<String, String>) itemAtPosition1;
        String titleStr1 = map1.get("ItemTitle");
        String detailStr1 = map1.get("ItemDetail");
        Float curr_rate = Float.parseFloat(detailStr1);
        Log.i(TAG, "onItemClick: titleStr=" + titleStr1);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr1);

        Intent config_new = new Intent(this,MainActivity5.class);
        //传递参数
        Bundle bdl = new Bundle();
        bdl.putString("itemTitle",titleStr1);
        bdl.putFloat("itemDetail",curr_rate);
        config_new.putExtras(bdl);

        Log.i(TAG,"openOne:itemTitle="+titleStr1);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int pp = position;
        //长按处理
        Object itemAtPosition2 = myList.getItemAtPosition(position);//获取ListView中点击的数据
        HashMap<String,String> map2 = (HashMap<String, String>) itemAtPosition2;
        final String titleStr2 = map2.get("ItemTitle");
        final String detailStr2 = map2.get("ItemDetail");

        //AlertDialog对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" 提示")
                .setMessage(" 请确认是否删除当前数据 ")
                .setPositiveButton(" 是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick:  对话框事件处理");
                        //删除数据项
                        dataList.remove(pp);
                        //更新适配器
                        myAdapter.notifyDataSetChanged();
                        Log.i(TAG, "onItemClick: 删除的数据为：titleStr= " + titleStr2 + " , detailStr= " + detailStr2);
                    }
                }).setNegativeButton(" 否", null);
        builder.create().show();

        return true;//如果返回false，则会在松开点击鼠标后弹出单击事件的处理
    }
}