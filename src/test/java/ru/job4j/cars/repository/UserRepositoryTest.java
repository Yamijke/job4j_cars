package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.config.HibernateRunCfg;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    private static SessionFactory sf;
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final UserRepository userRepository = new UserRepository(crudRepository);

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
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Test
    public void whenCreateUserThenFindTheSameUser() {
        User user = new User();
        user.setLogin("TestLogin");
        userRepository.create(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getLogin()).isEqualTo("TestLogin");
    }

    @Test
    public void whenUpdateUserThenYouGetNewUser() {
        User user = new User();
        user.setLogin("TestLogin");
        userRepository.create(user);
        user.setLogin("TestLogin2");
        userRepository.update(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getLogin()).isEqualTo("TestLogin2");
    }

    @Test
    public void whenDeleteUserThenThereIsNoUser() {
        User user = new User();
        user.setLogin("TestLogin");
        userRepository.create(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertTrue(rsl.isPresent());
        userRepository.delete(user.getId());
        Optional<User> rsl2 = userRepository.findById(user.getId());
        assertThat(rsl2).isNotPresent();
    }
}