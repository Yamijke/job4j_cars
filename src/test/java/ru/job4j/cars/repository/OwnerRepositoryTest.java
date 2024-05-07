package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.config.HibernateRunCfg;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OwnerRepositoryTest {

    private static SessionFactory sf;
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final OwnerRepository ownerRepository = new OwnerRepository(crudRepository);
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
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Test
    public void whenCreateOwnerThenFindTheSameOwner() {
        Owner owner = new Owner();
        owner.setName("TestOwner");
        User user = new User();
        user.setLogin("TestLogin");
        userRepository.create(user);
        owner.setAutoUser(user);
        ownerRepository.create(owner);
        Optional<Owner> rsl = ownerRepository.findById(owner.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo("TestOwner");
    }

    @Test
    public void whenUpdateOwnerThenYouGetNewOwner() {
        Owner owner = new Owner();
        owner.setName("TestOwner");
        User user = new User();
        user.setLogin("TestLogin");
        userRepository.create(user);
        owner.setAutoUser(user);
        ownerRepository.create(owner);
        owner.setName("TestOwner2");
        ownerRepository.update(owner);
        Optional<Owner> rsl = ownerRepository.findById(owner.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getName()).isEqualTo("TestOwner2");
    }

    @Test
    public void whenDeleteOwnerThenThereIsNoOwner() {
        Owner owner = new Owner();
        owner.setName("TestOwner");
        User user = new User();
        user.setLogin("TestLogin");
        userRepository.create(user);
        owner.setAutoUser(user);
        ownerRepository.create(owner);
        Optional<Owner> rsl = ownerRepository.findById(owner.getId());
        assertTrue(rsl.isPresent());
        ownerRepository.delete(owner.getId());
        Optional<Owner> rsl2 = ownerRepository.findById(owner.getId());
        assertThat(rsl2).isNotPresent();
    }
}