package com.crepestrips.adminservice.observer;


import com.crepestrips.adminservice.dto.ReportDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportSubject {

    private final List<ReportObserver> observers = new ArrayList<ReportObserver>();

    public void addObserver(ReportObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(ReportObserver observer) {
        observers.remove(observer);
    }
    public void notifyObservers(ReportDTO report) {
        for (ReportObserver observer : observers) {
            observer.update(report);
        }
    }
}
