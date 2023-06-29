package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.vo.TopStocksBySector;
import io.endeavour.stocks.vo.TopStocksBySubSectorVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockFundamentalsRepository extends JpaRepository<StockFundamentals, String>
{
@Query(name = "StockFundamentals.TopStocksBySector", nativeQuery = true)
List<TopStocksBySector> getTopStocksBySector();

@Query(name = "StockFundamentals.TopStocksBySubSector", nativeQuery = true)
List<TopStocksBySubSectorVo> getTopStocksBySubSector();


@Query(nativeQuery = true, value = """
        SELECT *
        FROM ENDEAVOUR.STOCK_FUNDAMENTALS sf
        WHERE
        sf.MARKET_CAP IS NOT NULL
        ORDER BY MARKET_CAP DESC
        FETCH FIRST :num ROWS ONLY
        """)
    List<StockFundamentals> getTopNStocksUsingNativeSQL(@Param(value ="num") Integer num);

@Query(value = "SELECT sf FROM StockFundamentals sf where sf.marketCap is not null order by sf.marketCap desc")
    List<StockFundamentals> getAllStocksFromStockFundamentals();
}
