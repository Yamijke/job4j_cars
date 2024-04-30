package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param car пользователь.
     * @return авто с id.
     */
    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    /**
     * Обновить в базе авто.
     *
     * @param car пользователь.
     */
    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удалить авто по id.
     *
     * @param carId ID
     */
    public void delete(int carId) {
        crudRepository.run(
                "DELETE FROM Car WHERE id = :fId",
                Map.of("fId", carId)
        );
    }

    /**
     * Список авто отсортированных по id.
     *
     * @return список авто.
     */
    public List<Car> findAllOrderById() {
        return crudRepository.query("from Car order by id asc", Car.class);
    }

    /**
     * Найти авто по ID
     *
     * @return авто.
     */
    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "from Car where id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }
}
