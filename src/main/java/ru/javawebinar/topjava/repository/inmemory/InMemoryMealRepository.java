package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));

        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 15, 10, 0), "Завтрак 2-го", 800), 2);
        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 15, 15, 0), "Обед 2-го", 700), 2);
        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 15, 20, 0), "Ужин 2-го", 600), 2);

        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 18, 10, 0), "Завтрак 3-го", 800), 3);
        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 18, 15, 0), "Обед 3-го", 700), 3);
        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 18, 20, 0), "Ужин 3-го", 400), 3);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            mealMap = new ConcurrentHashMap<>();
            repository.put(userId, mealMap);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId) == null ? new ConcurrentHashMap<>() : repository.get(userId);
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId) == null ? new ConcurrentHashMap<>() : repository.get(userId);
        return mealMap.values().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Meal::getDateTime)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getByDatesTimes(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId) == null ? new ConcurrentHashMap<>() : repository.get(userId);
        return mealMap.values().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Meal::getDateTime)))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate) && DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}

