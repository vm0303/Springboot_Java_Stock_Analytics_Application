package io.endeavour.stocks.entity.stocks;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "STOCK_FUNDAMENTALS", schema = "ENDEAVOUR")
public class StockFundamentals
{
    @Column(name = "TICKER_SYMBOL")
    @Id
    private String tickerSymbols;

    @Column(name = "SECTOR_ID")
    private int sectorID;

    @Column(name = "SUBSECTOR_ID")
    private int subsectorID;


    @Column(name = "MARKET_CAP")
    private BigDecimal marketCap;

    @Column(name = "CURRENT_RATIO")
    private BigDecimal currentRatio;

    public String getTickerSymbols() {
        return tickerSymbols;
    }

    public void setTickerSymbols(String tickerSymbols) {
        this.tickerSymbols = tickerSymbols;
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

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockFundamentals that = (StockFundamentals) o;
        return Objects.equals(tickerSymbols, that.tickerSymbols);
    }



    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbols);
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StockFundamentals{");
        sb.append("tickerSymbols='").append(tickerSymbols).append('\'');
        sb.append(", sectorID=").append(sectorID);
        sb.append(", subsectorID=").append(subsectorID);
        sb.append(", marketCap=").append(marketCap);
        sb.append(", currentRatio=").append(currentRatio);
        sb.append('}');
        return sb.toString();
    }
}



