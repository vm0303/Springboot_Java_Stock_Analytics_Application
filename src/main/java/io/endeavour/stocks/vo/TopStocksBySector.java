package io.endeavour.stocks.vo;

import java.math.BigDecimal;

public class TopStocksBySector
{
    private Integer sectorID;

    private String sectorName;

    private String tickerSymbol;

    private String tickerName;

    private BigDecimal marketCap;


    public TopStocksBySector(Integer sectorID, String sectorName, String tickerSymbol, String tickerName, BigDecimal marketCap) {
        this.sectorID = sectorID;
        this.sectorName = sectorName;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
    }

    public Integer getSectorID() {
        return sectorID;
    }

    public String getSectorName() {
        return sectorName;
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
