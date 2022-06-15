package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public MealTo get(int id) {
        log.info("get {}", id);
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getTos(Arrays.asList(service.get(id, userId)), SecurityUtil.authUserCaloriesPerDay()).get(0);
    }

    public MealTo create(Meal meal) {
        log.info("create {}", meal);
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        return MealsUtil.getTos(Arrays.asList(service.create(meal, userId)), SecurityUtil.authUserCaloriesPerDay()).get(0);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public List<MealTo> getByDatesTimes(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getByDatesTimes {}, {}, {}, {}", startDate, startTime, endDate, endTime);
        int userId = SecurityUtil.authUserId();

        return MealsUtil.getTos(service.getByDatesTimes(startDate, startTime, endDate, endTime, userId), SecurityUtil.authUserCaloriesPerDay());
    }
}