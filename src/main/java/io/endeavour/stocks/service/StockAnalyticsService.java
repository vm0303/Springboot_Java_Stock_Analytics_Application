package io.endeavour.stocks.service;

import io.endeavour.stocks.StocksException;
import io.endeavour.stocks.dao.PriceHistoryDAO;
import io.endeavour.stocks.dao.SingleStocksPriceHistoryDAO;
import io.endeavour.stocks.dao.StockFundamentalsDAO;
import io.endeavour.stocks.entity.stocks.Sector;
import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.entity.stocks.StocksPriceHistory;
import io.endeavour.stocks.entity.stocks.StocksPriceHistoryPk;
import io.endeavour.stocks.remote.StocksCalculationClient;
import io.endeavour.stocks.remote.vo.CumulativeReturnWebServiceInputVO;
import io.endeavour.stocks.remote.vo.CumulativeReturnWebServiceOutputVO;
import io.endeavour.stocks.repository.stocks.SectorRepository;
import io.endeavour.stocks.repository.stocks.StockFundamentalsRepository;
import io.endeavour.stocks.repository.stocks.StocksPriceHistoryRepository;
import io.endeavour.stocks.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockAnalyticsService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StockAnalyticsService.class);

    private SingleStocksPriceHistoryDAO singleStocksPriceHistoryDAO;
    private PriceHistoryDAO priceHistoryDAO;

    private StockFundamentalsDAO stockFundamentalsDAO;

    private StockFundamentalsRepository stockFundamentalsRepository;

    private SectorRepository sectorRepository;

    private StocksPriceHistoryRepository stocksPriceHistoryRepository;

    private StocksCalculationClient stocksCalculationClient;



    @Autowired
    public StockAnalyticsService(SingleStocksPriceHistoryDAO singleStocksPriceHistoryDAO, PriceHistoryDAO priceHistoryDAO,
                                StockFundamentalsDAO stockFundamentalsDAO, StockFundamentalsRepository stockFundamentalsRepository,
                                SectorRepository sectorRepository, StocksPriceHistoryRepository stocksPriceHistoryRepository, StocksCalculationClient stocksCalculationClient) {
        this.singleStocksPriceHistoryDAO = singleStocksPriceHistoryDAO;
        this.priceHistoryDAO = priceHistoryDAO;
        this.stockFundamentalsDAO =stockFundamentalsDAO;
        this.stockFundamentalsRepository = stockFundamentalsRepository;
        this.sectorRepository = sectorRepository;
        this.stocksPriceHistoryRepository = stocksPriceHistoryRepository;
        this.stocksCalculationClient = stocksCalculationClient;
    }


    public List<StockFundamentals> getAllStockFundamentalsJPA(Optional<String> sortFieldOptional, Optional<String> sortDirectionOptional)
    {
        String sortField = sortFieldOptional.orElse("tickerSymbol");
        String sortDirection = sortDirectionOptional.orElse("asc");

        List<StockFundamentals> stockFundamentalsList = stockFundamentalsRepository.findAll();


        Comparator sortComparatorSF = switch (sortField)
        {
            case("tickerSymbol") -> Comparator.comparing(StockFundamentals::getTickerSymbols);
            case("marketCap") -> Comparator.comparing(StockFundamentals::getMarketCap, Comparator.nullsFirst(BigDecimal::compareTo));
            case("currentRatio") -> Comparator.comparing(StockFundamentals::getCurrentRatio);
            case("subsectorID") -> Comparator.comparing(StockFundamentals::getSubSectorID);
            case("sectorID") -> Comparator.comparing(StockFundamentals::getSectorID);
            default -> throw new IllegalArgumentException("Unexpected value entered: " + sortField);
        };


        if(sortDirection.equalsIgnoreCase("dsc"))
            sortComparatorSF = sortComparatorSF.reversed();

        stockFundamentalsList.sort(sortComparatorSF);

        return stockFundamentalsList;
    }


    public List<StockPriceHistoryVo> singleStocksPriceHistoryDAOList(String tickerSymbol, LocalDate fromDate, LocalDate toDate,
                                                                     Optional<String> sortFieldOptional, Optional<String> sortDirectionOptional)
    {

        String sortField = sortFieldOptional.orElse("tradingDate");
        String sortDirection = sortDirectionOptional.orElse("asc");


        List<StockPriceHistoryVo> stockPriceHistoryVoList = singleStocksPriceHistoryDAO.getStockPriceHistory(tickerSymbol, fromDate, toDate);

        Comparator sortComparator = switch (sortField)
        {
            case("openPrice") -> Comparator.comparing(StockPriceHistoryVo::getOpenPrice);
            case ("closePrice") -> Comparator.comparing(StockPriceHistoryVo::getClosePrice);
            case ("Volume") -> Comparator.comparing(StockPriceHistoryVo::getVolume);
            case ("tradingDate") -> Comparator.comparing(StockPriceHistoryVo::getTradingDate);
            default -> throw new IllegalArgumentException("Unexpected value entered: " + sortField);
        };

        if(sortDirection.equalsIgnoreCase("dsc"))
          sortComparator =  sortComparator.reversed();

        stockPriceHistoryVoList.sort(sortComparator);

        return stockPriceHistoryVoList;


    }

    public List<StockPriceHistoryVo> getStocksPriceHistory(List<String> tickerSymbolList, LocalDate fromDate, LocalDate toDate)
    {
        return priceHistoryDAO.getStockPriceHistory(tickerSymbolList, fromDate, toDate);
    }

    public List<StockFundamentalsVO> getStockFundamentals(List<String> tickerSymbolList)
    {
        return stockFundamentalsDAO.getStockFundamentals(tickerSymbolList);
    }

    public List<Sector> getAllSectors()
    {
        return sectorRepository.findAll();
    }

    public Optional<Sector> getSector(Integer sectorID)
    {
        Optional<Sector> optionalSectorByID = sectorRepository.findById(sectorID);

        return optionalSectorByID;
    }

    public Optional<StocksPriceHistory> getStocksPriceHistoryJPA(String tickerSymbol, LocalDate tradingDate)
    {
        StocksPriceHistoryPk stocksPriceHistoryPk = new StocksPriceHistoryPk();
        stocksPriceHistoryPk.setTickerSymbol(tickerSymbol);
        stocksPriceHistoryPk.setTradingDate(tradingDate);
        return stocksPriceHistoryRepository.findById(stocksPriceHistoryPk);
    }

    public List<TopStocksBySector> getTopStocksBySectorList()
    {
        return stockFundamentalsRepository.getTopStocksBySector();
    }


    public List<FormattedTopStocksBySubSectorVO> getTop5StocksBySubSectorList()
    {
        List<TopStocksBySubSectorVo> topStocksBySubSectorList = stockFundamentalsRepository.getTopStocksBySubSector();
        List<FormattedTopStocksBySubSectorVO> finalOutputList = new ArrayList<>();

        //Map has SubSectorName as key and list of SubSectorStocks as value
        Map<String, List<TopStocksBySubSectorVo>> subSectorListByNameMap = topStocksBySubSectorList.stream()
                .collect(Collectors.groupingBy(TopStocksBySubSectorVo::getSubSectorName));

        //For each iteration of the earlier map, populate a new FormattedTopStocksBySubSectorVO object
        subSectorListByNameMap.forEach((subSectorName, stocksList) -> {

            //This object represents each SubSector of the economy
            FormattedTopStocksBySubSectorVO formattedTopStocksBySubSectorVO = new FormattedTopStocksBySubSectorVO();
            formattedTopStocksBySubSectorVO.setSubSectorName(subSectorName);

            //The below list represents the list of Top Stocks for each SubsSector
            List<StockVO> stockVOList = new ArrayList<>();

            // For each iteration of the StockList, populate a unique StockVO object and add it to the List of Top Stocks for each subSector
            stocksList.forEach(topStockBySubSectorVO -> {
                formattedTopStocksBySubSectorVO.setSubSectorID(topStockBySubSectorVO.getSubSectorID());
                formattedTopStocksBySubSectorVO.setSectorName(topStockBySubSectorVO.getSectorName());

                //The below object represents each Stock in the Top Stocks list
                StockVO stockVO = new StockVO(topStockBySubSectorVO.getTickerSymbol(), topStockBySubSectorVO.getTickerName(),
                        topStockBySubSectorVO.getMarketCap());
                stockVOList.add(stockVO); //Adding each stock to the Top Stock list for each subSector
            });
            formattedTopStocksBySubSectorVO.setTopStockList(stockVOList); // Set the topStockList into the SubSector Formatted Object
            finalOutputList.add(formattedTopStocksBySubSectorVO); // Add the SubSector Formatted object to the final Output List
        });
        return finalOutputList;

    }


    //Using Native SQL Query in StockFundamentals Repository class
    public List<StockFundamentals> getTopNStocksByNativeSQL(Integer num)
    {
        return stockFundamentalsRepository.getTopNStocksUsingNativeSQL(num);
    }

    public List<StockFundamentals> allStocksFromStockFundamentals()
    {
        return stockFundamentalsRepository.getAllStocksFromStockFundamentals();
    }

    public List<StockFundamentals> getTopNStockFundamentalsUsingJPQL(Integer num)
    {
        return stockFundamentalsDAO.getTopNStocksUsingJPQL(num);
    }

    public List<StockFundamentals> getTopNStockFundamentalsUsingCriteria(Integer num)
    {
        return stockFundamentalsDAO.getTopNStocksUsingJPQL(num);
    }



    public Optional<StockFundamentalsHistoryVO> getStockFundamentalHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate){
        List<StockPriceHistoryVo> stockPriceHistoryList = singleStocksPriceHistoryDAO.getStockPriceHistory(tickerSymbol, fromDate, toDate);

        Optional<StockFundamentals> stockFundamentalsOptional = stockFundamentalsRepository.findById(tickerSymbol);
        StockFundamentalsHistoryVO  stockFundamentalHistoryVO = null;

        if(stockFundamentalsOptional.isPresent()){
            stockFundamentalHistoryVO = new StockFundamentalsHistoryVO();
            stockFundamentalHistoryVO.setTickerSymbol(tickerSymbol);
            stockFundamentalHistoryVO.setCurrentRatio(stockFundamentalsOptional.get().getCurrentRatio());
            stockFundamentalHistoryVO.setMarketCap(stockFundamentalsOptional.get().getMarketCap());
            stockFundamentalHistoryVO.setTradingHistory(stockPriceHistoryList);
        }
        Optional<StockFundamentalsHistoryVO> stockFundamentalHistoryOptional = Optional.ofNullable(stockFundamentalHistoryVO);
        return stockFundamentalHistoryOptional;
    }


    //Will use this method for Unit Testing
    public List<StockFundamentals> getTopNPerformingStocks(Integer num, LocalDate fromDate, LocalDate toDate, Long greaterThanMKCp)
    {
        List<StockFundamentals> allStockList = stockFundamentalsRepository.findAll();
        LOGGER.info("Size of all stock list is {}", allStockList.size());
        List<String> allTickerSymbolsList = allStockList.stream()
                .map(stockFundamentals -> stockFundamentals.getTickerSymbols())
                .collect(Collectors.toList());

        CumulativeReturnWebServiceInputVO cumulativeReturnWebServiceInputVO = new CumulativeReturnWebServiceInputVO();
        cumulativeReturnWebServiceInputVO.setTickers(allTickerSymbolsList);


        List<CumulativeReturnWebServiceOutputVO> cumulativeReturnOutputList =
                stocksCalculationClient.getCumulativeReturn(fromDate, toDate, cumulativeReturnWebServiceInputVO);

        LOGGER.info("Size of cumulative return list is {}", cumulativeReturnOutputList.size());


        if(cumulativeReturnOutputList == null || cumulativeReturnOutputList.isEmpty())
        {
            throw new StocksException("The Stock calculation Web Service is down.");
        }
        if(allStockList == null || allStockList.isEmpty())
        {
            throw new StocksException("The Stock database is down.");
        }
        //Generating a map with tickerSymbol as key and Cumulative Return as value
        Map<String, BigDecimal> cumulativeReturnByTickerMap = cumulativeReturnOutputList.stream().collect(Collectors.toMap(
                CumulativeReturnWebServiceOutputVO::getTickerSymbol,
                CumulativeReturnWebServiceOutputVO::getCumulativeReturn
        ));

        //Iterate the AllStocks list and put the cumulative return values from the previous map into the list.
        allStockList.forEach(stockFundamentals -> {
            stockFundamentals.setCumulativeReturn(cumulativeReturnByTickerMap.get(stockFundamentals.getTickerSymbols()));
        });

        //This will filter the cumulative return to make sure it's not null, and sort it in descending order.
        List<StockFundamentals> finalOutputList = allStockList.stream()
                .filter(stockFundamentals -> stockFundamentals.getMarketCap() != null)
                .filter(stockFundamentals-> stockFundamentals.getMarketCap().compareTo(new BigDecimal(String.valueOf(greaterThanMKCp)))>0)
                .filter(stockFundamentals -> stockFundamentals.getCumulativeReturn() != null)
                .sorted(Comparator.comparing(StockFundamentals::getCumulativeReturn).reversed())
                .limit(num)
                .collect(Collectors.toList());

        return finalOutputList;

    }

    public List<FormattedTopStocksBySubSectorVO> getTop5StocksWithCumulativeReturn(LocalDate fromDate, LocalDate toDate)
    {
        List<TopStocksBySubSectorVo> topStocksBySubSectorVoList = stockFundamentalsRepository.getTopStocksBySubSector();

        List<FormattedTopStocksBySubSectorVO> formattedTopStocksBySubSectorVOList
                = new ArrayList<>();


        List<String> allTickerSymbolsList = topStocksBySubSectorVoList.stream()
                .map(TopStocksBySubSectorVo::getTickerSymbol)
                .collect(Collectors.toList());

        CumulativeReturnWebServiceInputVO cumulativeReturnWebServiceInputVO = new CumulativeReturnWebServiceInputVO();
        cumulativeReturnWebServiceInputVO.setTickers(allTickerSymbolsList);


        Map<String, List<TopStocksBySubSectorVo>> subSectorByListMap = topStocksBySubSectorVoList.stream()
                .collect(Collectors.groupingBy(TopStocksBySubSectorVo::getSubSectorName));

        subSectorByListMap.forEach((subSector, stocksList) -> {
            FormattedTopStocksBySubSectorVO formattedTopStocksBySubSectorVO = new FormattedTopStocksBySubSectorVO();
            formattedTopStocksBySubSectorVO.setSubSectorName(subSector);

            List<CumulativeReturnWebServiceOutputVO> cumulativeReturnOutputList =
                    stocksCalculationClient.getCumulativeReturn(fromDate, toDate,cumulativeReturnWebServiceInputVO);

            Map<String, BigDecimal> cumulativeReturnByTickerSymbolsMap = cumulativeReturnOutputList.stream().collect(Collectors.toMap(
                    CumulativeReturnWebServiceOutputVO::getTickerSymbol,
                    CumulativeReturnWebServiceOutputVO::getCumulativeReturn
            ));

            topStocksBySubSectorVoList.forEach(topStocksBySubSectorVo -> topStocksBySubSectorVo.setCumulativeReturn
                    (cumulativeReturnByTickerSymbolsMap.get(topStocksBySubSectorVo.getTickerSymbol())));

            List<StockVO> stockVOList = new ArrayList<>();

            stocksList.forEach(topStocksBySubSectorVo ->
            {
                formattedTopStocksBySubSectorVO.setSubSectorID(topStocksBySubSectorVo.getSubSectorID());
                formattedTopStocksBySubSectorVO.setSectorName(topStocksBySubSectorVo.getSectorName());

                StockVO stockVO = new StockVO(topStocksBySubSectorVo.getTickerSymbol(), topStocksBySubSectorVo.getTickerName(),
                        topStocksBySubSectorVo.getMarketCap());

                stockVO.setCumulativeReturn(cumulativeReturnByTickerSymbolsMap.get(topStocksBySubSectorVo.getTickerSymbol()));

                stockVOList.add(stockVO);
            });
            formattedTopStocksBySubSectorVO.setTopStockList(stockVOList);
            formattedTopStocksBySubSectorVOList.add(formattedTopStocksBySubSectorVO);
        });


        List<FormattedTopStocksBySubSectorVO> finalOutputList = formattedTopStocksBySubSectorVOList.stream()
                .map(formattedTopStocksBySubSectorVO ->
                {
                    List<StockVO> topStockList = formattedTopStocksBySubSectorVO.getTopStockList().stream()
                            .filter(stockVO -> stockVO.getMarketCap() !=null)
                            .filter(stockVO -> stockVO.getCumulativeReturn() !=null)
                            .sorted(Comparator.comparing(StockVO::getCumulativeReturn).reversed())
                            .collect(Collectors.toList());
                    formattedTopStocksBySubSectorVO.setTopStockList(topStockList);

                    return formattedTopStocksBySubSectorVO;
                }).collect(Collectors.toList());

        List<FormattedTopStocksBySubSectorVO> sortedFinalOutputList = finalOutputList.stream()
                .sorted(Comparator.comparing(FormattedTopStocksBySubSectorVO::getSubSectorName))
                .collect(Collectors.toList());

        return sortedFinalOutputList;
    }






}


