package io.endeavour.stocks.dao;

import io.endeavour.stocks.vo.StockFundamentalsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockFundamentalsDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StockFundamentalsDAO.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public List<StockFundamentalsVO> getStockFundamentals (List<String> tickerList)
    {
        String sqlQuery =
                """
                                
                        SELECT
                              sf.TICKER_SYMBOL, sf.SECTOR_ID, sf.SUBSECTOR_ID, sf.MARKET_CAP, sf.CURRENT_RATIO
                              FROM
                             ENDEAVOUR.STOCK_FUNDAMENTALS sf
                              WHERE
                              sf.TICKER_SYMBOL IN (:tickerSymbols)   
                                                
                        """;


        MapSqlParameterSource mapSqlParameterSource =new MapSqlParameterSource();
        mapSqlParameterSource.addValue("tickerSymbols", tickerList);
        List<StockFundamentalsVO> stockFundamentalsVOList = namedParameterJdbcTemplate.query(sqlQuery, mapSqlParameterSource, (resultSet, rowNum) ->
        {
            StockFundamentalsVO stockFundamentalsVO = new StockFundamentalsVO();
            stockFundamentalsVO.setTickerSymbols(resultSet.getString("TICKER_SYMBOL"));
            stockFundamentalsVO.setMarketCap(resultSet.getBigDecimal("MARKET_CAP"));
            stockFundamentalsVO.setCurrentRatio(resultSet.getDouble("CURRENT_RATIO"));
            stockFundamentalsVO.setSectorID(resultSet.getInt("SECTOR_ID"));
            stockFundamentalsVO.setSubsectorID(resultSet.getInt("SUBSECTOR_ID"));
            return stockFundamentalsVO;
        });
        return stockFundamentalsVOList;
    }
}
