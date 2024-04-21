package cz.uhk.fim.project.bakalarka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.uhk.fim.project.bakalarka.DAO.FoodRepository;
import cz.uhk.fim.project.bakalarka.DAO.PictureRepository;
import cz.uhk.fim.project.bakalarka.DAO.TagRepository;
import cz.uhk.fim.project.bakalarka.DTO.FoodDTO;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Food;
import cz.uhk.fim.project.bakalarka.enumerations.Tag_Timing;
import cz.uhk.fim.project.bakalarka.model.Food;
import cz.uhk.fim.project.bakalarka.model.Picture;
import cz.uhk.fim.project.bakalarka.model.Tag;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import io.micrometer.common.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Service class responsible for managing food-related operations.
 *
 * @author Alex Zamastil
 */
@Service
public class FoodService {
    MessageHandler<String> stringMessageHandler = new MessageHandler<>();
    private final MessageSource messageSource;
    private final PictureRepository pictureRepository;
    private final FoodRepository foodRepository;
    private final TagRepository tagRepository;
    MessageHandler<FoodDTO> foodMessageHandler = new MessageHandler<>();
    MessageHandler<Long> longMessageHandler = new MessageHandler<>();

    public FoodService(MessageSource messageSource, PictureRepository pictureRepository, FoodRepository foodRepository, TagRepository tagRepository) {
        this.messageSource = messageSource;
        this.pictureRepository = pictureRepository;
        this.foodRepository = foodRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Adds a new food on the provided FoodDTO and multipart file.
     * Returns an appropriate ResponseEntity indicating the result of the operation.
     *
     * @param foodRequest Details of food to be added.
     * @param multipartFile   Multipart file containing the picture associated with the food.
     * @return ResponseEntity containing the result of the operation.
     */
    public ResponseEntity<?> addNewFood(FoodDTO foodRequest, MultipartFile multipartFile) {

        if(foodRequest == null) return stringMessageHandler.error(messageSource.getMessage("error.exercise/food.null", null, LocaleContextHolder.getLocale()));
        if (StringUtils.isBlank(foodRequest.getName()) ||
                StringUtils.isBlank(foodRequest.getName_eng()) ||
                StringUtils.isBlank(foodRequest.getDescription()) ||
                StringUtils.isBlank(foodRequest.getDescription_eng()) ||
                foodRequest.getCalories() == 0 ||
                multipartFile.isEmpty()
        )  return stringMessageHandler.error(messageSource.getMessage("error.exercise/food.null", null, LocaleContextHolder.getLocale()));

        return createFood(multipartFile, foodRequest.getName(), foodRequest.getDescription(), foodRequest.getName_eng(), foodRequest.getDescription_eng(), foodRequest.getCalories(), foodRequest.getTags());
    }
    /**
     * Retrieves details of food based on its ID.
     * If the food is found, the details are encapsulated in an FoodDTO object and returned as a success response.
     * Otherwise, an error response is returned.
     *
     * @param id The ID of the food to retrieve.
     * @return ResponseEntity containing the food details or an error message.
     */
    public ResponseEntity<?> getFood(long id) {
        Optional<Food> f = foodRepository.findFoodById(id);
        if(f.isPresent()){
            Food food = f.get();
            Set<Tag> tags = foodRepository.findTagsByFoodId(id);
            List<String> tagList = new ArrayList<>();
            String jsonList;
            for (Tag t : tags) {
                tagList.add(t.getText());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                jsonList = objectMapper.writeValueAsString(tagList);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            FoodDTO foodDTO = new FoodDTO(
                    food.getName(),
                    food.getName_eng(),
                    food.getDescription(),
                    food.getDescription_eng(),
                    food.getCaloriesGained(),
                    jsonList);
            return foodMessageHandler.success(foodDTO);
        }
        return stringMessageHandler.error(messageSource.getMessage("error.invalidID", null, LocaleContextHolder.getLocale()));

    }
    /**
     * Retrieves the picture associated with food based on its ID.
     * If the food is found, its picture data is converted to a Base64-encoded string and returned as a success response.
     * Otherwise, an error response is returned.
     *
     * @param id The ID of the food to retrieve the picture for.
     * @return ResponseEntity containing the Base64-encoded picture data or an error message.
     */
    public ResponseEntity<?> getFoodPicture(long id) {
        Optional<Food> f = foodRepository.findFoodById(id);
        if (f.isPresent()) {
            Food food = f.get();
            byte[] picture = food.getPicture().getPictureData();
            String pictureBytes = Base64.getEncoder().encodeToString(picture);
            return stringMessageHandler.success(pictureBytes);
        }
        return stringMessageHandler.error(messageSource.getMessage("error.invalidID", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Handles the processing of tags associated with the food.
     * Tags are converted to uppercase and checked against predefined exercise tags.
     * If a tag matches, it is saved and associated with the exercise.
     *
     * @param tags     The list of tags associated with the food.
     * @param food The food to associate the tags with.
     */
    public void handleTags(List<String> tags, Food food) {

        for (String tag : tags) {

            tag = tag.toUpperCase();
            boolean isMatch = false;
            for (Tag_Food tagEnum : Tag_Food.values()) {

                if (tag.equals(tagEnum.toString())) {

                    isMatch = true;
                    break;
                }
            }
            for (Tag_Timing tagEnum : Tag_Timing.values()) {

                if (tag.equals(tagEnum.toString())) {

                    isMatch = true;
                    break;
                }
            }
            if (isMatch) {
                if (tagRepository.findTagByText(tag) == null) {
                    cz.uhk.fim.project.bakalarka.model.Tag newTag = new cz.uhk.fim.project.bakalarka.model.Tag(tag);
                    tagRepository.save(newTag);
                }
                cz.uhk.fim.project.bakalarka.model.Tag tag2 = tagRepository.findTagByText(tag);
                foodRepository.save(food);
                food.addTagFood(tag2);
                tagRepository.save(tag2);
            }
        }
    }
    /**
     * Converts a multipart file to its byte array representation.
     *
     * @param f The multipart file to convert.
     * @return The byte array representing the contents of the multipart file.
     */
    public byte[] handlePicture(MultipartFile f) {
        try {
            return f.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new food entity based on the provided details.
     * The food is associated with a picture and tags.
     *
     * @param multipartFile  The multipart file containing the exercise picture.
     * @param name           The name of the food.
     * @param description    The description of the food.
     * @param name_eng       The English name of the food.
     * @param description_eng The English description of the food.
     * @param tags           The tags associated with the food.
     * @return The newly created Food entity.
     */
    public ResponseEntity<?> createFood(MultipartFile multipartFile, String name, String description, String name_eng, String description_eng, int calories, List<String> tags) {
        byte[] pictureData = handlePicture(multipartFile);
        Picture picture = new Picture(pictureData);
        pictureRepository.save(picture);
        Food food = new Food(calories,description,name,description_eng,name_eng,picture);
        foodRepository.save(food);
        handleTags(tags,food);
        return longMessageHandler.success(food.getId());
    }
}
