package com.atguigu.beijingnew.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/6/3.
 */

public abstract class MenuDetailBasePager {
    /***
     * 代表子类的整个视图
     */
    public View rootView;

    public final Context context;

    public MenuDetailBasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**
     * 该方法是抽象的，由子类实现，达到自己的视图
     * @return
     */
    public abstract View initView();


    /**
     * 子类要绑定数据的时候重写该方法
     */
    public void initData(){

    }


}