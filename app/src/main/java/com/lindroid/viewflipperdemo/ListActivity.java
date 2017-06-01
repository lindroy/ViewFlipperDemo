package com.lindroid.viewflipperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    /**
     * Android之ViewFlipper的简单使用
     */
    public void normalClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void marqueeClick(View view) {

    }
}
