package com.StockAppBackend.fullstackbackend.dto;

import java.math.BigDecimal;

public class BalanceReport {

    private String idinfo;
    private BigDecimal incomeAmount;
    private BigDecimal expenseAmount;
    private int number;

    public BalanceReport(){}
    public BalanceReport(String idinfo, BigDecimal incomeAmount, BigDecimal expenseAmount, int number) {
        this.idinfo = idinfo;
        this.incomeAmount = incomeAmount;
        this.expenseAmount = expenseAmount;
        this.number = number;
    }

    public String getIdinfo() {
        return idinfo;
    }

    public void setIdinfo(String idinfo) {
        this.idinfo = idinfo;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
