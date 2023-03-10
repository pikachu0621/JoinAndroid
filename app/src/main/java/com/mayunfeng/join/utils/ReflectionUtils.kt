package com.mayunfeng.join.utils

import java.lang.reflect.Field

/**
 *
 * @ProjectName:    考勤记录管理系统
 * @Package:        com.mayunfeng.join.utils
 * @Author:         pkpk.run
 * @Description:    null
 */
object ReflectionUtils {




    /**
     * 功能：通过反射 获取指定类对象中的 指定属性的值
     *
     * @param obj           类对象
     * @param propertyName  属性名
     * @param clazz         值的属性
     */
    fun <f> getAnyValue(obj: Any, propertyName: String, clazz: Class<f>): f? {
        try {
            val declaredField: Field = obj.javaClass.getDeclaredField(propertyName)
            declaredField.isAccessible = true
            return clazz.cast(declaredField.get(obj))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }





}