package cz.uhk.fim.project.bakalarka.request;

import cz.uhk.fim.project.bakalarka.enumerations.ElevationProfile;
import cz.uhk.fim.project.bakalarka.enumerations.Goal;

import java.time.Duration;
import java.time.LocalDate;

public class CreateTrainingRequest {
    LocalDate startDay;
    LocalDate raceDay;
    Goal goal;
    Integer lengthOfRaceInMeters;
    String wantedTime;
    Integer actualRunLength;
    String actualTime;

    ElevationProfile elevationProfile;

    public CreateTrainingRequest(LocalDate startDay, LocalDate raceDay, Goal goal, Integer lengthOfRaceInMeters, String wantedTime, Integer actualRunLength, String actualTime, ElevationProfile elevationProfile) {
        this.startDay = startDay;
        this.raceDay = raceDay;
        this.goal = goal;
        this.lengthOfRaceInMeters = lengthOfRaceInMeters;
        this.wantedTime = wantedTime;
        this.actualRunLength = actualRunLength;
        this.actualTime = actualTime;
        this.elevationProfile = elevationProfile;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startday) {
        this.startDay = startday;
    }

    public LocalDate getRaceDay() {
        return raceDay;
    }

    public void setRaceDay(LocalDate raceDay) {
        this.raceDay = raceDay;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Integer getLengthOfRaceInMeters() {
        return lengthOfRaceInMeters;
    }

    public void setLengthOfRaceInMeters(Integer lengthOfRaceInMeters) {
        this.lengthOfRaceInMeters = lengthOfRaceInMeters;
    }

    public String getWantedTime() {
        return wantedTime;
    }

    public void setWantedTime(Integer wantedTimeInSeconds) {
        this.wantedTime = wantedTime;
    }

    public Integer getActualRunLength() {
        return actualRunLength;
    }

    public void setActualRunLength(Integer actualRunLength) {
        this.actualRunLength = actualRunLength;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTimeInSecond) {
        this.actualTime = actualTimeInSecond;
    }

    public void setWantedTime(String wantedTime) {
        this.wantedTime = wantedTime;
    }

    public ElevationProfile getElevationProfile() {
        return elevationProfile;
    }

    public void setElevationProfile(ElevationProfile elevationProfile) {
        this.elevationProfile = elevationProfile;
    }

    @Override
    public String toString() {
        return "CreateTrainingRequest{" +
                "startDay=" + startDay +
                ", raceDay=" + raceDay +
                ", goal=" + goal +
                ", lengthOfRaceInMeters=" + lengthOfRaceInMeters +
                ", wantedTimeInSeconds=" + wantedTime +
                ", actualRunLength=" + actualRunLength +
                ", actualTimeInSeconds=" + actualTime +
                '}';
    }
}
