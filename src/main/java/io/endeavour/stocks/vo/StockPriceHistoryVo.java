package io.endeavour.stocks.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Schema(name = "StocksPriceHistoryVO", description = "Contains a list of stocks and its history")
public class StockPriceHistoryVo {

    @Schema(name = "tickerSymbol", example = "TSLA")
    private String tickerSymbol;

    @Schema(name = "tradingDate", example = "2023-05-15")
    private LocalDate tradingDate;

    @Schema(name = "openPrice", example = "135.32")
    private BigDecimal openPrice;

    @Schema(name = "closePrice", example = "156.43")
    private BigDecimal closePrice;

    @Schema(name = "volume", example = "1232343538")
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
        StockPriceHistoryVo that = (StockPriceHistoryVo) o;
        return Objects.equals(tickerSymbol, that.tickerSymbol) && Objects.equals(tradingDate, that.tradingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol, tradingDate);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StockPriceHistoryVo{");
        sb.append("tickerSymbol='").append(tickerSymbol).append('\'');
        sb.append(", tradingDate=").append(tradingDate);
        sb.append(", openPrice=").append(openPrice);
        sb.append(", closePrice=").append(closePrice);
        sb.append(", volume=").append(volume);
        sb.append('}');
        return sb.toString();
    }
}
