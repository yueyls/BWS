package org.example.bws.domain.model;

import java.time.LocalDateTime;

public class SignalReport {
    private Long id;
    private int carId;
    private int warnId;
    private LocalDateTime reportTime;
    private SignalVO signalData;
    private Boolean isHandled;

    public SignalReport() {}

    @Override
    public String toString() {
        return "SignalReport{" +
                "id=" + id +
                ", carId=" + carId +
                ", warnId=" + warnId +
                ", reportTime=" + reportTime +
                ", signalData=" + signalData +
                ", isHandled=" + isHandled +
                '}';
    }

    public SignalReport(Long id, int warnId, int carId, LocalDateTime reportTime, SignalVO signalData, Boolean isHandled) {
        this.id = id;
        this.warnId = warnId;
        this.carId = carId;
        this.reportTime = reportTime;
        this.signalData = signalData;
        this.isHandled = isHandled;
    }

    public int getWarnId() {
        return warnId;
    }

    public void setWarnId(int warnId) {
        this.warnId = warnId;
    }

    public Boolean getIsHandled() {
        return isHandled;
    }

    public void setIsHandled(Boolean handled) {
        isHandled = handled;
    }

    public SignalReport(int carId, Long id, LocalDateTime reportTime, SignalVO signalData, Boolean isHandled) {
        this.carId = carId;
        this.id = id;
        this.reportTime = reportTime;
        this.signalData = signalData;
        this.isHandled = isHandled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public SignalVO getSignalData() {
        return signalData;
    }

    public void setSignalData(SignalVO signalData) {
        this.signalData = signalData;
    }

}
