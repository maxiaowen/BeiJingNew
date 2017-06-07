package com.atguigu.beijingnew.detailpager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.adapter.PhotosMenuDetailPagerAdapater;
import com.atguigu.beijingnew.base.MenuDetailBasePager;
import com.atguigu.beijingnew.domain.NewsCenterBean;
import com.atguigu.beijingnew.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnew.utils.ConstantUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String url;

    private PhotosMenuDetailPagerAdapater adapater;


    /**
     * 图组的数据
     */
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_photos_menu_detail, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        ButterKnife.inject(this,view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getDataFromNet(url);
            }
        });

        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //联网请求
        url = ConstantUtils.BASE_URL + dataBean.getUrl();
        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "图组请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "图组请求成功==" + response);
                        processData(response);



                    }


                });
    }

    private void processData(String json) {

        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
        datas = bean.getData().getNews();

        if(datas != null && datas.size() >0){
            //有数据
            progressbar.setVisibility(View.GONE);
            adapater = new PhotosMenuDetailPagerAdapater(context,datas);
            //设置适配器
            recyclerview.setAdapter(adapater);

            //布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        }else{
            //没有数据
            progressbar.setVisibility(View.VISIBLE);
        }

        //结束下拉刷新
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * true:显示List效果
     * false:显示Grid
     */
    private boolean isShowList = true;

    public void swichListAndGrid(ImageButton iv) {
        if(isShowList){
            //显示Grid效果
            recyclerview.setLayoutManager(new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false));
            isShowList = false;
            //按钮状态-List
            iv.setImageResource(R.drawable.icon_pic_list_type);
//            adapater.notifyItemChanged(0,datas.size());
        }else{
            //显示List
            //布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            isShowList = true;
            //按钮状态-Grid
            iv.setImageResource(R.drawable.icon_pic_grid_type);
//            adapater.notifyItemChanged(0,datas.size());
        }
    }
}
