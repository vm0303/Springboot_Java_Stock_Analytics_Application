package io.endeavour.stocks.dao;

import io.endeavour.stocks.mapper.StockPriceHistoryRowMapper;
import io.endeavour.stocks.vo.StockPriceHistoryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PriceHistoryDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceHistoryDAO.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List <StockPriceHistoryVo> getStockPriceHistory(List<String> tickerSymbolList, LocalDate fromDate, LocalDate toDate)
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
                    SPH.TICKER_SYMBOL IN (:tickerSymbols)
                    AND
                    SPH.TRADING_DATE BETWEEN :fromDate AND :toDate
                 """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("toDate" ,toDate);
        mapSqlParameterSource.addValue("fromDate", fromDate);
        mapSqlParameterSource.addValue("tickerSymbols", tickerSymbolList);
        LOGGER.info("Values coming from the API are {}, {}, {} " + tickerSymbolList,fromDate,toDate);
        System.out.println("Values from API are toDate: " + toDate + ", tickerSymbolList:" + tickerSymbolList + ", fromDate:" +fromDate);
        List<StockPriceHistoryVo> stockPriceHistoryVoList = namedParameterJdbcTemplate.query(sqlQuery, mapSqlParameterSource,
                (rs, rowNum)->{ //Callback method
            StockPriceHistoryVo stockPriceHistoryVo = new StockPriceHistoryVo();
            stockPriceHistoryVo.setTickerSymbol(rs.getString("TICKER_SYMBOL"));
            stockPriceHistoryVo.setTradingDate(rs.getDate("TRADING_DATE").toLocalDate());
            stockPriceHistoryVo.setOpenPrice(rs.getBigDecimal("OPEN_PRICE"));
            stockPriceHistoryVo.setClosePrice(rs.getBigDecimal("CLOSE_PRICE"));
            stockPriceHistoryVo.setVolume(rs.getLong("VOLUME"));
            return stockPriceHistoryVo;
        });
        return stockPriceHistoryVoList;

    }
}
