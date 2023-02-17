package com.pikachu.utils.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.pikachu.utils.utils.AppManagerUtils;
import com.pikachu.utils.utils.LogsUtils;
import com.pikachu.utils.utils.ToastUtils;
import com.pikachu.utils.utils.UiUtils;
import com.pikachu.utils.utils.ViewBindingUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * build 加
 *
 *
 * buildFeatures {
 * viewBinding true
 * }
 *
 *
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity{
    public final static String START_STR = "PIKACHU";
    protected static final String TAG = "---" + BaseActivity.class.getCanonicalName();
    protected Context context;
    protected T binding;

    private Intent intent;
    public AppManagerUtils appManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(this));
        setContentView(binding.getRoot());
        context = this;
        appManager = AppManagerUtils.getAppManager();
        initActivity(savedInstanceState);
    }


    protected abstract void initActivity(Bundle savedInstanceState);


    public void showToast(String msg) {
        ToastUtils.showToast(context, msg);
    }
    public void showToast(Object msg) {
        ToastUtils.showToast(context, msg);
    }


    public void showLog(String... msg) {
        LogsUtils.showLog(context, LogsUtils.forStr(msg));
    }
    public void showLog(Object... msg) {
        List<String> strings = new ArrayList<>();
        for (Object f : msg)
            strings.add(f + "");
        LogsUtils.showLog(context, LogsUtils.forStr(strings.toArray(new String[]{})));
    }





    /////////////////////////////////////// 状态栏 /////////////////////////////////////////////////

    /**
     * 重写此处 更改状态栏颜色
     * <!-- 导入包 -->
     * implementation 'com.gyf.immersionbar:immersionbar:3.0.0'
     * implementation 'com.gyf.immersionbar:immersionbar-components:3.0.0' // fragment快速实现（可选）
     *
     * <!-- 地址 -->
     * https://github.com/gyf-dev/ImmersionBar
     *
     * <!-- 全面屏 -->
     * 在manifest的Application节点中加入
     * android:resizeableActivity="true"
     *
     * <!--适配华为（huawei）刘海屏-->
     * <meta-data
     * android:name="android.notch_support"
     * android:value="true"/>
     *
     * <!--适配小米（xiaomi）刘海屏-->
     * <meta-data
     * android:name="notch.config"
     * android:value="portrait|landscape" />
     *
     * <!--androidx-->
     * android.enableJetifier=true
     *
     * @return ImmersionBar
     */
    protected ImmersionBar setImmersionBar() {
        return ImmersionBar.with(this)
                .barColor(android.R.color.white)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true); // 解决布局与状态栏重叠问题
    }

    /**
     * 设置状态栏为白色  字体为黑色
     */
    protected void setWindowBlack() {
        ImmersionBar.with(this)
                .barColor(android.R.color.white)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true) // 解决布局与状态栏重叠问题
                .init();

    }

    /**
     * 自定义颜色  并且设置字体为黑色
     */
    protected void setWindowBlack(@ColorRes int barColor, @ColorRes int navColor ) {
        ImmersionBar.with(this)
                .barColor(barColor)
                .navigationBarColor(navColor)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true) // 解决布局与状态栏重叠问题
                .init();

    }




    /**
     * 设置状态栏为黑色 字体为白色
     */
    protected void setWindowWhite() {
        ImmersionBar.with(this)
                .barColor(android.R.color.black)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(true)
                .init();
    }


    /**
     * 自定义颜色  并且设置字体为白色
     */
    protected void setWindowWhite(@ColorRes int barColor, @ColorRes int navColor ) {
        ImmersionBar.with(this)
                .barColor(barColor)
                .navigationBarColor(navColor)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(true)
                .init();
    }



    //设置 状态栏字体 导航栏图标 为黑色
    protected void setWindowTextBlack() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true) //  true 深色
                .fitsSystemWindows(true)
                .navigationBarDarkIcon(true)
                .init();
    }

    //设置 状态栏字体 导航栏图标 为白色
    protected void setWindowTextWhite() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(false)
                .fitsSystemWindows(true)
                .init();
    }


    //设置 全屏
    protected void setWindowFullScreen() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }


    //设置 显示状态栏
    protected void setWindowNoFullScreen() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .hideBar(BarHide.FLAG_SHOW_BAR).init();
    }


    //设置 显示状态栏并且布局延伸至状态栏
    protected void setNoFullViewToWindow() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .init();
    }

    //设置 显示状态栏并且布局延伸至状态栏
    protected void setNoFullViewToWindow(@ColorRes int navColor, boolean barDark) {
        ImmersionBar.with(this)
                .navigationBarColor(navColor)
                .statusBarDarkFont(barDark) //  true 深色
                .fitsSystemWindows(false)
                .init();
    }
    //设置 显示状态栏并且布局不延伸至状态栏
    protected void setNoFullViewToWindow(@ColorRes int barColor, @ColorRes int navColor, boolean barDark, boolean fits) {
        ImmersionBar.with(this)
                .barColor(barColor)
                .navigationBarColor(navColor)
                .statusBarDarkFont(barDark) //  true 深色
                .fitsSystemWindows(fits)
                .init();
    }





    //设置 布局占用状态栏
    protected void setViewToWindow() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .init();
    }


