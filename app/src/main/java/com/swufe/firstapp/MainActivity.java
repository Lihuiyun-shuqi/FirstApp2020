package com.swufe.firstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView out1,out2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out1 = (TextView)findViewById(R.id.textView2);
        out2 = (TextView)findViewById(R.id.textView4);
    }

    public void btn(View btn){
        int score1 = Integer.parseInt(out1.getText().toString());
        int score2 = Integer.parseInt(out2.getText().toString());
        if(btn.getId()==R.id.button1){
            int t = score1 + 3;
            String str = String.valueOf(t);
            out1.setText(str);
        }else if(btn.getId()==R.id.button2){
            int t = score1 + 2;
            String str = String.valueOf(t);
            out1.setText(str);
        }else if(btn.getId()==R.id.button3){
            int t = score1 + 1;
            String str = String.valueOf(t);
            out1.setText(str);
        }else if(btn.getId()==R.id.button4){
            int t = score2 + 3;
            String str = String.valueOf(t);
            out2.setText(str);
        }else if(btn.getId()==R.id.button5){
            int t = score2 + 2;
            String str = String.valueOf(t);
            out2.setText(str);
        }else if(btn.getId()==R.id.button6){
            int t = score2 + 1;
            String str = String.valueOf(t);
            out2.setText(str);
        }else if(btn.getId()==R.id.button7){
            int t = 0;
            String str = String.valueOf(t);
            out1.setText(str);
            out2.setText(str);
        }
    }

}