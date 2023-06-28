package io.endeavour.stocks.entity.stocks;

import io.swagger.v3.oas.annotations.tags.Tags;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "STOCKS_PRICE_HISTORY", schema = "ENDEAVOUR")
@IdClass(StocksPriceHistoryPk.class)
public class StocksPriceHistory
{
    @Column(name = "TICKER_SYMBOL")
    @Id
    private String tickerSymbol;

    @Column(name = "TRADING_DATE")
    @Id
    private LocalDate tradingDate;

    @Column(name = "OPEN_PRICE")
    private BigDecimal openPrice;

    @Column(name = "CLOSE_PRICE")
    private BigDecimal closePrice;

    @Column(name = "VOLUME")
    private long volume;


    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StocksPriceHistory that = (StocksPriceHistory) o;
        return Objects.equals(tickerSymbol, that.tickerSymbol) && Objects.equals(tradingDate, that.tradingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol, tradingDate);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StocksPriceHistory{");
        sb.append("tickerSymbol='").append(tickerSymbol).append('\'');
        sb.append(", tradingDate=").append(tradingDate);
        sb.append(", openPrice=").append(openPrice);
        sb.append(", closePrice=").append(closePrice);
        sb.append(", volume=").append(volume);
        sb.append('}');
        return sb.toString();
    }
}
