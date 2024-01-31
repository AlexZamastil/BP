package cz.uhk.fim.project.bakalarka.controller;

import cz.uhk.fim.project.bakalarka.DAO.UserRepository;
import cz.uhk.fim.project.bakalarka.DAO.UserStatsRepository;
import cz.uhk.fim.project.bakalarka.model.User;
import cz.uhk.fim.project.bakalarka.model.UserStats;
import cz.uhk.fim.project.bakalarka.request.CreateTrainingRequest;
import cz.uhk.fim.project.bakalarka.service.TrainingService;
import cz.uhk.fim.project.bakalarka.util.MessageHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
public class TrainingController {

    private final TrainingService trainingService;
    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    @Autowired
    public TrainingController(TrainingService trainingService, UserRepository userRepository, UserStatsRepository userStatsRepository) {
        this.trainingService = trainingService;
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
    }

    @PostMapping(value = "authorized/generateTraining", consumes = {"application/json"})
    public ResponseEntity<?> generateTraining(@RequestBody CreateTrainingRequest createTrainingRequest, HttpServletRequest httpServletRequest) throws Exception {
        trainingService.trainModel();
        System.out.println(createTrainingRequest);
        Instant wantedTime = Instant.parse(createTrainingRequest.getWantedTime());
        Instant actualTime = Instant.parse(createTrainingRequest.getActualTime());

        LocalDate today = LocalDate.now();

        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();

        Duration wantedTimeDuration = Duration.between(startOfDay,wantedTime);
        Duration actualTimeDuration = Duration.between(startOfDay,actualTime);

        System.out.println("WANTED TIME: " + wantedTimeDuration);
        System.out.println("ACTUAL TIME: " + actualTimeDuration);

        String token = httpServletRequest.getHeader("Authorization");
        User user = userRepository.findUserByToken(token);
        UserStats userStats = userStatsRepository.findUserStatsByUser(user);

        float minutesOfActualTime = actualTimeDuration.toMinutes();
        double actualPace = minutesOfActualTime/createTrainingRequest.getActualRunLength();

        float minutesOfWantedTime = wantedTimeDuration.toMinutes();
        double wantedPace = minutesOfWantedTime/createTrainingRequest.getActualRunLength();


        int trainingDays = (int) ChronoUnit.DAYS.between(createTrainingRequest.getStartDay(),createTrainingRequest.getRaceDay());
//temp
        String firstLetter = user.getBodyType().toString().substring(0,1).toUpperCase();
        String restLetters = user.getBodyType().toString().substring(1).toLowerCase();
        String adjustedBodyType = firstLetter + restLetters;
        String firstLetter1 = createTrainingRequest.getGoal().toString().substring(0,1).toUpperCase();
        String restLetters1 = createTrainingRequest.getGoal().toString().substring(1).toLowerCase();
        String adjustedRun;
        if(createTrainingRequest.getGoal().toString().equals("RUN"))
         adjustedRun = firstLetter1 + restLetters1;
         else adjustedRun = createTrainingRequest.getGoal().toString();
//

        trainingService.generateTrainingPlan(
                user.getWeight(),
                user.getHeight(),
                userStats.getBmi(),
                user.getSex().toString(),
                adjustedBodyType,
                1,
                trainingDays,
                createTrainingRequest.getActualRunLength(),
                actualPace,
                createTrainingRequest.getLengthOfRaceInMeters(),
                wantedPace,
                adjustedRun,
                createTrainingRequest.getElevationProfile().toString()
                );

        trainingService.testTrainingPossibility(
                createTrainingRequest.getStartDay(),
                createTrainingRequest.getRaceDay(),
                createTrainingRequest.getLengthOfRaceInMeters(),
                wantedTimeDuration,createTrainingRequest.getActualRunLength(),
                actualTimeDuration);

return MessageHandler.success("Training generated");

    }

    @GetMapping(value = "authorized/hasActiveTraining/{id}")
    public boolean hasActiveTraining(@PathVariable long id){
        return trainingService.hasActiveTraining(id);
    }

    @PostMapping(value = "nonauthorized/trainJ48")
    public void trainJ48() throws Exception {trainingService.trainModel();}
}
