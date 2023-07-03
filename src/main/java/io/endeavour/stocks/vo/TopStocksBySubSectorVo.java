package io.endeavour.stocks.vo;

import javax.persistence.Transient;
import java.math.BigDecimal;

public class TopStocksBySubSectorVo
{
    private String subSectorName;

    private Integer subSectorID;

    private String sectorName;

    private String tickerSymbol;

    private String tickerName;

    private BigDecimal marketCap;


    private BigDecimal cumulativeReturn;

    public TopStocksBySubSectorVo(String subSectorName, Integer subSectorID, String sectorName, String tickerSymbol, String tickerName, BigDecimal marketCap) {
        this.subSectorName = subSectorName;
        this.subSectorID = subSectorID;
        this.sectorName = sectorName;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public void setSubSectorID(Integer subSectorID) {
        this.subSectorID = subSectorID;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
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
