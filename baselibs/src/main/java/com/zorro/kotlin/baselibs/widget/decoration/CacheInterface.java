package com.zorro.kotlin.baselibs.widget.decoration;

/**
 * Created by Zorro
 * date 2019/10/18
 * 缓存工具需要暴露的接口
 */

public interface CacheInterface<T> {

    /**
     * 加入缓存
     *
     * @param position
     * @param t
     */
    void put(int position, T t);

    /**
     * 从缓存中获取
     *
     * @param position
     * @return
     */
    T get(int position);

    /**
     * 移除
     *
     * @param position
     */
    void remove(int position);

    /**
     * 清空缓存
     */
    void clean();

}
