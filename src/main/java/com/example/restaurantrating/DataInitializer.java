package com.example.restaurantrating;

import com.example.restaurantrating.dto.RestaurantRequest;
import com.example.restaurantrating.dto.ReviewRequest;
import com.example.restaurantrating.dto.UserRequest;
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

    public DataInitializer(VisitorService visitorService,
                           RestaurantService restaurantService,
                           RatingService ratingService) {
        this.visitorService = visitorService;
        this.restaurantService = restaurantService;
        this.ratingService = ratingService;
    }

    @Override
    public void run(String... args) throws Exception {
        UserRequest user1 = new UserRequest("Иван", 30, "М");
        UserRequest user2 = new UserRequest("Мария", 25, "Ж");
        UserRequest user3 = new UserRequest(null, 40, "М"); // анонимный

        var savedUser1 = visitorService.save(user1);
        var savedUser2 = visitorService.save(user2);
        var savedUser3 = visitorService.save(user3);

        RestaurantRequest restaurantReq1 = new RestaurantRequest(
                "Итальянский дворик",
                "Уютное место с пастой",
                CuisineType.ITALIAN,
                new BigDecimal("1500")
        );
        RestaurantRequest restaurantReq2 = new RestaurantRequest(
                "Китайский дракон",
                "Блюда на любой вкус",
                CuisineType.CHINESE,
                new BigDecimal("1200")
        );

        var savedRestaurant1 = restaurantService.save(restaurantReq1);
        var savedRestaurant2 = restaurantService.save(restaurantReq2);

        ReviewRequest review1 = new ReviewRequest(
                savedUser1.id(),
                savedRestaurant1.id(),
                5,
                "Отлично!"
        );
        ReviewRequest review2 = new ReviewRequest(
                savedUser2.id(),
                savedRestaurant1.id(),
                4,
                "Хорошо, но дороговато"
        );
        ReviewRequest review3 = new ReviewRequest(
                savedUser3.id(),
                savedRestaurant2.id(),
                3,
                ""
        );

        var savedReview1 = ratingService.save(review1);
        var savedReview2 = ratingService.save(review2);
        var savedReview3 = ratingService.save(review3);

        System.out.println("\n=== Все посетители ===");
        visitorService.findAll().forEach(System.out::println);

        System.out.println("\n=== Все рестораны ===");
        restaurantService.findAll().forEach(System.out::println);

        System.out.println("\n=== Все оценки ===");
        ratingService.findAll().forEach(System.out::println);

        System.out.println("\n=== Рестораны после обновления рейтинга ===");
        restaurantService.findAll().forEach(r ->
                System.out.println(r.name() + " - средняя оценка: " + r.averageRating()));

        System.out.println("\n=== Удаляем оценку review2 (visitorId=" + savedReview2.visitorId() +
                ", restaurantId=" + savedReview2.restaurantId() + ") ===");
        ratingService.remove(savedReview2.visitorId(), savedReview2.restaurantId());

        System.out.println("=== Рестораны после удаления оценки ===");
        restaurantService.findAll().forEach(r ->
                System.out.println(r.name() + " - средняя оценка: " + r.averageRating()));
    }
}