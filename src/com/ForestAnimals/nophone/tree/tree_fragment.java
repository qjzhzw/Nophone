package com.ForestAnimals.nophone.tree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.util.MyThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MyWorld on 2016/6/12.
 */
public class tree_fragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollView_tree;
    private RelativeLayout relativeLayout_tree_rain;
    private ImageView imageView_tree,
            imageView_tree_leaf1,
            imageView_tree_leaf2,
            imageView_tree_leaf3,
            imageView_tree_leaf4,
            imageView_tree_cloud,
            imageView_tree_rain1,
            imageView_tree_rain2,
            imageView_tree_rain3;
    private Button button_tree_bag,
            button_tree_friend,
            button_tree_water,
            button_tree_hand;

    private Animation leaf1_anim,
            leaf2_anim,
            leaf3_anim,
            leaf4_anim,
            cloud_anim,
            rain1_anim,
            rain2_anim,
            rain3_anim;
    //动画集锦

    private EditText editText_tree_experiment;
    private Button button_tree_experiment;
    //试验效果

    int count, hour = 0, minute = 0, second = 0;
    int word_num = 0;

    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tree_layout, container,
                false);

        //声明ID集合
        scrollView_tree = (ScrollView) view.findViewById(R.id.scrollView_tree);
        relativeLayout_tree_rain = (RelativeLayout) view.findViewById(R.id.relativeLayout_tree_rain);
        imageView_tree = (ImageView) view.findViewById(R.id.imageView_tree);
        imageView_tree_leaf1 = (ImageView) view.findViewById(R.id.imageView_tree_leaf1);
        imageView_tree_leaf2 = (ImageView) view.findViewById(R.id.imageView_tree_leaf2);
        imageView_tree_leaf3 = (ImageView) view.findViewById(R.id.imageView_tree_leaf3);
        imageView_tree_leaf4 = (ImageView) view.findViewById(R.id.imageView_tree_leaf4);
        imageView_tree_cloud = (ImageView) view.findViewById(R.id.imageView_tree_cloud);
        imageView_tree_rain1 = (ImageView) view.findViewById(R.id.imageView_tree_rain1);
        imageView_tree_rain2 = (ImageView) view.findViewById(R.id.imageView_tree_rain2);
        imageView_tree_rain3 = (ImageView) view.findViewById(R.id.imageView_tree_rain3);
        button_tree_bag = (Button) view.findViewById(R.id.button_tree_bag);
        button_tree_friend = (Button) view.findViewById(R.id.button_tree_friend);
        button_tree_water = (Button) view.findViewById(R.id.button_tree_water);
        button_tree_hand = (Button) view.findViewById(R.id.button_tree_hand);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.tree_swiper);
        editText_tree_experiment = (EditText) view.findViewById(R.id.editText_tree_experiment);
        button_tree_experiment = (Button) view.findViewById(R.id.button_tree_experiment);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_orange_light));

        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams lp = imageView_tree.getLayoutParams();
        lp.width = screenWidth;
        lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        imageView_tree.setLayoutParams(lp);
        imageView_tree.setMaxWidth(screenWidth);
        imageView_tree.setMaxHeight(screenWidth * 5);
        //设置图片自适应宽高

        setAction();
        judge_level();//判断树是哪一级
        leaf_anim();//树叶会飘动

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setAction();
                        judge_level();//判断树是哪一级
                        leaf_anim();//树叶会飘动
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        return view;
    }


    private String[] connect() {
        SharedPreferences information = getActivity().getSharedPreferences("information", 0);
        String identification = information.getString("identification", "0");

        String url = "information/tree_water/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", identification));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"status"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }


    private String[] connect_level() {
        SharedPreferences information = getActivity().getSharedPreferences("information", 0);
        String identification = information.getString("identification", "0");

        String url = "information/tree/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", identification));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"level"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            Intent intent = new Intent();
            switch (button.getId()) {
                case R.id.button_tree_bag:
                    Toast.makeText(getActivity(), getString(R.string.not_finished), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_tree_friend:
                    Toast.makeText(getActivity(), getString(R.string.not_finished), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_tree_water:
                    if (word_num == 0) {
                        rain_anim();
                        connect();
                        Toast.makeText(getActivity(), getString(R.string.water_success), Toast.LENGTH_SHORT).show();
                        init();
                    } else {
                        Toast.makeText(getActivity(), count + "秒后可浇水", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.button_tree_hand:
                    Toast.makeText(getActivity(), getString(R.string.not_finished), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_tree_experiment:
                    int m = Integer.parseInt(editText_tree_experiment.getText().toString());
                    if (m <= 10) {
                        imageView_tree.setImageResource(R.drawable.tree_lv1);
                    }
                    if (m <= 100 && m > 10) {
                        imageView_tree.setImageResource(R.drawable.tree_lv2);
                    }
                    if (m <= 500 && m > 100) {
                        imageView_tree.setImageResource(R.drawable.tree_lv3);
                    }
                    break;
            }
        }
    };


    public void leaf_anim()
    //让树叶随风摆动吧
    {
        leaf1_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.leaf1_anim);
        leaf2_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.leaf2_anim);
        leaf3_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.leaf3_anim);
        leaf4_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.leaf4_anim);
        imageView_tree_leaf1.startAnimation(leaf1_anim);
        imageView_tree_leaf2.startAnimation(leaf2_anim);
        imageView_tree_leaf3.startAnimation(leaf3_anim);
        imageView_tree_leaf4.startAnimation(leaf4_anim);
        leaf1_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                leaf_anim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void rain_anim()
    //云和雨滴向右移动
    {
        cloud_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.cloud_anim);
        rain1_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rain1_anim);
        rain2_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rain2_anim);
        rain3_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rain3_anim);
        relativeLayout_tree_rain.startAnimation(cloud_anim);
        imageView_tree_rain1.startAnimation(rain1_anim);
        imageView_tree_rain2.startAnimation(rain2_anim);
        imageView_tree_rain3.startAnimation(rain3_anim);
        leaf1_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                leaf_anim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void judge_level()
    {
        String[] result = connect_level();
        int level = Integer.parseInt(result[0]);

        switch (level) {
            case 1:
                imageView_tree.setImageResource(R.drawable.tree_lv1);
                break;
            case 2:
                imageView_tree.setImageResource(R.drawable.tree_lv2);
                break;
            case 3:
                imageView_tree.setImageResource(R.drawable.tree_lv3);
                break;
            default:
                break;
        }
    }


    public void setAction()
    //设置事件集合
    {
        button_tree_bag.setOnClickListener(listener);
        button_tree_friend.setOnClickListener(listener);
        button_tree_water.setOnClickListener(listener);
        button_tree_hand.setOnClickListener(listener);
        button_tree_experiment.setOnClickListener(listener);

        Intent intent = new Intent();
        intent.setClass(getActivity(), tree_news.class);
        startActivity(intent);
    }


    private Timer mTimer;
    private TimerTask mTimerTask;

    private void init() {
        word_num = 1;
        hour = 1;
        minute = 0;
        second = 0;
        count = second + minute * 60 + hour * 60 * 60;

        mTimer = new Timer();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 1:
                        button_tree_water.setEnabled(true);
                        word_num = 0;
                }
                super.handleMessage(msg);
            }
        };

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                count--;
                System.out.println("---> count=" + count);
                if (count == 0) {
                    mTimer.cancel();
                    System.out.println("---> 取消定时任务");

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        };
        //开始一个定时任务
        mTimer.schedule(mTimerTask, 1000, 1000);
    }


}