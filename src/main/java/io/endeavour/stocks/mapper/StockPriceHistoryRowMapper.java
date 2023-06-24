package io.endeavour.stocks.mapper;

import io.endeavour.stocks.vo.StockPriceHistoryVo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockPriceHistoryRowMapper implements RowMapper<StockPriceHistoryVo> {
    @Override
    public StockPriceHistoryVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        StockPriceHistoryVo stockPriceHistoryVo = new StockPriceHistoryVo();
        stockPriceHistoryVo.setTickerSymbol(rs.getString("TICKER_SYMBOL"));
        stockPriceHistoryVo.setTradingDate(rs.getDate("TRADING_DATE").toLocalDate());
        stockPriceHistoryVo.setOpenPrice(rs.getBigDecimal("OPEN_PRICE"));
        stockPriceHistoryVo.setOpenPrice(rs.getBigDecimal("CLOSE_PRICE"));
        stockPriceHistoryVo.setVolume(rs.getLong("VOLUME"));
        return stockPriceHistoryVo;
    }
}
