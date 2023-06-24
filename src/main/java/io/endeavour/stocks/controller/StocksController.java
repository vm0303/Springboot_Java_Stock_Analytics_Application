package io.endeavour.stocks.controller;


import io.endeavour.stocks.service.StockAnalyticsService;
import io.endeavour.stocks.vo.StockPriceHistoryVo;
import io.endeavour.stocks.vo.StocksHistoryRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StocksController {

    private StockAnalyticsService stockAnalyticsService;

    public StocksController(StockAnalyticsService stockAnalyticsService) {
        this.stockAnalyticsService = stockAnalyticsService;
    }

    @GetMapping(value = "/samplePriceHistory/{tickerSymbol}")
    public StockPriceHistoryVo getSamplePriceHistory(
            @PathVariable(name = "tickerSymbol") String tickerSymbol,
            @RequestParam(name = "tradingDate", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate tradingDate
            )
    {
       StockPriceHistoryVo stockPriceHistoryVo = new StockPriceHistoryVo();
       stockPriceHistoryVo.setTickerSymbol(tickerSymbol);
       stockPriceHistoryVo.setTradingDate(tradingDate);
       stockPriceHistoryVo.setOpenPrice(new BigDecimal("100"));
       stockPriceHistoryVo.setClosePrice(new BigDecimal("100.5"));
       stockPriceHistoryVo.setVolume(89765L);
       return stockPriceHistoryVo;

    }

    @GetMapping(value = "/singleStockPriceHistory/{tickerSymbol}")
    public List<StockPriceHistoryVo> getSingleStockPriceHistoryList(
            @PathVariable(name = "tickerSymbol") String tickerSymbol,
            @RequestParam(name = "fromDate", required = true)
            @DateTimeFormat (pattern = "MM-dd-yyyy") LocalDate fromDate,
            @RequestParam(name = "toDate", required = true)
            @DateTimeFormat (pattern = "MM-dd-yyyy") LocalDate toDate
    )
    {
        return stockAnalyticsService.singleStocksPriceHistoryDAOList(tickerSymbol, fromDate, toDate);
    }

    @PostMapping(value = "/getStocksPriceHistory")
    public List<StockPriceHistoryVo> getStockPriceHistory(@RequestBody StocksHistoryRequest stocksHistoryRequest)
    {
        //This will give a bad request, if the from date is greater than to date.
        if(stocksHistoryRequest.getFromDate().compareTo(stocksHistoryRequest.getToDate())>0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate cannot be greater than toDate");
        }
        return stockAnalyticsService.getStocksPriceHistory(stocksHistoryRequest.getTickerSymbolList(),
                stocksHistoryRequest.getFromDate(), stocksHistoryRequest.getToDate());
    }
}
