package org.example.bws.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.bws.domain.model.WarnInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

@Component
public class BufferedLogSaver {
    private static final String BASE_DIR = "logs";
    // 每次 flush 最多写入多少条
    private static final int MAX_ENTRIES_PER_FLUSH = 10000;
    private static final int BUFFER_SIZE = 2000;
    private static final int FLUSH_INTERVAL_SECONDS = 3;

    private final BlockingQueue<WarnInfo> queue = new LinkedBlockingQueue<>(BUFFER_SIZE);
    private ScheduledExecutorService scheduler;
    private Path currentFile = null;
    private BufferedWriter writer = null;
    private LocalDate lastDate = LocalDate.now();

    private ObjectMapper mapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.mapper = objectMapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void init() throws IOException {
        // 初始化目录和 writer
        lastDate = LocalDate.now();
        rotateFile(lastDate);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::flush, FLUSH_INTERVAL_SECONDS, FLUSH_INTERVAL_SECONDS, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(this::flushAndClose));
    }

    @PreDestroy
    public void destroy() {
        flushAndClose();
        scheduler.shutdownNow();
    }

    public void save(WarnInfo info) {
        try {
            queue.put(info);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void flush() {
        if (queue.isEmpty()) return;

        try {
            LocalDate now = LocalDate.now();
            if (!now.equals(lastDate)) {
                rotateFile(now);
            }

            int count = 0;
            while (!queue.isEmpty() && count < MAX_ENTRIES_PER_FLUSH) {
                WarnInfo info = queue.poll();
                if (info == null) break;

                String json = mapper.writeValueAsString(info);
                writer.write(json + "\n");
                count++;
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flushAndClose() {
        try {
            flush();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rotateFile(LocalDate newDate) throws IOException {
        if (writer != null) {
            writer.close();
        }

        lastDate = newDate;
        String dateStr = newDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        Path dir = Paths.get(BASE_DIR, dateStr);
        Files.createDirectories(dir);

        int fileIndex = getCurrentFileIndex(dir, dateStr);
        currentFile = dir.resolve(String.format("%s_%d.json", dateStr, fileIndex));
        writer = Files.newBufferedWriter(currentFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private int getCurrentFileIndex(Path dir, String dateStr) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, dateStr + "_*.json")) {
            int maxIndex = 0;
            for (Path file : stream) {
                String name = file.getFileName().toString();
                int indexStart = dateStr.length() + 1;
                int indexEnd = name.length() - 5;
                if (indexStart >= indexEnd) continue;

                int index = Integer.parseInt(name.substring(indexStart, indexEnd));
                maxIndex = Math.max(maxIndex, index);
            }
            return maxIndex + 1;
        } catch (NoSuchFileException e) {
            return 0;
        }
    }
}