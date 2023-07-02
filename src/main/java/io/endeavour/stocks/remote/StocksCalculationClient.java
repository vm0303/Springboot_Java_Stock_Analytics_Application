package io.endeavour.stocks.remote;


import io.endeavour.stocks.config.FeignConfig;
import io.endeavour.stocks.remote.vo.CumulativeReturnWebServiceInputVO;
import io.endeavour.stocks.remote.vo.CumulativeReturnWebServiceOutputVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "stocksCalculationClient", configuration = {FeignConfig.class}, url = "${client.stock-calculations.url}")
public interface StocksCalculationClient
{
    @PostMapping(value = "/calculate/cumulative-return/{fromDate}/{toDate}")
    List<CumulativeReturnWebServiceOutputVO> getCumulativeReturn(@PathVariable(value = "fromDate")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                 @PathVariable(value = "toDate")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
                                                                 CumulativeReturnWebServiceInputVO cumulativeReturnWebServiceInputVO);

}
