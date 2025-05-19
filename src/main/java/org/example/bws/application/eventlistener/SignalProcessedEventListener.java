package org.example.bws.application.eventlistener;

import org.example.bws.domain.service.SignalService;
import org.example.bws.shared.event.SignalProcessedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
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

        // 如果达到批量大小，立即处理
        if (processedSignalIds.size() >= 1000) {
            processAndSaveRemaining(1000);
        }
    }

    /**
     * 当应用关闭时，处理队列中剩余的信号ID
     */
    @PreDestroy
    public void flushRemainingSignalIds() {
        System.out.println("正在关闭应用，开始处理剩余的未持久化的信号ID...");
        processAndSaveRemaining(processedSignalIds.size());
    }

    /**
     * 从队列中取出一批ID并调用服务进行持久化
     * @param batchSize 最大处理数量
     */
    private void processAndSaveRemaining(int batchSize) {
        List<Long> batch = new ArrayList<>(batchSize);
        while (batchSize > 0 && !processedSignalIds.isEmpty()) {
            Long id = processedSignalIds.poll();
            if (id != null) {
                batch.add(id);
                batchSize--;
            }
        }

        if (!batch.isEmpty()) {
            signalService.markSignalsAsProcessed(batch);
            System.out.println("已持久化剩余信号ID数量: " + batch.size());
        }
    }
}