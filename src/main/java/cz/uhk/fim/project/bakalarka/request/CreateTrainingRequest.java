package cz.uhk.fim.project.bakalarka.request;

import cz.uhk.fim.project.bakalarka.enumerations.Goal;

import java.time.Duration;
import java.time.LocalDate;

public class CreateTrainingRequest {
    LocalDate startDay;
    LocalDate raceDay;
    Goal goal;
    Integer lengthOfRaceInMeters;
    Duration wantedTime;
    Integer actualRunLength;
    Duration actualTime;

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

    public Duration getWantedTime() {
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

    public Duration getActualTime() {
        return actualTime;
    }

    public void setActualTime(Duration actualTimeInSecond) {
        this.actualTime = actualTimeInSecond;
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
