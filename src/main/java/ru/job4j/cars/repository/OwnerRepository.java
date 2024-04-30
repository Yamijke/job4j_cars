package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param owner пользователь.
     * @return владелец с id.
     */
    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    /**
     * Обновить в базе владельца.
     *
     * @param owner пользователь.
     */
    public void update(Owner owner) {
        crudRepository.run(session -> session.merge(owner));
    }

    /**
     * Удалить владельца по id.
     *
     * @param ownerId ID
     */
    public void delete(int ownerId) {
        crudRepository.run(
                "DELETE FROM Owner WHERE id = :fId",
                Map.of("fId", ownerId)
        );
    }

    /**
     * Список владельцев отсортированных по id.
     *
     * @return список владельцев.
     */
    public List<Owner> findAllOrderById() {
        return crudRepository.query("from Owner order by id asc", Owner.class);
    }

    /**
     * Найти владельца по ID
     *
     * @return владелец.
     */
    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional(
                "from Owner where id = :fId", Owner.class,
                Map.of("fId", ownerId)
        );
    }
}
