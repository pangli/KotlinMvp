package com.zorro.kotlin.baselibs.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Zorro on 2019/11/14.
 * 备注：固定长度List
 * 如果List里面的元素个数大于了缓存最大容量，则删除链表的头元素
 */
public class FixSizeLinkedList<E> extends LinkedList<E> {
    private static final long serialVersionUID = 3292612616231532364L;
    // 定义缓存的容量
    private volatile int capacity;


    public FixSizeLinkedList(int capacity) {
        super();
        this.capacity = capacity;
    }

    public FixSizeLinkedList(Collection<? extends E> c, int capacity) {
        this(capacity);
        addAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Iterator<? extends E> iterator = iterator();
        Iterator<? extends E> addIterator = c.iterator();
        if (iterator.hasNext()) {//原集合有数据需要组合判断
            if (addIterator.hasNext()) {//原集合无数据时只需判断新集合长度
                for (int i = 0; i < c.size(); i++) {
                    add(addIterator.next());
                }
            }
        } else {
            if (addIterator.hasNext()) {//原集合无数据时只需判断新集合长度
                while (c.size() > capacity) {
                    addIterator.next();
                    addIterator.remove();
                }
            }
            return super.addAll(c);
        }
        return true;
    }

    @Override
    public boolean add(E e) {
        // 超过长度，检索并删除此列表的头
        if (size() + 1 > capacity) {
            super.removeFirst();
        }
        return super.add(e);
    }
}
