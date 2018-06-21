package com.linyimin.java.algorithms.stack;

import java.util.Iterator;

/**
 * @Author: linyimin
 * @Description:
 * @Date: Created in 23:13 2018/6/21
 */
public class DynamicCapacityStackWithIterable<T> implements Iterable<T>{
    private T[] a = (T[]) new Object[1];
    private int N;

    public int size(){
        return this.N;
    }
    public boolean isEmpty(){
        return this.N == 0;
    }

    public void push(T item){
        if(this.N == this.a.length){
            resize(this.a.length * 2);
        }
        this.a[N++] = item;
    }

    public T pop(){
        if(this.N == 0){
            return null;
        }
        T item = this.a[--N];
        this.a[N] = null;
        if(this.N < this.a.length / 4){
            resize(a.length / 2);
        }
        return item;
    }

    // 重新创建指定大小的数组
    private void resize(int capacity){
        T[] temp = (T[]) new Object[capacity];
        for(int i = 0; i < this.N; i++){
            temp[i] = this.a[i];
        }
        this.a = temp;
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }

    class StackIterator implements Iterator<T>{

        private int i = N;
        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public T next() {
            return a[--i];
        }
    }
}
