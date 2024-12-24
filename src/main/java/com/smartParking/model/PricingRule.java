package com.smartParking.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class PricingRule {
    private int ruleId;
    private int lotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal pricePerHour;

    // Constructors
    public PricingRule(int ruleId, int lotId, LocalTime startTime, LocalTime endTime, BigDecimal pricePerHour) {
        this.ruleId = ruleId;
        this.lotId = lotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pricePerHour = pricePerHour;
    }

    public PricingRule(int lotId, LocalTime startTime, LocalTime endTime, BigDecimal pricePerHour) {
        this.lotId = lotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pricePerHour = pricePerHour;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
