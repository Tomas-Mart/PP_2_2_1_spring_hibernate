package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }

    @Override
    public User getUserByCarModelAndSeries(String model, int series) {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User WHERE car.model = :model AND car.series = :series", User.class);
        query.setParameter("model", model);
        query.setParameter("series", series);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public void clearUsersAndCars() {
        sessionFactory.getCurrentSession().createQuery("DELETE FROM User").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("DELETE FROM Car").executeUpdate();
    }
}
