package com.my.liufeng.rpc.scan;

public interface ClassSelector {
    /**
     * 判断该类是否选中
     *
     * @param clazz class对象
     * @return true：选中；false：未选中
     */
    boolean select(Class<?> clazz);
}
