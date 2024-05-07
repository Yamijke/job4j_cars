package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;
import ru.job4j.cars.config.HibernateRunCfg;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarRepositoryTest {
    private static SessionFactory sf;
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final CarRepository carRepository = new CarRepository(crudRepository);

    @BeforeAll
    public static void init() {
        sf = new HibernateRunCfg().getSessionFactory();
    }

    @AfterAll
    public static void close() {
        sf.close();
    }

    @BeforeEach
    public void cleanDb() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Test
    public void whenCreateCarThenFindTheSameCar() {
        Car car = new Car();
        car.setName("VW");
        carRepository.create(car);
        Optional<Car> rsl = carRepository.findById(car.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo(car.getName());
    }

    @Test
    public void whenUpdateCarThenYouGetNewCar() {
        Car car = new Car();
        car.setName("VW");
        carRepository.create(car);
        car.setName("Audi");
        carRepository.update(car);
        Optional<Car> rsl = carRepository.findById(car.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo("Audi");
    }

    @Test
    public void whenDeleteCarThenThereIsNoCar() {
        Car car = new Car();
        car.setName("VW");
        carRepository.create(car);
        Optional<Car> rsl = carRepository.findById(car.getId());
        assertTrue(rsl.isPresent());
        carRepository.delete(car.getId());
        Optional<Car> rsl2 = carRepository.findById(car.getId());
        assertThat(rsl2).isNotPresent();
    }
}