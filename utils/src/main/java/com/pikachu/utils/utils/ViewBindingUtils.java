package com.pikachu.utils.utils;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;


/**
 * user：利用反射获取ViewBinding   -keep class  包名.databinding.* {*;}
 */
public class ViewBindingUtils {

    public static <Binding extends ViewBinding> Binding create(Class<?> clazz, LayoutInflater inflater) {
        return create(clazz, inflater, null);
    }

    public static <Binding extends ViewBinding> Binding create(Class<?> clazz, LayoutInflater inflater, ViewGroup root) {
        return create(clazz, inflater, root, false);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <Binding extends ViewBinding> Binding create(Class<?> clazz, LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        Class<?> bindingClass = getBindingClass(clazz);
        Binding binding = null;
        if (bindingClass != null) {
            try {
                Method method = bindingClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                binding = (Binding) method.invoke(null, inflater, root, attachToRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Objects.requireNonNull(binding);
    }


    @NonNull
    public static <Binding extends ViewBinding> Binding createCls(Class<?> clazz, LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        Binding binding = null;
        if (clazz != null) {
            try {
                Method method = clazz.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                binding = (Binding) method.invoke(null, inflater, root, attachToRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Objects.requireNonNull(binding);
    }




    public static Class<?> getBindingClass(Class<?> clazz) {
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = Objects.requireNonNull(parameterizedType).getActualTypeArguments();
        Class<?> bindingClass = null;
        for (Type type : types) {
            if (type instanceof Class<?>) {
                Class<?> temp = (Class<?>) type;
                if (ViewBinding.class.isAssignableFrom(temp)) {
                    bindingClass = temp;
                }
            }
        }
        return bindingClass;
    }
}