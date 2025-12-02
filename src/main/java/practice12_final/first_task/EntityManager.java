package practice12_final.first_task;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class EntityManager<T extends Entity> {

    private CopyOnWriteArrayList<T> entityList;

    public EntityManager() {
        entityList = new CopyOnWriteArrayList<>();
    }

    public void addEntity(T entity) {
        if (entity == null) {
            throw new NoSuchElementException("Невозможно добавить null элемент");
        }
        if (entity.getName().isEmpty()) {
            throw new NoSuchElementException("Невозможно добавить сущность с пустым именем");
        }
        entityList.add(entity);
        System.out.println("В список добавлена новая сущность: " + entity);
    }

    public boolean removeEntity(T entity) {
        if (entity == null) {
            throw new NoSuchElementException("Невозможно удалить null элемент");
        }
        if (entityList.contains(entity)) {
            entityList.remove(entity);
            System.out.println("Из списка удалена сущность: " + entity);
            return true;
        } else {
            System.out.println("В списке отсутствует данная сущность");
            return false;
        }
    }

    public List<T> getEntityList() {
        return List.copyOf(entityList);
    }

    public List<T> getEntitiesByAge(int startAge, int endAge) {
        return entityList.stream().filter(entity ->
                        entity.getAge() >= startAge && entity.getAge() <= endAge)
                .collect(Collectors.toList());
    }

    public List<T> getEntitiesByName(String name) {
        if (name == null) {
            throw new NoSuchElementException("Невозможно найти null элемент");
        }
        return entityList.stream().filter(entity ->
                entity.getName().equalsIgnoreCase(name)
        ).collect(Collectors.toList());
    }

    public List<T> getEntitiesByStatus(EntityStatus entityStatus) {
        if (entityStatus == null) {
            throw new NoSuchElementException("Невозможно найти null элемент");
        }
        return entityList.stream().filter(entity ->
                entity.getEntityStatus().equals(entityStatus)
        ).collect(Collectors.toList());
    }


    /**
     * Функциональные требования:
     * 1. Ок - Добавление элементов: Метод для добавления объекта в коллекцию. Должен быть потокобезопасным.
     * 2. Ок - Удаление элементов: Метод для удаления объекта из коллекции. Возвращает true, если объект был удалён,
     * и false, если объект не найден в коллекции. Должен быть потокобезопасным.
     * 3. Ок - Получение всех элементов: Метод возвращает копию списка всех элементов,
     * обеспечивая невозможность изменения исходной коллекции через возвращаемый список.
     * 4. Специализированные методы фильтрации:
     * 5. Ок - Фильтрация по возрасту: Возвращает список пользователей в заданном возрастном диапазоне.
     * 6. Ок - Фильтрация по имени: Возвращает список пользователей, чьи имена соответствуют заданной строке.
     * 7. Ок - Фильтрация по активности: Возвращает список пользователей с заданным статусом активности.
     */
}
