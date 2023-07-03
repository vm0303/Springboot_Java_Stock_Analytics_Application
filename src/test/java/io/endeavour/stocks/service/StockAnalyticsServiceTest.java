package io.endeavour.stocks.service;

import io.endeavour.stocks.StocksException;
import io.endeavour.stocks.entity.stocks.StockFundamentals;
import io.endeavour.stocks.remote.StocksCalculationClient;
import io.endeavour.stocks.remote.vo.CumulativeReturnWebServiceOutputVO;
import io.endeavour.stocks.repository.stocks.StockFundamentalsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles({"test"})
class StockAnalyticsServiceTest
{
    @Autowired
    StockAnalyticsService stockAnalyticsService;

    @MockBean
    StockFundamentalsRepository stockFundamentalsRepository;

    @MockBean
    StocksCalculationClient stocksCalculationClient;


    @Test
    public void testTopPerformingStocks_HappyPath()
    {
        List<StockFundamentals> stockFundamentalsDummyList=
                List.of(
                  createStockFundamentals("AAPL", new BigDecimal(150), new BigDecimal(2.54)) ,
                        createStockFundamentals("TSLA", new BigDecimal(150), new BigDecimal(2.24)),
                        createStockFundamentals("V", new BigDecimal(150), new BigDecimal(5.21))
                );


        Mockito.when(stockFundamentalsRepository.findAll()).thenReturn(stockFundamentalsDummyList);

        List<CumulativeReturnWebServiceOutputVO>cumulativeReturnWebServiceOutputVODummyList =

                List.of(
                        createCumulativeReturnWebServiceOutputVO("AAPL", new BigDecimal(2.43)),
                        createCumulativeReturnWebServiceOutputVO("TSLA", new BigDecimal(1.54)),
                        createCumulativeReturnWebServiceOutputVO("V", new BigDecimal(0.98))
                );

        Mockito.when(stocksCalculationClient.getCumulativeReturn(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(cumulativeReturnWebServiceOutputVODummyList);

         List<StockFundamentals> topNPerformingStocksList = stockAnalyticsService.getTopNPerformingStocks(2, LocalDate.now().minusMonths(3),
                LocalDate.now(), 0L);

         assertEquals(2, topNPerformingStocksList.size());
         assertNotEquals(100, topNPerformingStocksList.size());
    }

    @Test
    public void testTopPerformingStocks_CumulativeReturnsWSDown()
    {
        List<StockFundamentals> stockFundamentalsDummyList=
                List.of(
                        createStockFundamentals("AAPL", new BigDecimal(150), new BigDecimal(2.54)) ,
                        createStockFundamentals("TSLA", new BigDecimal(150), new BigDecimal(2.24)),
                        createStockFundamentals("V", new BigDecimal(150), new BigDecimal(5.21))
                );

        Mockito.when(stockFundamentalsRepository.findAll()).thenReturn(stockFundamentalsDummyList);

        //Simulating an empty list of Cumulative Return in case the Web Service goes down.
        Mockito.when(stocksCalculationClient.getCumulativeReturn(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());



       StocksException stocksExceptionError = Assertions.assertThrows(StocksException.class, () -> stockAnalyticsService.getTopNPerformingStocks
                (5, LocalDate.now().minusMonths(1), LocalDate.now(), 0L));

        Assertions.assertEquals("Error: ", stocksExceptionError.getMessage());

    }

    StockFundamentals createStockFundamentals(String tickerSymbol, BigDecimal marketCap, BigDecimal currentRatio)
    {
       StockFundamentals stockFundamentals = new StockFundamentals();
       stockFundamentals.setCurrentRatio(currentRatio);
       stockFundamentals.setMarketCap(marketCap);
       stockFundamentals.setTickerSymbols(tickerSymbol);
       return stockFundamentals;
    }
    CumulativeReturnWebServiceOutputVO createCumulativeReturnWebServiceOutputVO
            (String tickerSymbol, BigDecimal cumulativeReturn)
    {
        CumulativeReturnWebServiceOutputVO cumulativeReturnWebServiceOutputVO
                = new CumulativeReturnWebServiceOutputVO();
        cumulativeReturnWebServiceOutputVO.setTickerSymbol(tickerSymbol);
        cumulativeReturnWebServiceOutputVO.setCumulativeReturn(cumulativeReturn);
        return  cumulativeReturnWebServiceOutputVO;
    }

    @Test
    public void testTopPerformingStocks_MisMatchDBCumulativeWS()
    {
        List<StockFundamentals> stockFundamentalsDummyList=
                List.of(
                        createStockFundamentals("AAPL", new BigDecimal(150), new BigDecimal(2.54)) ,
                        createStockFundamentals("AMD", new BigDecimal(150), new BigDecimal(2.24)),
                        createStockFundamentals("HD", new BigDecimal(150), new BigDecimal(5.21))
                );


        Mockito.when(stockFundamentalsRepository.findAll()).thenReturn(stockFundamentalsDummyList);

        List<CumulativeReturnWebServiceOutputVO>cumulativeReturnWebServiceOutputVODummyList =

                List.of(
                        createCumulativeReturnWebServiceOutputVO("AAPL", new BigDecimal(2.43)),
                        createCumulativeReturnWebServiceOutputVO("TSLA", new BigDecimal(1.54)),
                        createCumulativeReturnWebServiceOutputVO("V", new BigDecimal(0.98))
                );

        Mockito.when(stocksCalculationClient.getCumulativeReturn(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(cumulativeReturnWebServiceOutputVODummyList);

        List<StockFundamentals> topNPerformingStocksList = stockAnalyticsService.getTopNPerformingStocks(1, LocalDate.now().minusMonths(3),
                LocalDate.now(), 0L);

        assertEquals(1, topNPerformingStocksList.size());
    }
}