package io.endeavour.stocks.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "StocksHistoryRequest", description = "Contains a list of stocks and their tickerSymbols using fromDate " +
        "and to Date")
public class StocksHistoryRequest {

    @Schema(name = "tickerSymbolList", description = "List of ticker symbols for which the Stock Price History is needed.",
    example = "[\"AAPL\", \"TSLA\", \"V\", \"SBUX\"]")
    List<String> tickerSymbolList;

    @Schema(name = "fromDate", description = "Lower end of the timeframe for which Stock Price History is needed.", example = "2023-03-04")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate fromDate;
    @Schema(name = "toDate", description = "Upper end of the timeframe for which Stock Price History is needed."
            + "toDate must be greater than fromDate" , example = "2023-05-04")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate toDate;

    public List<String> getTickerSymbolList() {
        return tickerSymbolList;
    }

    public void setTickerSymbolList(List<String> tickerSymbolList) {
        this.tickerSymbolList = tickerSymbolList;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }


    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }



}
