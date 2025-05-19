package org.example.bws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bws.domain.model.SignalReport;
import org.example.bws.infrastructure.mq.producer.WarmMessageProducer;
import org.example.bws.infrastructure.repository.SignalReportRepository;
import org.example.bws.shared.utils.SignalVOTypeHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MqTest {

    @Autowired
    WarmMessageProducer warmMessageProducer;

    @Autowired
    SignalReportRepository signalReportRepository;

    @Autowired
    @Qualifier("customObjectMapper")
    private ObjectMapper objectMapper;

    @Test
    public void test() throws JsonProcessingException, InterruptedException {
        List<SignalReport> signalReports = signalReportRepository.selectByHandledStatus(false);
        signalReports.forEach(System.out::println);

        for (SignalReport signalReport : signalReports) {
            String json = objectMapper.writeValueAsString(signalReport);
            System.out.println(json);

            // 发送到消息队列
            warmMessageProducer.sendMessage("warm-message", json);
        }
        Thread.sleep(5000);
    }

}
