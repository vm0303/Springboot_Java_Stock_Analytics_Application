package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.util.Objects;

public class StockFundamentalsVO
{
    private String tickerSymbols;

    private int sectorID;

    private int subsectorID;
    private BigDecimal marketCap;

    private double currentRatio;

    public String getTickerSymbols() {
        return tickerSymbols;
    }

    public void setTickerSymbols(String tickerSymbols) {
        this.tickerSymbols = tickerSymbols;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public double getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(double currentRatio) {
        this.currentRatio = currentRatio;
    }

    public int getSectorID() {
        return sectorID;
    }

    public void setSectorID(int sectorID) {
        this.sectorID = sectorID;
    }

    public int getSubsectorID() {
        return subsectorID;
    }

    public void setSubsectorID(int subsectorID) {
        this.subsectorID = subsectorID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockFundamentalsVO that = (StockFundamentalsVO) o;
        return Objects.equals(tickerSymbols, that.tickerSymbols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbols);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StockFundamentalsVO{");
        sb.append("tickerSymbols='").append(tickerSymbols).append('\'');
        sb.append(", sectorID=").append(sectorID);
        sb.append(", subsectorID=").append(subsectorID);
        sb.append(", marketCap=").append(marketCap);
        sb.append(", currentRatio=").append(currentRatio);
        sb.append('}');
        return sb.toString();
    }
}

