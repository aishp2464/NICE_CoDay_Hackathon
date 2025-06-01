package com.nice.avishkar;

public class CustomerRequest{
    private final String requestId;
    private final String customerName;
    private final String source;
    private final String destination;
    private final String criteria;

    public CustomerRequest(String requestId, String customerName, String source, String destination, String criteria) {
        this.requestId = requestId;
        this.customerName = customerName;
        this.source = source;
        this.destination = destination;
        this.criteria = criteria;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCustomerName() {
        return customerName;
    }


    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "Input{" +
                "requestId='" + requestId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", criteria='" + criteria + '\'' +
                '}';
    }
}