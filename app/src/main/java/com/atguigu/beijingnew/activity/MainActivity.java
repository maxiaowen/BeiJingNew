package com.atguigu.beijingnew.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.fragment.ContentFragment;
import com.atguigu.beijingnew.fragment.LeftMenuFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_TAG = "left_tag";
    public static final String MAIN_TAG = "main_tag";

    String haha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initSlidingMenu();
        //初始化Fragment
        initFragment();

        System.out.println(haha.equals("hello"));  // s没有进行赋值，所以会出现NullPointException异常


    }

    private void initSlidingMenu() {
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu);


        SlidingMenu slidingMenu = getSlidingMenu();
        //设置右侧菜单
//        slidingMenu.setSecondaryMenu(R.layout.left_menu);

        //设置模式：左侧+主页；左侧+主页+右侧；主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);


        //设置滑动模式：不可用滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        //设置主页面占的宽度
        slidingMenu.setBehindOffset(200);
    }

    private void initFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换两个Fragment
        ft.replace(R.id.fl_left,new LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.fl_main,new ContentFragment(), MAIN_TAG);
        //4.提交事务
        ft.commit();

    }

    /**
     * 得到LeftMenuFragment
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {

        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_TAG);
    }

    /**
     * 得到ContentFragment
     * @return
     */
    public ContentFragment getContentFragment() {
        return  (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_TAG);
    }
}
