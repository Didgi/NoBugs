package practice12_final.first_task;

import java.util.Objects;

public abstract class Entity {
    private final String name;
    private final int age;
    private final EntityStatus entityStatus;

    public Entity(String name, int age, EntityStatus entityStatus) {
        this.name = name;
        this.age = age;
        this.entityStatus = entityStatus;
    }

    public Entity(String name, int age) {
        this.name = name;
        this.age = age;
        this.entityStatus = EntityStatus.ACTIVE;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public EntityStatus getEntityStatus() {
        return entityStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return age == entity.age && Objects.equals(name, entity.name) && entityStatus == entity.entityStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, entityStatus);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", entityStatus=" + entityStatus +
                '}';
    }
}
