package io.endeavour.stocks.service;

import io.endeavour.stocks.dao.PriceHistoryDAO;
import io.endeavour.stocks.dao.SingleStocksPriceHistoryDAO;
import io.endeavour.stocks.dao.StockFundamentalsDAO;
import io.endeavour.stocks.entity.stocks.Sector;
import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.entity.stocks.StocksPriceHistory;
import io.endeavour.stocks.entity.stocks.StocksPriceHistoryPk;
import io.endeavour.stocks.repository.stocks.SectorRepository;
import io.endeavour.stocks.repository.stocks.StockFundamentalsRepository;
import io.endeavour.stocks.repository.stocks.StocksPriceHistoryRepository;
import io.endeavour.stocks.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockAnalyticsService
{

    private SingleStocksPriceHistoryDAO singleStocksPriceHistoryDAO;
    private PriceHistoryDAO priceHistoryDAO;

    private StockFundamentalsDAO stockFundamentalsDAO;

    private StockFundamentalsRepository stockFundamentalsRepository;

    private SectorRepository sectorRepository;

    private StocksPriceHistoryRepository stocksPriceHistoryRepository;



    @Autowired
    public StockAnalyticsService(SingleStocksPriceHistoryDAO singleStocksPriceHistoryDAO, PriceHistoryDAO priceHistoryDAO,
                                StockFundamentalsDAO stockFundamentalsDAO, StockFundamentalsRepository stockFundamentalsRepository,
                                SectorRepository sectorRepository, StocksPriceHistoryRepository stocksPriceHistoryRepository) {
        this.singleStocksPriceHistoryDAO = singleStocksPriceHistoryDAO;
        this.priceHistoryDAO = priceHistoryDAO;
        this.stockFundamentalsDAO =stockFundamentalsDAO;
        this.stockFundamentalsRepository = stockFundamentalsRepository;
        this.sectorRepository = sectorRepository;
        this.stocksPriceHistoryRepository = stocksPriceHistoryRepository;
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


    public List<FormattedTopStocksBySubSectorVO> getTopStocksBySubSectorList()
    {
        List<TopStocksBySubSectorVo> topStocksBySubSectorVoList = stockFundamentalsRepository.getTopStocksBySubSector();

        List<FormattedTopStocksBySubSectorVO> finalOutputList = new ArrayList<>();

        //Map has SubsectorName as key and list of subSectors as values
        Map<String, List<TopStocksBySubSectorVo>> subSectorByListMap = topStocksBySubSectorVoList.stream()
                .collect(Collectors.groupingBy(TopStocksBySubSectorVo::getSubSectorName));

        //For each  iterating of the map,
        subSectorByListMap.forEach((subSector, stocksList) -> {
            FormattedTopStocksBySubSectorVO formattedTopStocksBySubSectorVO = new FormattedTopStocksBySubSectorVO();
            formattedTopStocksBySubSectorVO.setSubSectorName(subSector);

            List<StockVO> stockVOList = new ArrayList<>();

            stocksList.forEach(topStocksBySubSectorVo ->
            {
                formattedTopStocksBySubSectorVO.setSubSectorID(topStocksBySubSectorVo.getSubSectorID());
                formattedTopStocksBySubSectorVO.setSectorName(topStocksBySubSectorVo.getSectorName());

                StockVO stockVO = new StockVO(topStocksBySubSectorVo.getTickerSymbol(), topStocksBySubSectorVo.getTickerName(),
                        topStocksBySubSectorVo.getMarketCap());

                stockVOList.add(stockVO);
            });
            formattedTopStocksBySubSectorVO.setTopStockList(stockVOList);
            finalOutputList.add(formattedTopStocksBySubSectorVO);




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

}


