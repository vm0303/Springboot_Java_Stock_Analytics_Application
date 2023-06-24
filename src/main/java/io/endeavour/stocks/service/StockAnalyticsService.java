package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.PriceHistoryDAO;
import io.endeavour.stocks.dao.SingleStocksPriceHistoryDAO;
import io.endeavour.stocks.vo.StockPriceHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockAnalyticsService
{

    private SingleStocksPriceHistoryDAO singleStocksPriceHistoryDAO;
    private PriceHistoryDAO priceHistoryDAO;



    @Autowired
    public StockAnalyticsService(SingleStocksPriceHistoryDAO singleStocksPriceHistoryDAO, PriceHistoryDAO priceHistoryDAO) {
        this.singleStocksPriceHistoryDAO = singleStocksPriceHistoryDAO;
        this.priceHistoryDAO = priceHistoryDAO;
    }



    public List<StockPriceHistoryVo> singleStocksPriceHistoryDAOList(String tickerSymbol, LocalDate fromDate, LocalDate toDate)
    {
        return singleStocksPriceHistoryDAO.stockPriceHistoryVoList(tickerSymbol, fromDate, toDate);
    }

    public List<StockPriceHistoryVo> getStocksPriceHistory(List<String> tickerSymbolList, LocalDate fromDate, LocalDate toDate)
    {
        return priceHistoryDAO.getStockPriceHistory(tickerSymbolList, fromDate, toDate);
    }

}

