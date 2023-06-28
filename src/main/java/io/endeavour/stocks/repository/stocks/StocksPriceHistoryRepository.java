package io.endeavour.stocks.repository.stocks;

import io.endeavour.stocks.entity.stocks.StocksPriceHistory;
import io.endeavour.stocks.entity.stocks.StocksPriceHistoryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StocksPriceHistoryRepository  extends JpaRepository<StocksPriceHistory, StocksPriceHistoryPk> {
}
