package com.swufe.firstapp;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements Runnable{
    private static final String TAG = "MainActivity3";
    ListView myList;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        //获取ListView控件
        myList = (ListView) findViewById(R.id.mylist1);
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
                    List<HashMap<String,String>> dataList = (List<HashMap<String,String>>) msg.obj;
                    ListAdapter listItemAdapter = new SimpleAdapter(MainActivity3.this,
                            dataList,
                            R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail});
                    myList.setAdapter(listItemAdapter);
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

        //使用Jsoup解析网页数据
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Log.i(TAG,"run:" + doc.title());
            /*Elements tables = doc.getElementsByTag("table");
            Element table6 = tables.get(0);*/
            Element table = doc.getElementsByTag("table").first();//获取标签为table的第一个元素，即上面两句话的意思

            //获取TD中的数据
            //方法一
            Elements tds = table.getElementsByTag("td");
            List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
            for(int i = 0;i < tds.size();i += 6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);
                String str1 = td1.text();
                String val = td2.text();
                //Log.i(TAG,"run:" + str1 + "==>" + val);
                float v = 100f / Float.parseFloat(val);
                Log.i(TAG,"run:" + str1 + "==>" + v);
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",String.valueOf(v));
                list.add(map);
            }

            Message msg = handler.obtainMessage(5);
            msg.obj = list;
            handler.sendMessage(msg);


            //方法二
            /*Elements trs = table.getElementsByTag("tr");
            for(Element tr : trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size() > 0){
                    //get data
                    String td1 = tds.get(0).text();
                    String td2 = tds.get(5).text();
                    float v = 100f / Float.parseFloat(td2);
                    //float rate =(float)(Math.round(v*100))/100;//取两位小数
                    Log.i(TAG,"run:" + td1 + "==>" + v);
                    //获取数据并返回
                    if(td1.equals("美元")){
                        new_dollar = v;
                        Log.i(TAG,"run:" + td1 + "==>" + new_dollar);
                        dolRate.setText(String.valueOf(new_dollar));
                    }
                    if(td1.equals("欧元")){
                        new_euro = v;
                        Log.i(TAG,"run:" + td1 + "==>" + new_euro);
                        eurRate.setText(String.valueOf(new_euro));
                    }
                    if(td1.equals("韩元")){
                        new_won = v;
                        Log.i(TAG,"run:" + td1 + "==>" + new_won);
                        woRate.setText(String.valueOf(new_won));
                    }
                }
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}