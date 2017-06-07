package com.atguigu.a02_androidandh5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        //设置支持javaScript脚步语言
        settings.setJavaScriptEnabled(true);
        //支持双击-前提是页面要支持才显示
        settings.setUseWideViewPort(true);
        //支持缩放按钮-前提是页面要支持才显示
        settings.setBuiltInZoomControls(true);

        //设置客户端-不跳转到默认浏览器中
        webView.setWebViewClient(new WebViewClient());

        //设置支持js调用java
        webView.addJavascriptInterface(new AndroidAndJSInterface(), "Android");

        //加载本地资源
//        webView.loadUrl("http://atguigu.com/teacher.shtml");
        webView.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");
//        webView.loadUrl("http://10.0.2.2:8080/assets/JsCallJavaCallPhone.html");

    }

    class AndroidAndJSInterface {

        @JavascriptInterface
        public void showcontacts() {
            // 下面的代码建议在子线程中调用
            String json = "[{\"name\":\"马晓文\", \"phone\":\"18600012345\"}]";
            // 调用JS中的方法
            webView.loadUrl("javascript:show('" + json + "')");
        }

//        /**
//         * 拨打电话
//         *
//         * @param phone
//         */
//        @JavascriptInterface
//        public void call(String phone) {
//
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
//
//            startActivity(intent);
//
//        }
    }


}
