package org.example.bws;

import org.example.bws.infrastructure.repository.BufferedLogSaver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.rmi.server.LogStream.log;
import static org.junit.jupiter.api.Assertions.*;

class BufferedLogSaverTest {

    private static final Logger log = LoggerFactory.getLogger(BufferedLogSaverTest.class);
    private BufferedLogSaver logSaver;
    private Path tempLogFile;

    @BeforeEach
    void setUp() throws IOException {
        tempLogFile = Files.createTempFile("test_log", ".txt");
        logSaver = new BufferedLogSaver();
        logSaver.init();
    }

    @Test
    void testBufferedLogging() throws InterruptedException, IOException {
        // 写入15条日志（超过缓冲区大小10）
        for (int i = 0; i < 15; i++) {
           log("Test log line " + i);
        }

        // 等待刷新线程执行
        Thread.sleep(2000);

        // 验证日志文件内容
        List<String> lines = Files.readAllLines(tempLogFile);
        assertEquals(15, lines.size(), "日志文件应包含15条记录");
        for (int i = 0; i < 15; i++) {
            assertTrue(lines.get(i).contains("Test log line " + i), "日志内容不匹配");
        }
    }

    @Test
    void testConcurrentLogging() throws InterruptedException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(100);

        // 并发写入100条日志
        for (int i = 0; i < 100; i++) {
            final int lineNum = i;
            executor.submit(() -> {
                try {
                    log("Concurrent log line " + lineNum);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // 等待刷新线程执行
        Thread.sleep(2000);

        // 验证日志文件内容
        List<String> lines = Files.readAllLines(tempLogFile);
        assertEquals(100, lines.size(), "日志文件应包含100条记录");
    }

    @Test
    void testFlushOnShutdown() throws IOException {
        // 写入5条日志（未达到缓冲区大小）
        for (int i = 0; i < 5; i++) {
            log("Pre-shutdown log line " + i);
        }

        // 关闭日志保存器（应强制刷新缓冲区）
        logSaver.destroy();

        // 验证日志文件内容
        List<String> lines = Files.readAllLines(tempLogFile);
        assertEquals(5, lines.size(), "关闭时应刷新剩余日志");
    }
}    