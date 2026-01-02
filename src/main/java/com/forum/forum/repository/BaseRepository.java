package com.forum.forum.repository;

public interface BaseRepository<T> {

    T save(T object);

    boolean delete(int id);

    T get(int id);

}
