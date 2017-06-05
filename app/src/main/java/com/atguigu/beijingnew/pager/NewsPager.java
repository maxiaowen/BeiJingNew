package com.atguigu.beijingnew.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnew.activity.MainActivity;
import com.atguigu.beijingnew.base.BasePager;
import com.atguigu.beijingnew.base.MenuDetailBasePager;
import com.atguigu.beijingnew.detailpager.InteractMenuDetailPager;
import com.atguigu.beijingnew.detailpager.NewsMenuDetailPager;
import com.atguigu.beijingnew.detailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnew.detailpager.TopicMenuDetailPager;
import com.atguigu.beijingnew.detailpager.VoteMenuDetailPager;
import com.atguigu.beijingnew.domain.NewsCenterBean;
import com.atguigu.beijingnew.fragment.LeftMenuFragment;
import com.atguigu.beijingnew.utils.CacheUtils;
import com.atguigu.beijingnew.utils.ConstantUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/2.
 */

public class NewsPager extends BasePager {

    /**
     * 左侧页面的数据集合
     */
    private List<NewsCenterBean.DataBean> datas;
    /**
     * 左侧菜单详情的页面集合
     */
    private List<MenuDetailBasePager> basePagers;


    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上
        Log.e("TAG","NewsPager-数据初始化...");
        //设置标题
        tv_title.setText("新闻");

        ib_menu.setVisibility(View.VISIBLE);

        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        fl_content.addView(textView);

        //获取数据
        String saveJson = CacheUtils.getString(context, ConstantUtils.NEWSCENTER_PAGER_URL);//
        if(!TextUtils.isEmpty(saveJson)) {//当不是null,""
            processData(saveJson);
            Log.e("TAG", "取出缓存的数据..==" + saveJson);
        }
        //联网请求
        getDataFromNet();
    }

    private void getDataFromNet() {
        //新闻中心的网络路径
        String url = ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","请求失败=="+e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","请求成功=="+response);
                        //缓存数据
                        CacheUtils.putString(context,ConstantUtils.NEWSCENTER_PAGER_URL,response);
                        processData(response);
                    }


                });
    }

    private void processData(String json) {
//        NewsCenterBean newsCenterBean = new Gson().fromJson(json,NewsCenterBean.class);
        NewsCenterBean newsCenterBean = paseJson(json);

        Log.e("TAG","解析成功了哦=="+ newsCenterBean.getData().get(0).getChildren().get(0).getTitle());

        datas = newsCenterBean.getData();

        //传到左侧菜单
        MainActivity mainActivity = (MainActivity) context;

        //实例化详情页面
        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(context,datas.get(0).getChildren()));//新闻详情页面
        basePagers.add(new TopicMenuDetailPager(context));//专题详情页面
        basePagers.add(new PhotosMenuDetailPager(context));//组图详情页面
        basePagers.add(new InteractMenuDetailPager(context));//互动详情页面
        basePagers.add(new VoteMenuDetailPager(context));//投票详情页面


        //得到左侧菜单Fragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(datas);


    }

    private NewsCenterBean paseJson(String json) {
        NewsCenterBean newsCenterBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            newsCenterBean.setRetcode(retcode);

            JSONArray data = jsonObject.optJSONArray("data");
            if(data != null && data.length()>0) {
                List<NewsCenterBean.DataBean> dataBeen = new ArrayList<>();
                newsCenterBean.setData(dataBeen);
                for(int i = 0; i < data.length(); i++) {

                    JSONObject jsonObject1 = (JSONObject) data.get(i);
                    if(jsonObject1 != null) {
                        NewsCenterBean.DataBean newdataBean = new NewsCenterBean.DataBean();
                        newdataBean.setId(jsonObject1.optInt("id"));
                        newdataBean.setTitle(jsonObject1.optString("title"));
                        newdataBean.setType(jsonObject1.optInt("type"));
                        newdataBean.setUrl(jsonObject1.optString("url"));
                        dataBeen.add(newdataBean);

                        JSONArray children = jsonObject1.optJSONArray("children");
                        if(children != null && children.length()>0) {
                            List<NewsCenterBean.DataBean.ChildrenBean> childrenBeen = new ArrayList<>();
                            newdataBean.setChildren(childrenBeen);
                            for(int j = 0; j < children.length(); j++) {
                              JSONObject jsonObject2 = (JSONObject) children.get(j);
                                if(jsonObject2 != null) {
                                    NewsCenterBean.DataBean.ChildrenBean childrenBeen2 = new NewsCenterBean.DataBean.ChildrenBean();
                                    childrenBeen2.setId(jsonObject2.optInt("id"));
                                    childrenBeen2.setTitle(jsonObject2.optString("title"));
                                    childrenBeen2.setType(jsonObject2.optInt("type"));
                                    childrenBeen2.setUrl(jsonObject2.optString("url"));
                                    childrenBeen.add(childrenBeen2);

                                }
                            }

                        }

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newsCenterBean;
    }

    /**
     * 根据位置切换到不同的详情页面
     * @param prePosition
     */
    public void swichPager(int prePosition) {

        MenuDetailBasePager basePager = basePagers.get(prePosition);//NewsMenuDetailPager,TopicMenuDetailPager...
        View rootView = basePager.rootView;
        fl_content.removeAllViews();//把之前显示的给移除

        fl_content.addView(rootView);

        //调用InitData
        basePager.initData();

    }
}
