package com.example.restaurantrating;

import com.example.restaurantrating.entity.Rating;
import com.example.restaurantrating.entity.Restaurant;
import com.example.restaurantrating.entity.Visitor;
import com.example.restaurantrating.enums.CuisineType;
import com.example.restaurantrating.service.RatingService;
import com.example.restaurantrating.service.RestaurantService;
import com.example.restaurantrating.service.VisitorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final RatingService ratingService;

    // Явный конструктор для внедрения зависимостей
    public DataInitializer(VisitorService visitorService,
                           RestaurantService restaurantService,
                           RatingService ratingService) {
        this.visitorService = visitorService;
        this.restaurantService = restaurantService;
        this.ratingService = ratingService;
    }

    @Override
    public void run(String... args) throws Exception {
        Visitor visitor1 = new Visitor(null, "Иван", 30, "М");
        Visitor visitor2 = new Visitor(null, "Мария", 25, "Ж");
        Visitor visitor3 = new Visitor(null, null, 40, "М"); // анонимный

        visitorService.save(visitor1);
        visitorService.save(visitor2);
        visitorService.save(visitor3);

        Restaurant restaurant1 = new Restaurant(null, "Итальянский дворик", "Уютное место с пастой",
                CuisineType.ITALIAN, new BigDecimal("1500"), BigDecimal.ZERO);
        Restaurant restaurant2 = new Restaurant(null, "Китайский дракон", "Блюда на любой вкус",
                CuisineType.CHINESE, new BigDecimal("1200"), BigDecimal.ZERO);

        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);

        Rating rating1 = new Rating(null, visitor1.getId(), restaurant1.getId(), 5, "Отлично!");
        Rating rating2 = new Rating(null, visitor2.getId(), restaurant1.getId(), 4, "Хорошо, но дороговато");
        Rating rating3 = new Rating(null, visitor3.getId(), restaurant2.getId(), 3, "");

        ratingService.save(rating1);
        ratingService.save(rating2);
        ratingService.save(rating3);

        System.out.println("\n=== Все посетители ===");
        visitorService.findAll().forEach(System.out::println);

        System.out.println("\n=== Все рестораны ===");
        restaurantService.findAll().forEach(System.out::println);

        System.out.println("\n=== Все оценки ===");
        ratingService.findAll().forEach(System.out::println);

        System.out.println("\n=== Рестораны после обновления рейтинга ===");
        restaurantService.findAll().forEach(r ->
                System.out.println(r.getName() + " - средняя оценка: " + r.getAverageRating()));

        System.out.println("\n=== Удаляем оценку rating2 (id=" + rating2.getId() + ") ===");
        ratingService.remove(rating2.getId());

        System.out.println("=== Рестораны после удаления оценки ===");
        restaurantService.findAll().forEach(r ->
                System.out.println(r.getName() + " - средняя оценка: " + r.getAverageRating()));
    }
}