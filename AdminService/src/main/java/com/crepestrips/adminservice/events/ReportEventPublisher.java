//package com.crepestrips.adminservice.events;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class ReportEventPublisher implements ReportSubject {
//    private final List<ReportObserver> observers = new ArrayList<>();
//
//    // Register observers (can be done dynamically via reflection or hardcoded)
//    @PostConstruct
//    public void initializeObservers() {
//        // Example: Manually register observers (or use reflection later)
//        registerObserver(new AdminNotificationListener());
//    }
//
//    @Override
//    public void registerObserver(ReportObserver observer) {
//        observers.add(observer);
//    }
//
//    @Override
//    public void removeObserver(ReportObserver observer) {
//        observers.remove(observer);
//    }
//
//    @Override
//    public void notifyObservers(Report report) {
//        observers.forEach(observer -> observer.handleReport(report));
//    }
//}
