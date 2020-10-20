package com.swufe.firstapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FrameActivity extends FragmentActivity {

    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbtA,rbtB,rbtC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragmentA);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragmentB);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragmentC);
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();

        rbtA = (RadioButton)findViewById(R.id.btn_fragmentA);
        rbtB = (RadioButton)findViewById(R.id.btn_fragmentB);
        rbtC = (RadioButton)findViewById(R.id.btn_fragmentC);

        radioGroup = (RadioGroup)findViewById(R.id.btnGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Log.i("radioGroup","checkedId = " + checkedId);

                fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
                switch (checkedId){
                    case R.id.btn_fragmentA:
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;
                    case R.id.btn_fragmentB:
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;
                    case R.id.btn_fragmentC:
                        fragmentTransaction.show(mFragments[2]).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}