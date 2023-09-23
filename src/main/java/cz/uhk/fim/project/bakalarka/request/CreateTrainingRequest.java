package cz.uhk.fim.project.bakalarka.request;

import cz.uhk.fim.project.bakalarka.enumerations.Goal;

import java.time.LocalDate;

public class CreateTrainingRequest {
    LocalDate startDay;
    LocalDate raceDay;
    Goal goal;
    Integer lengthOfRaceInMeters;
    Integer wantedTimeInSeconds;
    Integer actualRunLength;
    Integer actualTimeInSeconds;

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

    public Integer getWantedTimeInSeconds() {
        return wantedTimeInSeconds;
    }

    public void setWantedTimeInSeconds(Integer wantedTimeInSeconds) {
        this.wantedTimeInSeconds = wantedTimeInSeconds;
    }

    public Integer getActualRunLength() {
        return actualRunLength;
    }

    public void setActualRunLength(Integer actualRunLength) {
        this.actualRunLength = actualRunLength;
    }

    public Integer getActualTimeInSecond() {
        return actualTimeInSeconds;
    }

    public void setActualTimeInSeconds(Integer actualTimeInSecond) {
        this.actualTimeInSeconds = actualTimeInSecond;
    }

    @Override
    public String toString() {
        return "CreateTrainingRequest{" +
                "startDay=" + startDay +
                ", raceDay=" + raceDay +
                ", goal=" + goal +
                ", lengthOfRaceInMeters=" + lengthOfRaceInMeters +
                ", wantedTimeInSeconds=" + wantedTimeInSeconds +
                ", actualRunLength=" + actualRunLength +
                ", actualTimeInSeconds=" + actualTimeInSeconds +
                '}';
    }
}
