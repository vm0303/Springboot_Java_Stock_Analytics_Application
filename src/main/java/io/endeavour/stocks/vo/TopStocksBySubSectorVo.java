package io.endeavour.stocks.vo;

import java.math.BigDecimal;

public class TopStocksBySubSectorVo
{
    private String subSectorName;

    private Integer subSectorID;

    private String sectorName;

    private String tickerSymbol;

    private String tickerName;

    private BigDecimal marketCap;

    public TopStocksBySubSectorVo(String subSectorName, Integer subSectorID, String sectorName, String tickerSymbol, String tickerName, BigDecimal marketCap) {
        this.subSectorName = subSectorName;
        this.subSectorID = subSectorID;
        this.sectorName = sectorName;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public Integer getSubSectorID() {
        return subSectorID;
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
