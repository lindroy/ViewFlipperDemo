package com.lindroid.viewflipperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    //要添加的页面布局ID
    private int viewIds[] = {R.layout.item_view1, R.layout.item_view2, R.layout.item_view3, R.layout.item_view4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewFlipper();
//        addViews();
    }

    private void initViewFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
//        viewFlipper.setOnTouchListener(this);
    }

    /**
     * 将页面添加进ViewFlipper
     */
    private void addViews() {
        View itemView;
        for (int viewId : viewIds) {
            itemView = View.inflate(this, viewId, null);
            viewFlipper.addView(itemView);
        }
    }

}
