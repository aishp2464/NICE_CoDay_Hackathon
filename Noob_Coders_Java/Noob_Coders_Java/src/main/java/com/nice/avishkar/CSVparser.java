package com.nice.avishkar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVparser {
    public static List<CustomerRequest> readInputFile(Path inputFile) {
        List<CustomerRequest> inputList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile.toString()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 5) {
                    String requestId = values[0].trim();
                    String customerName = values[1].trim();
                    String source = values[2].trim();
                    String destination = values[3].trim();
                    String criteria = values[4].trim();

                    CustomerRequest input = new CustomerRequest(requestId, customerName, source, destination, criteria);
                    inputList.add(input);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputList;
    }

    public static Map<Route, Long> readSchedules(Path schPath) {
        Map<Route, Long> scheduleMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(schPath.toString()))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String source = parts[0].trim();
                    String destination = parts[1].trim();
                    String mode = parts[2].trim();
                    String departureTime = parts[3].trim();
                    String arrivalTime = parts[4].trim();
                    long cost = Long.parseLong(parts[5].trim()); // Changed to Long

                    Route route = new Route(source, destination, mode, departureTime, arrivalTime);
                    scheduleMap.put(route, cost);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return scheduleMap;
    }
}