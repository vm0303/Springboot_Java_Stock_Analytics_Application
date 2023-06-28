package io.endeavour.stocks.entity.stocks;

import io.endeavour.stocks.vo.TopStocksBySector;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "STOCK_FUNDAMENTALS", schema = "ENDEAVOUR")
@NamedNativeQuery(name="StockFundamentals.TopStocksBySector", query = """
        WITH MARKETCP_RANK_TABLE AS (
            SELECT
                sf.SECTOR_ID,
                sl2.SECTOR_NAME,
                sf.TICKER_SYMBOL,
                sl.TICKER_NAME,
                sf.MARKET_CAP,
                RANK() OVER (PARTITION BY sf.SECTOR_ID ORDER BY sf.MARKET_CAP desc) AS MKCP_RANK
            FROM
                ENDEAVOUR.STOCK_FUNDAMENTALS sf,
                ENDEAVOUR.STOCKS_LOOKUP sl,
                ENDEAVOUR.SECTOR_LOOKUP sl2\s
            WHERE
                sf.MARKET_CAP IS NOT NULL
                AND sf.TICKER_SYMBOL = sl.TICKER_SYMBOL
                AND sf.SECTOR_ID = sl2.SECTOR_ID
        )
        SELECT
                mrt.SECTOR_ID,
                mrt.SECTOR_NAME,
                mrt.TICKER_SYMBOL,
                mrt.TICKER_NAME,
                mrt.MARKET_CAP
        FROM
           MARKETCP_RANK_TABLE mrt
        WHERE
            mrt.MKCP_RANK=1
        """ ,resultSetMapping = "StockFundamentals.TopStocksBySectorMapping")
@SqlResultSetMapping(name = "StockFundamentals.TopStocksBySectorMapping",
classes = @ConstructorResult(targetClass = TopStocksBySector.class, columns = {
        @ColumnResult(name ="SECTOR_ID", type =Integer.class),
        @ColumnResult(name ="SECTOR_NAME", type =String.class),
        @ColumnResult(name ="TICKER_SYMBOL", type =String.class),
        @ColumnResult(name ="TICKER_NAME", type =String.class),
        @ColumnResult(name ="MARKET_CAP", type =BigDecimal.class),
}))
public class StockFundamentals
{
    @Column(name = "TICKER_SYMBOL")
    @Id
    private String tickerSymbols;

    @Column(name = "SECTOR_ID")
    private Integer sectorID;

    @Column(name = "SUBSECTOR_ID")
    private Integer subsectorID;


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

    public Integer getSectorID() {
        return sectorID;
    }

    public void setSectorID(int sectorID) {
        this.sectorID = sectorID;
    }

    public Integer getSubsectorID() {
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



