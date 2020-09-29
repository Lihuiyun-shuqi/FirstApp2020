package com.swufe.javatest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class MyClass {
    public static void main(String[] args) {
        float new_dollar = 0;
        float new_euro = 0;
        float new_won = 0;
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            System.out.println(doc.charset());
            doc.charset(Charset.forName("ISO8859-1"));
            System.out.println(doc.charset());
            //doc = Jsoup.parse(new URL(url).openStream(),"gb2312",url);
            //String tit = new String(doc.text().getBytes("gb2312"),"UTF-8");
            //String ti = new String(doc.body().toString().getBytes("gbk"),"UTF-8");
            System.out.println(doc.title());


            Element table = doc.getElementsByTag("table").first();
            //获取TD中的数据
            Elements trs = table.getElementsByTag("tr");
            for(Element tr : trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size() > 0){
                    String td1 = tds.get(0).text();
                    String td2 = tds.get(5).text();
                    float v = 100f / Float.parseFloat(td2);
                    //System.out.println(td1 + "==>" + v);
                }
            }
                /*if(str1.equals("美元")){
                    new_dollar = v;
                }
                if(str1.equals("欧元")){
                    new_euro = v;
                }
                if(str1.equals("韩元")){
                    new_won = v;
                }*/
            /*System.out.println("美元 ==>" + new_dollar);
            System.out.println("欧元 ==>" + new_euro);
            System.out.println("韩元 ==>" + new_won);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}