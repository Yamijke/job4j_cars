package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.config.HibernateRunCfg;
import ru.job4j.cars.model.Engine;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EngineRepositoryTest {
    private static SessionFactory sf;
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);

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
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Test
    public void whenCreateEngineThenFindTheSameEngine() {
        Engine engine = new Engine();
        engine.setName("TDI");
        engineRepository.create(engine);
        Optional<Engine> rsl = engineRepository.findById(engine.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo("TDI");
    }

    @Test
    public void whenUpdateEngineThenYouGetNewEngine() {
        Engine engine = new Engine();
        engine.setName("TDI");
        engineRepository.create(engine);
        engine.setName("Gasoline");
        engineRepository.update(engine);
        Optional<Engine> rsl = engineRepository.findById(engine.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo("Gasoline");
    }

    @Test
    public void whenDeleteEngineThenThereIsNoEngine() {
        Engine engine = new Engine();
        engine.setName("TDI");
        engineRepository.create(engine);
        Optional<Engine> rsl = engineRepository.findById(engine.getId());
        assertTrue(rsl.isPresent());
        engineRepository.delete(engine.getId());
        Optional<Engine> rsl2 = engineRepository.findById(engine.getId());
        assertThat(rsl2).isNotPresent();
    }
}