package com.mayunfeng.join.utils

import com.mayunfeng.join.HTTP_OK
import com.mayunfeng.join.bean.BaseBean
import com.mayunfeng.join.utils.retrofit.RetrofitObserver

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils
 * @Author:         pkpk.run
 * @Description:    null
 */
abstract class  MyRetrofitObserver<T : Any> : RetrofitObserver<T>(BaseBean<Any>::error_code, HTTP_OK)