package io.endeavour.stocks.vo;

import javax.persistence.Transient;
import java.math.BigDecimal;

public class StockVO
{
    private String tickerSymbol;

    private String tickerName;

    private BigDecimal marketCap;


    private BigDecimal cumulativeReturn;

    public StockVO(String tickerSymbol, String tickerName, BigDecimal marketCap) {
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;

    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public void setCumulativeReturn(BigDecimal cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }

    public BigDecimal getCumulativeReturn() {
        return cumulativeReturn;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }
}
