package com.atguigu.beijingnew.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.base.MenuDetailBasePager;
import com.atguigu.beijingnew.domain.NewsCenterBean;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    /**
     * TabDetailPager的对应的数据
     */
    private final List<NewsCenterBean.DataBean.ChildrenBean> datas;
    private ViewPager viewpager;
    private TabPageIndicator indicator;
    /**
     * TabDetailPager页面集合
     */
    private List<TabDetailPager> tabDetailPagers;



    public NewsMenuDetailPager(Context context, List<NewsCenterBean.DataBean.ChildrenBean> children) {
        super(context);
        this.datas = children;//12
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_news_menu_detail,null);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        //创建子类的视图
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //根据数据创建子页面
        tabDetailPagers = new ArrayList<>();
        for(int i = 0; i < datas.size(); i++) {
            //一会再传递数据
            tabDetailPagers.add(new TabDetailPager(context,datas.get(i)));
        }

        //设置适配器
        viewpager.setAdapter(new NewsMenuDetailPagerAdapter());

        //TabPageIndicator和ViewPager关联起来
        indicator.setViewPager(viewpager);
    }

    class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return tabDetailPagers == null ? 0 : tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 得到标题
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return datas.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();
            return rootView;
        }
    }
}
