package com.atguigu.beijingnew.detailpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.base.MenuDetailBasePager;
import com.atguigu.beijingnew.domain.NewsCenterBean;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/5.
 */

public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.lv)
    ListView lv;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        tvTitle.setText(childrenBean.getTitle());
    }
}
