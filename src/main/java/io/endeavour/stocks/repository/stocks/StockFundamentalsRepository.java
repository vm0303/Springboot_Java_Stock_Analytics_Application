package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.vo.TopStocksBySector;
import io.endeavour.stocks.vo.TopStocksBySubSectorVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockFundamentalsRepository extends JpaRepository<StockFundamentals, String>
{
@Query(name = "StockFundamentals.TopStocksBySector", nativeQuery = true)
List<TopStocksBySector> getTopStocksBySector();

@Query(name = "StockFundamentals.TopStocksBySubSector", nativeQuery = true)
List<TopStocksBySubSectorVo> getTopStocksBySubSector();
}
