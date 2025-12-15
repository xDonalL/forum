package com.forum.forum.repository;

import java.util.List;

public interface BaseRepository<T> {

    T save(T object);

    boolean delete(int id);

    T get(int id);

    List<T> getAll();
}