/////////////////////////////////////// 状态栏 /////////////////////////////////////////////////


/////////////////////////////////////// 数据获取 /////////////////////////////////////////////////


    /**
     * 页面跳转
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }


    /**
     * 带数据页面跳转 序列化  所有类实现 Serializable
     */
    protected void startActivity(Class<?> clz, Serializable cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转  int
     */
    protected void startActivity(Class<?> clz, int cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转 string
     */
    protected void startActivity(Class<?> clz, String cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转 float
     */
    protected void startActivity(Class<?> clz, float cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }


    /**
     * 带数据页面跳转 long
     */
    protected void startActivity(Class<?> clz, long cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }


    /**
     * 简单的  Activity 接收信息
     */
    public String getStringExtra(String key) {
        intent = getIntent();
        return intent.getStringExtra(key);
    }

    public int getIntExtra(String key, int defaultValue) {
        intent = getIntent();
        return intent.getIntExtra(key, defaultValue);
    }


    public long getLongExtra(String key, int defaultValue) {
        intent = getIntent();
        return intent.getLongExtra(key, defaultValue);
    }

    public float getFloatExtra(String key, float defaultValue) {
        intent = getIntent();
        return intent.getFloatExtra(key, defaultValue);
    }

    public boolean getBooleanExtra(String key, boolean defaultValue) {
        intent = getIntent();
        return intent.getBooleanExtra(key, defaultValue);
    }

    public <t extends Serializable> t getSerializableExtra(String key, Class<t> cls) {
        return cls.cast(getIntent().getSerializableExtra(key));
    }





    /////////////////   回传跳转 /////////////////////////////////////
    /**
     * 回传跳转   过时
     * @param clz 界面
     * @param req 回传码
     * @param tag 值
     *            继承Serializable的对象
     *            可以是 Integer(int)   Float(float)  String  和自定义的
     *
     *            要实现  onActivityResult 方法
     */
    @Deprecated
    public void startActivityForResult(Class<?> clz, int req, Serializable tag){
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, tag);
        startActivityForResult(intent, req);
    }

    public interface ActivityResult<O extends Serializable>{
        void onResult(int resCode, O data);
    }

    /**
     * 回传跳转
     * @param clz  界面
     * @param callback 回传接口
     * @param <P>
     *           继承Serializable的对象
     *           可以是 Integer(int)   Float(float)  String  和自定义的
     */
    public <P extends Serializable> void startActivityForResult(Class<?> clz, ActivityResult<P> callback){
        ActivityResultLauncher<Intent> intentActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Intent data = result.getData();
                            Serializable serializableExtra = null;
                            if (data != null) {
                                serializableExtra = data.getSerializableExtra(START_STR);
                            }
                            P o = null;
                            if (serializableExtra != null){
                                try {
                                    o = ((P) serializableExtra);
                                }catch (Exception exception){
                                    exception.printStackTrace();
                                }
                            }
                            callback.onResult(result.getResultCode(), o);
                        });
        Intent intent = new Intent(context, clz);
        intentActivityResultLauncher.launch(intent);
    }

    /**
     * 回传关闭界面
     * @param tag  值
     *             继承Serializable的对象
     *             可以是 Integer(int)   Float(float)  String  和自定义的
     */
    public void finish(Serializable tag) {
        finish(100, tag);
    }

    /**
     * 回传关闭界面
     * @param req  回传码
     * @param tag  值
     *             继承Serializable的对象
     *             可以是 Integer(int)   Float(float)  String  和自定义的
     */
    public void finish(int req, Serializable tag) {
        Intent intent = getIntent();
        intent.putExtra(START_STR, tag);
        setResult(req, intent);
        finish();
    }
    /////////////////   回传跳转 /////////////////////////////////////


    /**
     * 关闭指定界面
     * @param cls 已开界面
     */
    public void finish(Class<?> cls) {
        appManager.finishActivity(cls);
    }









    public String getStringExtra() {
        return getStringExtra(START_STR);
    }

    public int getIntExtra() {
        return getIntExtra(START_STR, 0);
    }

    public long getLongExtra() {
        return getLongExtra(START_STR, 0);
    }

    public float getFloatExtra() {
        return getFloatExtra(START_STR, 0);
    }

    public boolean getBooleanExtra() {
        return getBooleanExtra(START_STR, false);
    }
    public <t extends Serializable> t getSerializableExtra(Class<t> cls) {
        return getSerializableExtra(START_STR, cls);
    }








}
