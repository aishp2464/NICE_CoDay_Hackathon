// Coday Competition 2025 - All the code provided in this file is part of the solution to the problem statement.
// Team Name - Noob Coders
package com.nice.avishkar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class TravelOptimizerImpl implements ITravelOptimizer {

    public static Map<String, Map<Route, Long>> buildGraph(Map<Route, Long> scheduleMap) {
        Map<String, Map<Route, Long>> graph = new HashMap<>();
        scheduleMap.forEach((route, cost) -> 
            graph.computeIfAbsent(route.getSource(), k -> new HashMap<>()).put(route, cost)
        );
        return graph;
    }

    @Override
    public Map<String, OptimalTravelSchedule> getOptimalTravelOptions(ResourceInfo resourceInfo) throws IOException {
        Map<Route, Long> scheduleMap = CSVparser.readSchedules(resourceInfo.transportSchedulePath);
        List<CustomerRequest> inputs = CSVparser.readInputFile(resourceInfo.customerRequestPath);
        Map<String, Map<Route, Long>> graph = buildGraph(scheduleMap);
        Map<String, OptimalTravelSchedule> result = new HashMap<>();

        for (CustomerRequest input : inputs) {
            OptimalTravelSchedule optimalSchedule = findOptimalPath(
                graph, scheduleMap, input.getSource(), input.getDestination(), input.getCriteria().toLowerCase()
            );
            result.put(input.getRequestId(), 
                optimalSchedule != null ? optimalSchedule : new OptimalTravelSchedule(Collections.emptyList(), input.getCriteria(), 0)
            );
        }
        return result;
    }

    private OptimalTravelSchedule findOptimalPath(Map<String, Map<Route, Long>> graph, Map<Route, Long> scheduleMap, String source, String dest, String criteria) {
        if (!graph.containsKey(source) || !graph.containsKey(dest)) {
            return null;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingLong((Node n) -> n.value)
                .thenComparingLong(n -> n.totalCost)
                .thenComparingLong(n -> n.hops));
        Set<String> visited = new HashSet<>();
        pq.offer(new Node(source, 0, 0, 0, 0, new ArrayList<>(), "00:00"));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.location.equals(dest)) {
                return new OptimalTravelSchedule(new ArrayList<>(current.path), criteria, current.value);
            }

            if (visited.contains(current.location)) {
                continue;
            }
            visited.add(current.location);

            for (Map.Entry<Route, Long> entry : graph.getOrDefault(current.location, Collections.emptyMap()).entrySet()) {
                Route route = entry.getKey();
                long cost = entry.getValue();

                if (!current.path.isEmpty() && !current.path.get(current.path.size() - 1).getDestination().equals(route.getSource())) {
                    continue;
                }

                long travelTime = calculateTravelTime(route.getDepartureTime(), route.getArrivalTime());
                long waitTime = current.path.isEmpty() ? 0 : calculateWaitTime(current.path.get(current.path.size() - 1).getArrivalTime(), route.getDepartureTime());
                long newTime = current.totalTime + travelTime + waitTime;
                long newCost = current.totalCost + cost;
                long newHops = current.hops + 1;

                long newValue = calculateCriteriaValue(criteria, newTime, newCost, newHops);

                List<Route> newPath = new ArrayList<>(current.path);
                newPath.add(route);
                pq.offer(new Node(route.getDestination(), newValue, newTime, newCost, newHops, newPath, route.getArrivalTime()));
            }
        }
        return null;
    }

    private long calculateCriteriaValue(String criteria, long time, long cost, long hops) {
        switch (criteria) {
            case "time": return time;
            case "cost": return cost;
            case "hops": return hops;
            default: throw new IllegalArgumentException("Invalid criteria: " + criteria);
        }
    }

    private long calculateTravelTime(String depTime, String arrTime) {
        int depMin = timeToMinutes(depTime);
        int arrMin = timeToMinutes(arrTime);
        return (arrMin < depMin ? arrMin + 24 * 60 : arrMin) - depMin;
    }

    private long calculateWaitTime(String lastArrival, String depTime) {
        int lastMin = timeToMinutes(lastArrival);
        int depMin = timeToMinutes(depTime);
        return Math.max(0, (depMin < lastMin ? depMin + 24 * 60 : depMin) - lastMin);
    }

    private int timeToMinutes(String timeStr) {
        String[] parts = timeStr.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private static class Node {
        String location;
        long value;
        long totalTime;
        long totalCost;
        long hops;
        List<Route> path;
        String lastArrival;

        Node(String location, long value, long totalTime, long totalCost, long hops, List<Route> path, String lastArrival) {
            this.location = location;
            this.value = value;
            this.totalTime = totalTime;
            this.totalCost = totalCost;
            this.hops = hops;
            this.path = path;
            this.lastArrival = lastArrival;
        }
    }
}