package com.atguigu.beijingnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnew.R;
import com.atguigu.beijingnew.activity.PicassoSampleActivity;
import com.atguigu.beijingnew.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnew.utils.BitmapCacheUtils;
import com.atguigu.beijingnew.utils.ConstantUtils;
import com.atguigu.beijingnew.utils.NetCachUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/6.
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {




    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;
    private final Context context;

    private final RecyclerView recyclerview;
    /**
     * 做图片三级缓存
     * 1.内存缓存
     * 2.本地缓存
     * 3.网络缓存
     */
    private BitmapCacheUtils bitmapCacheUtils;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCachUtils.SUCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    Log.e("TAG","请求图片成功=="+position);
                    ImageView imageview = (ImageView) recyclerview.findViewWithTag(position);
                    if(imageview != null && bitmap != null){
                        imageview.setImageBitmap(bitmap);
                    }


                    break;
                case NetCachUtils.FAIL:
                    position = msg.arg1;
                    Log.e("TAG","请求图片失败=="+position);
                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas, RecyclerView recyclerview) {
        this.datas = datas;
        this.context = context;
        this.recyclerview = recyclerview;

        //把Hanlder传入构造方法
        bitmapCacheUtils = new BitmapCacheUtils(handler);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_photos, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //1.根据位置得到对应的数据
        PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = datas.get(position);
        //2.绑定数据
        holder.tvTitle.setText(newsBean.getTitle());
        //3.设置点击事件
        String imageUrl = ConstantUtils.BASE_URL + newsBean.getListimage();
//        Glide.with(context)
//                .load(imageUrl)
//                .placeholder(R.drawable.pic_item_list_default)
//                .error(R.drawable.pic_item_list_default)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.ivIcon);
        //使用自定义请求图片
//        Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl,position);
//        //图片对应的Tag就是位置
//        holder.ivIcon.setTag(position);
//        if(bitmap != null){//来自内存和本地，不包括网络
//            holder.ivIcon.setImageBitmap(bitmap);
//        }

        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_pic_default) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.news_pic_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.news_pic_default)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .build();

        ImageLoader.getInstance().displayImage(imageUrl, holder.ivIcon,options);



    }

    /**
     * 返回总大小
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //跳转到显示图片的Activity页面，使用的PhotoView
                    String imageUrl = ConstantUtils.BASE_URL + datas.get(getLayoutPosition()).getListimage();
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(imageUrl));
                    context.startActivity(intent);
                }
            });
        }
    }
}
