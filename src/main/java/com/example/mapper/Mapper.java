package com.example.mapper;

/**
 * Функциональный интерфейс, который предоставляет метод map для перевода dto объектов к java сущностям и обратно
 * @param <F> - тип объекта, с которого приводим
 * @param <T> - тип объекта, к которому приводим
 */
@FunctionalInterface
public interface Mapper<F, T> {

    T map(F object);
}
