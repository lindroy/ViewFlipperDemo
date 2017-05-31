package com.lindroid.viewflipperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private ViewFlipper viewFlipper;
    //要添加的页面布局ID
    private int viewIds[] = {R.layout.item_view1, R.layout.item_view2, R.layout.item_view3, R.layout.item_view4};
    private float startX; //手指按下时的x坐标
    private float endX; //手指抬起时的x坐标
    private float moveX = 100f; //判断是否切换页面的标准值
    private GestureDetector gestureDetector; //创建手势监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewFlipper();
//        addViews();
    }

    private void initViewFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyGestureListener());
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

    /**
     * 自定义手势监听类
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > moveX) {
                viewFlipper.setInAnimation(MainActivity.this, R.anim.left_in);
                viewFlipper.setOutAnimation(MainActivity.this, R.anim.right_out);
                viewFlipper.showPrevious();
            } else if (e2.getX() - e1.getX() < moveX) {
                viewFlipper.setInAnimation(MainActivity.this, R.anim.right_in);
                viewFlipper.setOutAnimation(MainActivity.this, R.anim.left_out);
                viewFlipper.showNext();
            }
            return true;
        }
    }


    /**
     * 触摸监听事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //手指按下时获取起始点坐标
//                startX = event.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                //手指抬起时获取结束点坐标
//                endX = event.getX();
//                //比较startX和endX，判断手指的滑动方向
//                if (endX - startX > moveX) { //手指从左向右滑动
//                    viewFlipper.setInAnimation(this, R.anim.left_in);
//                    viewFlipper.setOutAnimation(this, R.anim.right_out);
//                    viewFlipper.showPrevious();
//                } else if (startX - endX > moveX) { //手指向右向左滑动
//                    viewFlipper.setInAnimation(this, R.anim.right_in);
//                    viewFlipper.setOutAnimation(this, R.anim.left_out);
//                    viewFlipper.showNext();
//                }
//                break;
//        }
        return true;
    }

}
