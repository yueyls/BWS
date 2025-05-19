package org.example.bws.shared.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class SignalProcessedEvent extends ApplicationEvent {
    private final List<Long> processedSignalIds;

    public SignalProcessedEvent(Object source, List<Long> processedSignalIds) {
        super(source);
        this.processedSignalIds = processedSignalIds;
    }

    public List<Long> getProcessedSignalIds() {
        return processedSignalIds;
    }
}