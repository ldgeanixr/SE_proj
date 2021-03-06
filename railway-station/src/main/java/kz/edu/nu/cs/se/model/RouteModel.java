package kz.edu.nu.cs.se.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RouteModel {
    private Integer routeId;
    private String origin;
    private String destination;
    private String startDate;
    private String endDate;
    private LocalDateTime startDateObject;
    private LocalDateTime endDateObject;
    private Integer price;
    private Integer trainId;
    private Integer capacity;

    public RouteModel(LocalDateTime startTime, LocalDateTime  endTime) {
        this.startDateObject = startTime;
        this.endDateObject = endTime;
    }

    public RouteModel(String origin, String destination, LocalDateTime startDateObject, LocalDateTime endDateObject, Integer routeId, Integer capacity, Integer price) {
        this.origin = origin;
        this.destination = destination;
        this.startDateObject = startDateObject;
        this.endDateObject = endDateObject;
        this.routeId = routeId;
        this.capacity = capacity;
        this.price = price;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public String getDestination() {
        return this.destination;
    }

    public LocalDateTime getStartDateObject() {
        return this.startDateObject;
    }

    public LocalDateTime getEndDateObject() {
        return this.endDateObject;
    }

    public void setStringDates(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");
        startDate = startDateObject.format(formatter);
        endDate = endDateObject.format(formatter);
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
