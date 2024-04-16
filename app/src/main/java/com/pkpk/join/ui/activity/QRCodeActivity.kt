package com.pkpk.join.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.king.zxing.CameraScan
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.pkpk.join.R
import com.google.zxing.Result
import com.gyf.immersionbar.ImmersionBar
import com.king.zxing.util.CodeUtils
import com.pkpk.join.base.AppBaseActivity
import com.pkpk.join.bean.BaseEventBean
import com.pikachu.utils.base.BaseActivity
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.ToastUtils
import com.pikachu.utils.utils.UiUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

const val QRCodeEvt = 102
class QRCodeActivity : CaptureActivity(), PhotoActivity.PhotoChooseListener {


    private var isWer = false
    private var longTime = 0L
    private var longTimeSrl = "asd_fgh_jkl_zxc_vbn_mbd_1o3_qwe_1tdf_sd2"
    private var cloudPws: String? = null

    private  lateinit var qrcImage: View
    private  lateinit var qrcSearch: View


    companion object {
        /**
         * @param cloudPws 密码 == null 设置模式
         */
        fun startActivity(activity: Activity, cloudPws: String? = null) {
            activity.startActivity(Intent(activity, QRCodeActivity::class.java).apply {
                putExtra(JumpType.J0, cloudPws)
            })
            activity.overridePendingTransition(R.anim.aty_in, R.anim.aty_ont)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_qrcode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppBaseActivity.activityStack.add(this)
        EventBus.getDefault().register(this)
        cloudPws = intent.getStringExtra(JumpType.J0)

        findViewById<View>(R.id.app_back)?.setOnClickListener {
            LoginActivity.finishTs(this)
        }
        ImmersionBar.with(this)
            .statusBarDarkFont(false)
            .fitsSystemWindows(false)
            .navigationBarDarkIcon(true)
            .navigationBarColor(R.color.black)
            .init()
        findViewById<View>(R.id.app_status)?.let {
            it.layoutParams.height = UiUtils.getStatusBarHeight(this)
        }

        qrcImage = findViewById(R.id.qrc_image)
        qrcSearch = findViewById(R.id.qrc_search)

        qrcImage.setOnClickListener {
            PhotoActivity.goPhotoImage(this, 1, 4, 1, this)
        }
        qrcSearch.setOnClickListener {
            startActivity(Intent(this, SearchGroupActivity::class.java))
            LoginActivity.finishTs(this)
        }
        cloudPws?.let {
            qrcSearch.visibility = View.GONE
        }
    }


    override fun initCameraScan() {
        super.initCameraScan()
        //初始化解码配置
        val decodeConfig = DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS) //如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
            .setFullAreaScan(false) //设置是否全区域识别，默认false
            .setAreaRectRatio(0.8f) //设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
            .setAreaRectVerticalOffset(0).areaRectHorizontalOffset =
            0 //设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        cameraScan
            .setVibrate(false) // 设置是否震动，默认为false
            .setNeedAutoZoom(false) // 二维码太小时可自动缩放，默认为false
            .setNeedTouchZoom(true)
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig)) //设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现
    }

    /**
     * 扫码结果回调
     * @param result
     * @return 返回false表示不拦截，将关闭扫码界面并将结果返回给调用界面；
     * 返回true表示拦截，需自己处理逻辑。当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
     * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调
     * 用[CameraScan.setAnalyzeImage]，
     * 因为[CameraScan.setAnalyzeImage]方法能动态控制是否继续分析图像。
     */
    override fun onScanResultCallback(result: Result?): Boolean {
        if (isWer) return true
        if (result != null) {
            isWer = true
            var groupId = 0L
            var pws = ""
            if(cloudPws != null){
                pws = QRCodeDisplayActivity.parseQrStrPws(result.text)
                if (pws == "") {
                    isWer = false
                    if (System.currentTimeMillis() - longTime >= 3000L || longTimeSrl != result.text){
                        ToastUtils.showToast(getString(R.string.activity_sqr_nul))
                    }
                    longTime = System.currentTimeMillis()
                    longTimeSrl = result.text
                    return true
                }
            } else {
                groupId = QRCodeDisplayActivity.parseQrStr(result.text)
                if (groupId <= 0) {
                    isWer = false
                    if (System.currentTimeMillis() - longTime >= 3000L || longTimeSrl != result.text){
                        ToastUtils.showToast(getString(R.string.activity_sqr_nul))
                    }
                    longTime = System.currentTimeMillis()
                    longTimeSrl = result.text
                    return true
                }
            }
            if(cloudPws != null){
                EventBus.getDefault().post(BaseEventBean(pws, QRCodeEvt, null, null))
                LoginActivity.finishTs(this@QRCodeActivity)
            } else {
                startActivity(Intent(this@QRCodeActivity, GroupInfoActivity::class.java).apply {
                    putExtra(BaseActivity.START_STR, groupId)
                })
            }
            return false
        }
        return true
    }


    /**
     * 图片选择回调
     */
    override fun onChooseClick(files: MutableList<String>?, num: Int) {
        files?:return
        val parseQRCode = CodeUtils.parseQRCode(BitmapFactory.decodeFile(files[0]))
        if(cloudPws != null){
            val pws = QRCodeDisplayActivity.parseQrStrPws(parseQRCode)
            if (pws == "") {
                ToastUtils.showToast(getString(R.string.activity_sqr_nul))
                return
            }
            EventBus.getDefault().post(BaseEventBean(pws, QRCodeEvt, null, null))
            LoginActivity.finishTs(this@QRCodeActivity)
        } else {
            val groupId = QRCodeDisplayActivity.parseQrStr(parseQRCode)
            if (groupId <= 0) {
                ToastUtils.showToast(getString(R.string.activity_sqr_nul))
                return
            }
            startActivity(Intent(this@QRCodeActivity, GroupInfoActivity::class.java).apply {
                putExtra(BaseActivity.START_STR, groupId)
            })
        }
        //finish()
    }


    @kotlin.jvm.Throws
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun baseEventBus(event: BaseEventBean<String>?) { }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoginActivity.finishTs(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        AppBaseActivity.activityStack.remove(this)
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}