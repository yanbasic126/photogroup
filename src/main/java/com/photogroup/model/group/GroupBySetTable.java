package com.photogroup.model.group;

import java.util.Map;
import java.util.Set;

public interface GroupBySetTable<E> {

    void insert(E e);

    void delete(E e);

    Set<E> query(String fieldName, Object value);

    Map<Object, Set<E>> groupBy(String fieldName);

}