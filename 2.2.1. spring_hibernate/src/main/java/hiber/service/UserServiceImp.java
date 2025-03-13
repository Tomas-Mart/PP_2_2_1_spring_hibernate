package hiber.service;

import hiber.dao.UserDao;
import hiber.exception.UserNotFoundException;
import hiber.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final UserDao userDao;

    public UserServiceImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void add(User user) {
        try {
            logger.info("Добавление пользователя через сервис: {}", user);
            userDao.add(user);
        } catch (RuntimeException e) {
            logger.error("Ошибка при добавлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        try {
            logger.info("Извлечение всех пользователей через сервис");
            return userDao.listUsersWithCars();
        } catch (RuntimeException e) {
            logger.error("Ошибка при извлечении пользователей: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByCarModelAndSeries(String model, int series) {
        try {
            logger.info("Выборка пользователя по модели автомобиля: {} и серии: {} через сервис", model, series);
            return userDao.getUserByCarModelAndSeries(model, series);
        } catch (UserNotFoundException e) {
            logger.error("Пользователь не найден: {}", e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            logger.error("Ошибка при выборе пользователя по модели и серии автомобиля: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public void clearUsersAndCars() {
        try {
            logger.info("Очистка всех пользователей и автомобилей через сервис");
            userDao.clearUsersAndCars();
        } catch (RuntimeException e) {
            logger.error("Ошибка при очистке пользователей и автомобилей: {}", e.getMessage());
            throw e;
        }
    }
}