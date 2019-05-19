package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rainday on 16/6/30.
 * ViewObject为视图对象
 * 通过键值对存储返回给视图的数据
 * 方便传入数据到velocity
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
