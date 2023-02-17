package com.pikachu.utils.adapter;
import android.view.ViewGroup;
import androidx.viewbinding.ViewBinding;

import com.pikachu.utils.utils.ViewBindingUtils;

import java.util.List;

/**
 * @author pkpk.run
 * @project 通用工程
 * @package com.pikachu.utils.adapter
 * @date 2021/9/9
 * @description
 *
 * 快速 RecyclerViewAdapter
 * 不支持 多Item布局
 * 多Item布局请继承 {@link BaseAdapter}
 *
 * @param <V> 布局类
 * @param <T> 数据类
 */
public abstract class QuickAdapter<V extends ViewBinding, T > extends BaseAdapter<T> {


    public QuickAdapter(List<T> data) {
       super(data);
    }

    /**
     * 业务逻辑
     * @param binding 布局文件
     * @param itemData 当前 item 的数据
     * @param position 当前 item 的游标
     * @param data  所有数据
     */
    public abstract void onQuickBindView(V binding, T itemData , int position,  List<T> data);


    @Override
    public void onBindView(ViewBinding binding, T itemData, int position, int itemViewType, List<T> data) {
        onQuickBindView((V) binding, itemData, position, data);
    }

    @Override
    public Class<? extends ViewBinding> onCreateView(ViewGroup parent, int viewType) {
        return (Class<? extends ViewBinding>) ViewBindingUtils.getBindingClass(getClass());
    }


    // 解决复用数据 position 错乱问题
    @Override
    public int getItemViewType(int position, T itemData) {
        return position;
    }
}
