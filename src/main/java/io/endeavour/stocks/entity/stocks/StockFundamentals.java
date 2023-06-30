package io.endeavour.stocks.entity.stocks;

import io.endeavour.stocks.vo.TopStocksBySector;
import io.endeavour.stocks.vo.TopStocksBySubSectorVo;

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
                ENDEAVOUR.SECTOR_LOOKUP sl2
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

@NamedNativeQuery(name= "StockFundamentals.TopStocksBySubSector" , query = """
         WITH MARKTCP_RANK_TABLE AS (
                    SELECT
                    sl.SUBSECTOR_NAME,
                    sf.SUBSECTOR_ID,
                    sl2.SECTOR_NAME,
                    sf.TICKER_SYMBOL,
                    sl3.TICKER_NAME,
                    sf.MARKET_CAP,
                    RANK() OVER (PARTITION BY sf.SUBSECTOR_ID ORDER BY sf.MARKET_CAP desc) AS MKCP_RANK
                    FROM
                    ENDEAVOUR.STOCK_FUNDAMENTALS sf,
                    ENDEAVOUR.SUBSECTOR_LOOKUP sl,
                    ENDEAVOUR.SECTOR_LOOKUP sl2,
                    ENDEAVOUR.STOCKS_LOOKUP sl3
                    WHERE
                    sf.MARKET_CAP IS NOT NULL
                    AND
                    sf.SUBSECTOR_ID = sl.SUBSECTOR_ID
                    AND
                    sf.TICKER_SYMBOL = SL3.TICKER_SYMBOL
                    AND
                    sf.SECTOR_ID = sl.SECTOR_ID
                    AND
                    sf.SECTOR_ID = sl2.SECTOR_ID
                    )
                    SELECT
                    mrt.SUBSECTOR_NAME,
                    MRT.SUBSECTOR_ID,
                    mrt.SECTOR_NAME,
                    mrt.TICKER_SYMBOL,
                    mrt.TICKER_NAME,
                    mrt.MARKET_CAP
                    FROM MARKTCP_RANK_TABLE mrt
                    WHERE mrt.MKCP_RANK <=5
                    ORDER BY SUBSECTOR_ID
                    """, resultSetMapping = "StockFundamentals.TopStocksBySubSectorMapping")
@SqlResultSetMapping(name = "StockFundamentals.TopStocksBySubSectorMapping",
        classes = @ConstructorResult(targetClass = TopStocksBySubSectorVo.class, columns = {
                @ColumnResult(name ="SUBSECTOR_NAME", type =String.class),
                @ColumnResult(name ="SUBSECTOR_ID", type =Integer.class),
                @ColumnResult(name ="SECTOR_NAME", type =String.class),
                @ColumnResult(name ="TICKER_SYMBOL", type =String.class),
                @ColumnResult(name ="TICKER_NAME", type =String.class),
                @ColumnResult(name ="MARKET_CAP", type =BigDecimal.class),
        }))

/*@NamedNativeQuery(name = "StockFundamentals.StockByTickerSymbolAndDate", query =
        """
                SELECT\s
                sf.MARKET_CAP,
                sf.CURRENT_RATIO,
                sph.TICKER_SYMBOL,
                SPH.TRADING_DATE,
                sph.CLOSE_PRICE,
                sph.VOLUME\s
                FROM\s
                ENDEAVOUR.STOCK_FUNDAMENTALS sf,
                ENDEAVOUR.STOCKS_PRICE_HISTORY sph
                WHERE\s
                sf.MARKET_CAP IS NOT NULL
                AND\s
                sf.CURRENT_RATIO IS NOT NULL\s
                AND
                sph.CLOSE_PRICE IS NOT NULL
                AND
                sf.TICKER_SYMBOL = SPH.TICKER_SYMBOL
                AND
                sf.TICKER_SYMBOL = ?
                AND\s
                SPH.TRADING_DATE BETWEEN  TO_DATE (?, 'yyyy-mm-dd')
                AND TO_DATE (?, 'yyyy-mm-dd')
                AND
                sph.VOLUME IS NOT NULL
                """, resultSetMapping = "StockFundamentals.StockByTickerNameAndDate")
@SqlResultSetMapping(name="StockFundamentals.StockByTickerNameAndDate", classes = {
        @ConstructorResult(targetClass = StockByTickerSymbolAndTradingDateVO.class, columns = {
                @ColumnResult(name ="MARKET_CAP", type =BigDecimal.class),
                @ColumnResult(name ="CURRENT_RATIO", type =Double.class),
                @ColumnResult(name ="TICKER_SYMBOL", type =String.class),
                @ColumnResult(name ="TRADING_DATE", type = LocalDate.class),
                @ColumnResult(name ="CLOSE_PRICE", type =BigDecimal.class),
                @ColumnResult(name ="VOLUME", type =Long.class)
        })
})*/

public class StockFundamentals
{
    @Column(name = "TICKER_SYMBOL")
    @Id
    private String tickerSymbols;

    @Column(name = "SECTOR_ID")
    private Integer sectorID;

    @Column(name = "SUBSECTOR_ID")
    private Integer subSectorID;


    @Column(name = "MARKET_CAP")
    private BigDecimal marketCap;

    @Column(name = "CURRENT_RATIO")
    private double currentRatio;

    public String getTickerSymbols() {
        return tickerSymbols;
    }

    public void setTickerSymbols(String tickerSymbols) {
        this.tickerSymbols = tickerSymbols;
    }

    public Integer getSectorID() {
        return sectorID;
    }

    public void setSectorID(Integer sectorID) {
        this.sectorID = sectorID;
    }

    public Integer getSubSectorID() {
        return subSectorID;
    }

    public void setSubSectorID(Integer subSectorID) {
        this.subSectorID = subSectorID;
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
        sb.append(", subsectorID=").append(subSectorID);
        sb.append(", marketCap=").append(marketCap);
        sb.append(", currentRatio=").append(currentRatio);
        sb.append('}');
        return sb.toString();
    }
}



