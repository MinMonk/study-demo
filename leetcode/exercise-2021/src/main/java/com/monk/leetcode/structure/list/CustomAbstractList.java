package com.monk.leetcode.structure.list;

public abstract class CustomAbstractList<E> implements CustomList{

    protected int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(Object value) {
        add(size, value);
    }

    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != ELEMENT_NOT_FOUND;
    }

    /**
     * 检查索引的值是否合法
     * @param index
     */
    protected void rangeCheck(int index){
        if(index < 0 || index >= size){
            outOfBound(index);
        }
    }

    protected void rangeCheckForAdd(int index) {
        if(index < 0 || index > size){
            outOfBound(index);
        }
    }

    private void outOfBound(int index) {
        throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
    }
}
