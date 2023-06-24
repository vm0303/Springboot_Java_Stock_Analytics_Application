package io.endeavour.stocks.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class StocksHistoryRequest {

    List<String> tickerSymbolList;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate fromDate;

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
