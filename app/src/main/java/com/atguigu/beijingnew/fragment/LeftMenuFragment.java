package com.atguigu.beijingnew.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.activity.MainActivity;
import com.atguigu.beijingnew.base.BaseFragment;
import com.atguigu.beijingnew.domain.NewsCenterBean;
import com.atguigu.beijingnew.pager.NewsPager;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    /**
     * 传入的数据
     */
    private List<NewsCenterBean.DataBean> datas;

    private ListView listView;
    private LeftMenuAdapter adapter;

    private int prePosition = 0;

    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0,40,0,0);

        //设置ListView的item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录位置
                prePosition = position;
                //适配器刷新
                adapter.notifyDataSetChanged();//getCount-->getView
                //1.得到MainActivity
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关<->开

                //根据位置切换到对应的详情页面
                switchPager(prePosition);

            }
        });

        return listView;
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;

        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);

        //根据位置切换到对应的详情页面
        switchPager(prePosition);
    }

    private void switchPager(int postion) {
        MainActivity mainActivity = (MainActivity) context;
        //2.得到ContentFragment
        ContentFragment contentFragment = mainActivity.getContentFragment();
        //3.得到NewsPager
        NewsPager newsPager = contentFragment.getNewsPager();
        //4.调用切换方法
        newsPager.swichPager(postion);
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas == null? 0: datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);

            if(prePosition==position){
                //高亮
                textView.setEnabled(true);
            }else{
                //默认
                textView.setEnabled(false);
            }

            //根据位置得到数据
            NewsCenterBean.DataBean dataBean = datas.get(position);
            textView.setText(dataBean.getTitle());
            return textView;
        }
    }
}
