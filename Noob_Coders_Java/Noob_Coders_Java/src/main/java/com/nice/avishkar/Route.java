package com.nice.avishkar;


public class Route {
    private final String source;
    private final String destination;
    private final String mode;
    private final String departureTime;
    private final String arrivalTime;


    public Route(String source, String destination, String mode, String departureTime, String arrivalTime) {
        this.source = source;
        this.destination = destination;
        this.mode = mode;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getMode() {
        return mode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

}
