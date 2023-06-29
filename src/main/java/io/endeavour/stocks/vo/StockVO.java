package io.endeavour.stocks.vo;

import java.math.BigDecimal;

public class StockVO
{
    private String tickerSymbol;

    private String tickerName;

    private BigDecimal marketCap;

    public StockVO(String tickerSymbol, String tickerName, BigDecimal marketCap) {
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
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
