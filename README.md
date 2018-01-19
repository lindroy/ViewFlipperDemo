在淘宝App的首页中间位置，有一块小小的地方在不知疲倦地循坏滚动着头条标题（见下图的红框区域），这样的设计无疑能够在有限的手机屏幕上展示更丰富的内容。而实现这一功能需要用到的控件就是我在上一篇文章中提到的**ViewFlipper**控件（详见“参考文章”）。在网上看到一篇博客是用自定义ViewFlipper实现的，但我却想起了我在实现饿了么导航栏时的思路：既然ViewFlipper的每个视图最多只有两个新闻标题，那我们可以先将标题两两分组（奇数的话最后一个单独为一组），每组创建一个视图，这样就计算出了需要创建多少个视图，然后再在每个视图中加载数据就可以了。这样的话，直接用原生的ViewFlipper就可以做到了。

![淘宝头条示意图](http://img.blog.csdn.net/20170601105801869?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGluZHJvaWQyMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# 1、创建工程及布局
创建一个MarqueeNewsActivity，其布局文件如下
**activity_marquee_news.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="80dp"
    android:orientation="horizontal"
    tools:context="com.lindroid.viewflipperdemo.MarqueeNewsActivity">

    <TextView
        android:paddingLeft="10dp"
        android:paddingRight="10dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:layout_gravity="center_vertical"
        android:textSize="22sp"
        android:textColor="@android:color/holo_red_light"
        android:text="@string/title"/>

    <View
        android:layout_marginTop="8dp"
        android:layout_marginBottom="8dp"
        android:background="@android:color/darker_gray"
        android:layout_width="0.5dp"
        android:layout_height="match_parent"/>

    <ViewFlipper
        android:layout_marginLeft="10dp"
        android:id="@+id/viewFlipper"
        android:layout_gravity="center_vertical"
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="match_parent">

    </ViewFlipper>
</LinearLayout>
```
再创建ViewFlipper的视图布局，也就是新闻标题的布局。一共两个线性布局，每个放置一个新闻标题。由于第二个可能需要消失，所以设置ID。
**title_view.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:weightSum="2">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:gravity="center_vertical"
        android:padding="3dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="@drawable/shape"
            android:paddingBottom="3dp"
            android:paddingLeft="5dp"
            android:paddingRight="5dp"
            android:paddingTop="3dp"
            android:text="热点"
            android:textColor="@android:color/holo_red_light" />

        <TextView
            android:id="@+id/tv_title1"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:ellipsize="end"
            android:gravity="center_vertical"
            android:maxLines="1"
            android:paddingLeft="5dp" />

    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll_second"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:gravity="center_vertical"
        android:padding="3dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="@drawable/shape"
            android:paddingBottom="3dp"
            android:paddingLeft="5dp"
            android:paddingRight="5dp"
            android:paddingTop="3dp"
            android:text="热点"
            android:textColor="@android:color/holo_red_light" />

        <TextView
            android:id="@+id/tv_title2"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:ellipsize="end"
            android:gravity="center_vertical"
            android:maxLines="1"
            android:paddingLeft="5dp" />

    </LinearLayout>

</LinearLayout>
```
标签的背景就是一个红色的边框，也很简单：
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">

    <solid android:color="@android:color/white" />
    <stroke
        android:width="1dp"
        android:color="@android:color/holo_red_light" />

    <corners android:radius="3dp" />

</shape>
```
编写完布局之后，下面就来编写代码了。

# 2、Activity代码
代码都是按照前面说到的思路写的，我在必要的地方也加了注释，相信大家读起来不难理解。要注意的是新闻标题数目可能是奇数，这样最后一个视图就只有一个标题。为了美观，我们需要让第二个线性布局消失掉，所以要加一个判断条件。ViewFlipper的视图进入和退出是通过动画来设置的，大家也可以根据需要增加我们需要的动画效果。
**MarqueeNewsActivity**
```java
public class MarqueeNewsActivity extends AppCompatActivity {
    private Context context;
    private ViewFlipper viewFlipper;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (titles.size() > 0) {
            //计算ViewFlipper视图的数目
            int viewNum = titles.size() / 2 + 1;
            for (int i = 0; i < viewNum; i++) {
                //每一个视图的第一个新闻标题中集合中的下标值
                final int position = i * 2;
                View itemView = View.inflate(context, R.layout.item_view, null);
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
```
顺便放一下动画文件吧。
**new_in.xml**
```xml
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="500">
    <translate
        android:fromXDelta="0"
        android:fromYDelta="100%p"
        android:toXDelta="0"
        android:toYDelta="0" />
</set>
```
new_out.xml的代码类似，只需要将`fromYDelta`的值改为0，`toYDelta`的值改为-100%p就可以了。

效果如下：

![滚动式新闻标题](http://img.blog.csdn.net/20170601092608054?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGluZHJvaWQyMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# 3、分页思想
都说编程最重要的是思想，现在就来抽取和总结一下我学习到的思想，尽管看起来还是很稚嫩，但毕竟也是我前进路上的一个脚印。

无论是跑马灯新闻标题还是饿了么的导航栏，它们的作用都是一样的，那就是“复用”有限的屏幕空间，展示更为丰富的内容。它们有着以下的共同点：

 - 将一定数量的数据分配在多个视图（View）中展示；
 - 每个数据的类型都是一样的，比如饿了么导航栏中的每个数据单元就是一张图片和几个文字；
 - 每一个视图的内容虽然不一样，但每一个视图的布局都是一样的（消失或者隐藏的子布局也算在内）；
 - 每一个视图内的最小数据单元（item）的布局也是一样的。

打个比方，就好像我们在信纸上写作文一样，每一页的方格数目都是固定的，方格的大小也是一样的，当一页的方格用完后，就另起一页再写。其实这也有点像ListView的分页加载，可以将每一个视图都当成一页，一页放不下就一页一页地加载。这里实际上是复用了View的布局和item的布局了。

思想指导行动，遇到符合上面特征的数据时，我们就可以用分页的思想按照下面的步骤一步步来：

1. 编写页面视图（View）的布局和数据单元（item）的布局；
2. 计算item的个数；
3. 确定每一个View中的item数目，对item进行分组
4. 根据View中的item数目计算需要的View数目；
5. 创建每一个View（一般使用for循环）；
6. 将数据填充至每一个item中
7. 考虑item数目（比如单数与奇数）对View布局的影响。

大体的步骤就是这样了，实际运用中可能会稍有不同，不必太过拘泥，只要有这种思想就可以了。

# 4、后记
文章到这里就写完了，这点小小的总结希望能对大家有点帮助，也希望能抛砖引玉。最后补充一点，如果你需要在项目中多次用到这种效果的话，那么可以看看我在参考文章一节中列出的博客，使用自定义控件的方法来写，如果就一两处用到，那么用原生的ViewFlipper就足够了。

最后附上源码的GitHub地址：

[ViewFlipperDemo](https://github.com/Lindroy/ViewFlipperDemo "ViewFlipperDemo")

# 参考文章
[Android之ViewFlipper的简单使用](http://blog.csdn.net/lindroid20/article/details/72828040 "Android之ViewFlipper的简单使用")

[仿淘宝首页的淘宝头条View垂直滚动](http://blog.csdn.net/dreamlivemeng/article/details/51979650 "仿淘宝首页的淘宝头条View垂直滚动")

[仿饿了么首页导航栏（ViewPager）](http://blog.csdn.net/lindroid20/article/details/66968410 "仿饿了么首页导航栏（ViewPager）")



