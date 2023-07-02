package io.endeavour.stocks.controller;


import io.endeavour.stocks.entity.stocks.Sector;
import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.entity.stocks.StocksPriceHistory;
import io.endeavour.stocks.service.StockAnalyticsService;
import io.endeavour.stocks.vo.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    //http://localhost:8095/stockanalytics/stocks/singleStockPriceHistory/TSLA?fromDate=03-01-2023&toDate=03-31-2023&sortField=openPrice&sortDirection=dsc
    @GetMapping(value = "/singleStockPriceHistory/{tickerSymbol}")
    public List<StockPriceHistoryVo> getSingleStockPriceHistoryList(
            @PathVariable(name = "tickerSymbol") String tickerSymbol,
            @RequestParam(name = "fromDate", required = true)
            @DateTimeFormat (pattern = "MM-dd-yyyy") LocalDate fromDate,
            @RequestParam(name = "toDate", required = true)
            @DateTimeFormat (pattern = "MM-dd-yyyy") LocalDate toDate,
            @RequestParam( name = "sortField" , required = false)Optional<String> sortFieldOptional,
            @RequestParam( name = "sortDirection" , required = false)Optional<String> sortDirectionOptional
            )
    {
        List<StockPriceHistoryVo> stockPriceHistoryVoList;
        try
        {
            stockPriceHistoryVoList =stockAnalyticsService.singleStocksPriceHistoryDAOList(tickerSymbol, fromDate, toDate, sortFieldOptional, sortDirectionOptional);
        }
        catch (IllegalArgumentException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sortfield entered is not valid");
        }
        return stockPriceHistoryVoList;

    }

    @PostMapping(value = "/getStocksPriceHistory")
    public List<StockPriceHistoryVo> getStockPriceHistory(@RequestBody StocksHistoryRequest stocksHistoryRequest)
    {
        //This will give a bad request, if the fromDate is greater than toDate.
        if(stocksHistoryRequest.getFromDate().compareTo(stocksHistoryRequest.getToDate())>0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate cannot be greater than toDate");
        }
        return stockAnalyticsService.getStocksPriceHistory(stocksHistoryRequest.getTickerSymbolList(),
                stocksHistoryRequest.getFromDate(), stocksHistoryRequest.getToDate());
    }


    @PostMapping(value = "/getStockFundamentals")
    public List<StockFundamentalsVO> getStockFundamentals(@RequestBody StockFundamentalsRequest stockFundamentalsRequest)
    {
        if(stockFundamentalsRequest.getTickerSymbolList() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Array list of ticker symbols cannot be null");
        }
      return stockAnalyticsService.getStockFundamentals(stockFundamentalsRequest.getTickerSymbolList());
    }

    @GetMapping(value ="/getAllStockFundamentalsJPA")

    public List<StockFundamentals> getAllStockFundamentalsJPA
            ( @RequestParam( name = "sortField" , required = false)Optional<String> sortFieldOptional,
              @RequestParam( name = "sortDirection" , required = false)Optional<String> sortDirectionOptional
            )
    {
        List<StockFundamentals> stockFundamentalsListByEntity;
        try
        {
            stockFundamentalsListByEntity = stockAnalyticsService.getAllStockFundamentalsJPA(sortFieldOptional,sortDirectionOptional);
        }
        catch (IllegalArgumentException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sortfield entered is not valid");
        }
        return stockFundamentalsListByEntity;
    }

    @GetMapping(value = "/getAllSectors")
    public List<Sector> getAllSectors()
    {
        return stockAnalyticsService.getAllSectors();
    }

    @GetMapping(value = "/sector")
    public ResponseEntity<Sector> getSector(@RequestParam(value = "sectorID", required = false) Integer sectorID)
    {
        if(sectorID==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sector ID sent is blank");
        }
        stockAnalyticsService.getSector(sectorID);
        return ResponseEntity.of(stockAnalyticsService.getSector(sectorID));
    }


    @GetMapping(value = "/getStocksPriceHistoryJPA")
    public ResponseEntity<StocksPriceHistory> getStocksPriceHistoryJPA(@RequestParam(value = "tickerSymbol") String tickerSymbol,
                                                                       @RequestParam(value = "tradingDate") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate tradingDate)
    {
            return ResponseEntity.of(stockAnalyticsService.getStocksPriceHistoryJPA(tickerSymbol,tradingDate));
    }


    @GetMapping(value = "/getTopStocksBySector")
    public List<TopStocksBySector> getTopStocksBySectorList ()
    {
        return stockAnalyticsService.getTopStocksBySectorList();
    }

    @GetMapping(value = "/getTop5StocksBySubSector")
    public List<FormattedTopStocksBySubSectorVO> getTop5StocksBySubSector(){
        return stockAnalyticsService.getTop5StocksBySubSectorList();
    }


    @GetMapping(value = "/getTopNStocksUsingSQL/{num}")
    public List<StockFundamentals> getTopNStocksUsingNativeSQL(@PathVariable(value = "num") Integer num)
    {
        return stockAnalyticsService.getTopNStocksByNativeSQL(num);
    }

    @GetMapping(value = "/getAllStocksFromSFJPQL")
    public List<StockFundamentals> getAllStocksFromStockFundamentals()
    {
        return stockAnalyticsService.allStocksFromStockFundamentals();
    }

    @GetMapping(value = "/getTopNStocksUsingJPQL/{num}")
    public List<StockFundamentals> getTopNStocksUsingJPQL(@PathVariable(value = "num") Integer num)
    {
        return stockAnalyticsService.getTopNStockFundamentalsUsingJPQL(num);
    }

    @GetMapping(value = "/getTopNStockFundamentalsUsingCriteria/{num}")
    public List<StockFundamentals> getTopNStocksUsingCriteria(@PathVariable(value = "num") Integer num)
    {
        return stockAnalyticsService.getTopNStockFundamentalsUsingCriteria(num);
    }

    @GetMapping(value = "/getStockFundamentalHistory")
    public ResponseEntity<StockFundamentalsHistoryVO> getStockFundamentalHistory(@RequestParam(value = "tickerSymbol") String tickerSymbol,
                                                                                @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                                @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate){
        return ResponseEntity.of(stockAnalyticsService.getStockFundamentalHistory(tickerSymbol,fromDate,toDate));

    }

    @GetMapping(value = "getTopNPerformingStocks/{num}")
    public List<StockFundamentals> getTopNPerformingStocks(@PathVariable(value = "num") Integer num,
                                                                                 @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                                 @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
                                                           @RequestParam(value = "greaterThanMKCp") Long greaterThanMKCp){
        return stockAnalyticsService.getTopNPerformingStocks(num,fromDate,toDate,greaterThanMKCp);

    }

    @GetMapping(value = "/getTop5StocksBySubSectorByCumulativeReturn")

    public List<FormattedTopStocksBySubSectorVO> getTopStocksBySubSectorListWithCumulativeReturn
            (@RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
             @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate)
    {
        return stockAnalyticsService.getTop5StocksWithCumulativeReturn(fromDate, toDate);
    }
}


