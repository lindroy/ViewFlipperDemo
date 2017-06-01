package com.lindroid.viewflipperdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MarqueeNewsActivity extends AppCompatActivity {

    private Context context;
    private ViewFlipper viewFlipper;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee_news);
        context = this;
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        initData();
        setViews();
    }

    /**
     * 初始化新闻标题数据
     */
    private void initData() {
        titles = new ArrayList();
        titles.add("日本老年犯罪严重，监狱成老人无奈归宿");
        titles.add("美机闯香港空域");
        titles.add("大学教授亮工资条");
        titles.add("英国空军轰炸IS");
        titles.add("三连败收官！柯洁再负AlphaGo，人机大战遭零封");
        titles.add("女子爬山遭雷劈晕");
        titles.add("电信诈骗现新骗局");
    }

    /**
     * 为每一页设置视图
     */
    private void setViews() {
        int viewNum;
        if (titles.size() > 0) {
            //计算ViewFlipper视图的数目
            viewNum = titles.size() / 2 + 1;
            for (int i = 0; i < viewNum; i++) {
                //每一个视图的第一个新闻标题中集合中的下标值
                final int position = i * 2;
                View itemView = View.inflate(context, R.layout.title_view, null);
                TextView tvTitle1 = (TextView) itemView.findViewById(R.id.tv_title1);
                TextView tvTitle2 = (TextView) itemView.findViewById(R.id.tv_title2);
                LinearLayout ll = (LinearLayout) itemView.findViewById(R.id.ll_second);
                tvTitle1.setText(titles.get(position));

                //判断第二行是否有数据
                if (position + 1 < titles.size()) {
                    tvTitle2.setText(titles.get(position + 1));
                } else {
                    //表示该视图的第二个标题没有数据，隐藏第二行布局
                    ll.setVisibility(View.GONE);
                }

                //标题1的点击事件
                tvTitle1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, titles.get(position), Toast.LENGTH_SHORT).show();
                    }
                });

                //标题2的点击事件
                tvTitle2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, titles.get(position + 1), Toast.LENGTH_SHORT).show();
                    }
                });

                viewFlipper.addView(itemView);
            }
            //视图进入动画
            viewFlipper.setInAnimation(context, R.anim.news_in);
            //视图退出动画
            viewFlipper.setOutAnimation(context, R.anim.news_out);
            //自动开始滚动
            viewFlipper.setAutoStart(true);
            //视图的切换间隔
            viewFlipper.setFlipInterval(3000);
//            viewFlipper.startFlipping();
        }
    }
}
