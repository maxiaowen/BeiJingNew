package com.atguigu.beijingnew.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by Administrator on 2017/6/7.
 */

public class BitmapCacheUtils {

    /**
     * 网络缓存工具类
     */

    private NetCachUtils netCachUtils;

    public BitmapCacheUtils(Handler handler){
        netCachUtils = new NetCachUtils(handler);
    }

    /**
     * 三级缓存设计步骤：
       * 从内存中取图片
       * 从本地文件中取图片
            向内存中保持一份
       * 请求网络图片，获取图片，显示到控件上
          * 向内存存一份
          * 向本地文件中存一份

     * @param imageUrl
     * @param position
     * @return
     */
    public Bitmap getBitmap(String imageUrl, int position) {
        // 从内存中取图片

        //从本地文件中取图片

        //请求网络图片，获取图片，显示到控件上
        netCachUtils.getBitmapFromNet(imageUrl,position);

        return null;
    }
}
