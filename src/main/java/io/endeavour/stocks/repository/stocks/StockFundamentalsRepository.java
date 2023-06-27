package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StockFundamentals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockFundamentalsRepository extends JpaRepository<StockFundamentals, String>
{

}
