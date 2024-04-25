package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DTO.FoodDTO;
import cz.uhk.fim.project.bakalarka.service.FoodService;
import cz.uhk.fim.project.bakalarka.util.AuthorizationCheck;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller class for handling food-related endpoints.
 *
 * @author Alex Zamastil
 */
@RestController
@RequestMapping("/api")

public class FoodController {
    private final FoodService foodService;
    MessageHandler<String> messageHandler = new MessageHandler<>();

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    /**
     * Endpoint for adding a new food
     *
     * @param foodRequest = the DTO containing the food data
     * @param imageData   = data of the food image
     * @param request     = HttpServletRequest that contains headers, authorization header is needed for this request
     * @return ResponseEntity with the result of the operation.
     */

    @PostMapping(value = "food/addFood", consumes = "multipart/form-data")
    public ResponseEntity<?> addFood(
            @RequestPart(name = "foodRequest") FoodDTO foodRequest,
            @RequestPart(name = "imageData") MultipartFile imageData,
            HttpServletRequest request
    ) {
        if (!AuthorizationCheck.hasAuthorization(request)) return messageHandler.error("Missing authorization");
        return foodService.addNewFood(foodRequest, imageData);
    }

    /**
     * Endpoint for retrieving food details by ID.
     *
     * @param id The ID of the food to retrieve.
     * @return ResponseEntity with the food details. (retrieving image data has its own endpoint)
     */
    @GetMapping(value = "food/getFood/{id}")
    public ResponseEntity<?> getFood(@PathVariable Long id) {
        return foodService.getFood(id);
    }

    /**
     * Endpoint for retrieving the picture of the food by ID.
     *
     * @param id The ID of the food to retrieve the picture for.
     * @return ResponseEntity with the food picture.
     */
    @GetMapping(value = "food/getFood/picture/{id}")
    public ResponseEntity<?> getFoodPicture(@PathVariable Long id) {
        return foodService.getFoodPicture(id);
    }
}

