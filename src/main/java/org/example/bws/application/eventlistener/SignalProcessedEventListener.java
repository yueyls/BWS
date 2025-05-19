package org.example.bws.application.eventlistener;

import org.example.bws.domain.service.SignalService;
import org.example.bws.shared.event.SignalProcessedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class SignalProcessedEventListener {

    private final ConcurrentLinkedQueue<Long> processedSignalIds = new ConcurrentLinkedQueue<>();

    @Autowired
    private SignalService signalService;

    @EventListener
    public void handleSignalProcessedEvent(SignalProcessedEvent event) {
        List<Long> ids = event.getProcessedSignalIds();
        processedSignalIds.addAll(ids);


        if (processedSignalIds.size() >= 3) {
            List<Long> batch = new ArrayList<>(100);
            while (batch.size() < 100 && !processedSignalIds.isEmpty()) {
                Long id = processedSignalIds.poll();
                if (id != null) {
                    batch.add(id);
                }
            }

            // 调用服务批量更新状态为已处理
            signalService.markSignalsAsProcessed(batch);
        }
    }
}