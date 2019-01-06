package com.photogroup.model.group;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public abstract class AbstractGroupBySetTable<E> implements GroupBySetTable<E> {

    private Set<E> data;

    private Map<String, GroupBy<E>> groupBy;

    protected AbstractGroupBySetTable() {
        data = new HashSet<E>();
        groupBy = new Hashtable<String, GroupBy<E>>();
        for (Field f : getModelClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(GroupByType.class)) {
                groupBy.put(f.getName(), new GroupBy<E>());
            }
        }
    }

    public abstract Class<?> getModelClass();

    public Set<E> query(String fieldName, Object value) {
        Set<E> result = new HashSet<E>();
        for (E e : data) {
            Object obj = getField(e, fieldName);
            if (value.equals(obj)) {
                result.add(e);
            }
        }

        return result;
    }

    @Override
    public void insert(E e) {
        data.add(e);
        groupBy.forEach((k, v) -> {
            Object obj = getField(e, k);
            if (obj != null) {
                v.add(obj, e);
            }
        });
    }

    @Override
    public void delete(E e) {
        data.remove(e);
        try {
            for (Field f : e.getClass().getDeclaredFields()) {
                Object obj = getField(e, f.getName());
                if (obj != null) {
                    groupBy.get(f.getName()).delete(obj, e);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public Map<Object, Set<E>> groupBy(String fieldName) {
        return groupBy.get(fieldName).getGroupBy();
    }

    private static Object getField(Object obj, String fieldName) {
        Object val = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (field != null) {
                boolean isAccessible = field.isAccessible();
                if (!isAccessible) {
                    field.setAccessible(true);
                }
                val = field.get(obj);
                if (!isAccessible) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return val;
    }
}
