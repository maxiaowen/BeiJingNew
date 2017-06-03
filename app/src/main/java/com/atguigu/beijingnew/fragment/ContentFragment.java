package com.atguigu.beijingnew.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnew.NoViewPager;
import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.activity.MainActivity;
import com.atguigu.beijingnew.base.BaseFragment;
import com.atguigu.beijingnew.base.BasePager;
import com.atguigu.beijingnew.pager.HomePager;
import com.atguigu.beijingnew.pager.NewsPager;
import com.atguigu.beijingnew.pager.SettingPager;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {

    @InjectView(R.id.vp)
    NoViewPager  vp;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> pagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //设ViewPager的数据-适配器
        //准备数据
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));//主页面
        pagers.add(new NewsPager(context));//新闻中心
        pagers.add(new SettingPager(context));//设置中心

        vp.setAdapter(new MyAdapter());

        //设置RadioGroup的监听
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        vp.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        vp.setCurrentItem(1,false);
                        break;
                    case R.id.rb_setting:
                        vp.setCurrentItem(2,false);
                        break;
                }
            }
        });

        vp.addOnPageChangeListener(new MyOnPageChangeListener());

        //默认选中主页
        rgMain.check(R.id.rb_home);
//        pagers.get(0).initData();
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagers.get(position).initData();

            if(position ==1){
                //可以侧滑
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().setTouchModeAbove( SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                //不可用侧滑
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().setTouchModeAbove( SlidingMenu.TOUCHMODE_NONE);


            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 得到新闻中心
     * @return
     */
    public NewsPager getNewsPager() {
        return (NewsPager) pagers.get(1);
    }


    class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);//HomePager,NewsPager,SettingPager
            View rootView = basePager.rootView;
            //调用initData方法
//            basePager.initData();//HomePager,NewsPager,SettingPager
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public int getCount() {
            return pagers.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
