package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.vo.StockFundamentalsHistoryVO;
import io.endeavour.stocks.vo.TopStocksBySector;
import io.endeavour.stocks.vo.TopStocksBySubSectorVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockFundamentalsRepository extends JpaRepository<StockFundamentals, String>
{
@Query(name = "StockFundamentals.TopStocksBySector", nativeQuery = true)
List<TopStocksBySector> getTopStocksBySector();

@Query(name = "StockFundamentals.TopStocksBySubSector", nativeQuery = true)
List<TopStocksBySubSectorVo> getTopStocksBySubSector();

/*
@Query(name = "StockFundamentals.StockByTickerSymbolAndDate", nativeQuery = true)
List<StockByTickerSymbolAndTradingDateVO>
getStockByTickerSymbolAndDate(String tickerSymbol, LocalDate fromDate, LocalDate toDate);
*/

@Query(nativeQuery = true, value = """
        SELECT *
        FROM ENDEAVOUR.STOCK_FUNDAMENTALS sf
        WHERE
        sf.MARKET_CAP IS NOT NULL
        ORDER BY MARKET_CAP DESC
        FETCH FIRST :num ROWS ONLY
        """)
    List<StockFundamentals> getTopNStocksUsingNativeSQL(@Param(value ="num") Integer num);

@Query(nativeQuery = true, value = """
                SELECT
                        sf.MARKET_CAP,
                        sf.CURRENT_RATIO,
                        sph.TICKER_SYMBOL,
                        SPH.TRADING_DATE,
                        sph.CLOSE_PRICE,
                        sph.VOLUME
                 FROM
                        ENDEAVOUR.STOCK_FUNDAMENTALS sf,
                        ENDEAVOUR.STOCKS_PRICE_HISTORY sph
                 WHERE
                        sf.MARKET_CAP IS NOT NULL
                 AND
                        sf.CURRENT_RATIO IS NOT NULL
                 AND
                        sph.CLOSE_PRICE IS NOT NULL
                 AND
                        sf.TICKER_SYMBOL = SPH.TICKER_SYMBOL
                 AND
                        sf.TICKER_SYMBOL = ?1
                 AND
                        SPH.TRADING_DATE BETWEEN  TO_DATE (?2, 'yyyy-mm-dd')
                        AND TO_DATE (?3, 'yyyy-mm-dd')
                 AND
                        sph.VOLUME IS NOT NULL
        """)
List<StockFundamentalsHistoryVO> getStockByTickerSymbolAndDate(String tickerSymbol, LocalDate fromDate, LocalDate toDate);


@Query(value = "SELECT sf FROM StockFundamentals sf where sf.marketCap is not null order by sf.marketCap desc")
    List<StockFundamentals> getAllStocksFromStockFundamentals();
}
