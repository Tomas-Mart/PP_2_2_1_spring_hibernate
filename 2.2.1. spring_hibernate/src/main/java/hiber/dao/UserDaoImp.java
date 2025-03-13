package hiber.dao;

import hiber.exception.UserNotFoundException;
import hiber.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImp.class);

    private final SessionFactory sessionFactory;

    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        try {
            logger.info("Добавление пользователя: {}", user);
            sessionFactory.getCurrentSession().save(user);
        } catch (HibernateException e) {
            logger.error("Ошибка при добавлении пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось добавить пользователя", e);
        }
    }

    @Override
    public List<User> listUsersWithCars() {
        try {
            logger.info("Получение всех пользователей с их автомобилями");
            String hql = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.car";
            TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);
            return query.getResultList();
        } catch (HibernateException e) {
            logger.error("Ошибка при извлечении пользователей: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить пользователей", e);
        }
    }

    @Override
    public User getUserByCarModelAndSeries(String model, int series) {
        try {
            logger.info("Выборка пользователя по модели автомобиля: {} и серии: {}", model, series);
            String hql = "SELECT u FROM User u JOIN FETCH u.car c WHERE c.model = :model AND c.series = :series";
            TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);
            query.setParameter("model", model);
            query.setParameter("series", series);
            return query.getResultList()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException("User not found with car model: " + model + " и серии: " + series));
        } catch (HibernateException e) {
            logger.error("Ошибка при выборе пользователя по модели и серии автомобиля: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить пользователя по модели и серии автомобиля", e);
        }
    }

    @Override
    public void clearUsersAndCars() {
        try {
            logger.info("Очистка всех пользователей и автомобилей");
            sessionFactory.getCurrentSession().createQuery("DELETE FROM User").executeUpdate();
            sessionFactory.getCurrentSession().createQuery("DELETE FROM Car").executeUpdate();
        } catch (HibernateException e) {
            logger.error("Ошибка при очистке пользователей и автомобилей: {}", e.getMessage());
            throw new RuntimeException("Не удалось очистить пользователей и автомобили", e);
        }
    }
}