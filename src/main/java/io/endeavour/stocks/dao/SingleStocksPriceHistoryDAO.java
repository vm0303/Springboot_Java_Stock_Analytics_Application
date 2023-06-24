package io.endeavour.stocks.dao;

import io.endeavour.stocks.mapper.StockPriceHistoryRowMapper;
import io.endeavour.stocks.vo.StockPriceHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SingleStocksPriceHistoryDAO
{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<StockPriceHistoryVo> stockPriceHistoryVoList (String tickerSymbol, LocalDate fromDate, LocalDate toDate)
    {
        String sqlQuery =
                    """
                    SELECT
                        sph.TICKER_SYMBOL,
                        sph.TRADING_DATE,
                        sph.OPEN_PRICE,
                        sph.CLOSE_PRICE,
                        sph.VOLUME
                    FROM ENDEAVOUR.STOCKS_PRICE_HISTORY sph
                    WHERE
                        SPH.TICKER_SYMBOL = ?
                        AND
                        SPH.TRADING_DATE BETWEEN ? AND ?
                     """;

        //This acts as an analog to retrieve data
        Object[] inputParameters = new Object[]{tickerSymbol, fromDate, toDate};

        List<StockPriceHistoryVo> stockPriceHistoryVoList = jdbcTemplate.query(sqlQuery, inputParameters, new StockPriceHistoryRowMapper());
        return stockPriceHistoryVoList;
    }
}
