package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        if (!loginExists(user.getLogin())) {
            Session session = sf.openSession();
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return user;
    }

    /**
     * Проверка существования логина
     *
     * @param login login.
     * @return true/false
     */
    public boolean loginExists(String login) {
        try (Session session = sf.openSession()) {
            User user = session.createQuery("FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
            return user != null;
        }
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET password = :fPassword WHERE id = :fId")
                    .setParameter("fPassword", "new password")
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            List<User> result = session.createQuery("from User order by id", User.class).list();
            session.getTransaction().commit();
            return result;
        }
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            List<User> result = session.createQuery("from User as u where u.login like :key", User.class)
                    .setParameter("key", "%" + key + "%")
                    .getResultList();
            session.getTransaction().commit();
            return result;
        }
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        try (Session session = sf.openSession()) {
            User user = session.createQuery("from User u where u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }
}