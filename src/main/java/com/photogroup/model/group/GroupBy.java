package com.photogroup.model.group;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class GroupBy<E> {

    private Map<Object, Set<E>> groupBy = new Hashtable<Object, Set<E>>();

    public Map<Object, Set<E>> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(Map<Object, Set<E>> groupBy) {
        this.groupBy = groupBy;
    }

    public void add(Object key, E e) {
        if (groupBy.containsKey(key)) {
            groupBy.get(key).add(e);
        } else {
            Set<E> v = new HashSet<E>();
            v.add(e);
            groupBy.put(key, v);
        }
    }

    public boolean delete(Object key, E value) {
        boolean removed = groupBy.get(key).remove(value);
        if (removed && groupBy.get(key).size() == 0) {
            groupBy.remove(key);
        }
        return removed;
    }
}
