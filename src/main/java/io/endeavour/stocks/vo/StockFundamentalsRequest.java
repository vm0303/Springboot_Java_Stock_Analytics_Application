package io.endeavour.stocks.vo;

import java.util.List;

public class StockFundamentalsRequest
{
    List<String> tickerSymbolList;

    public List<String> getTickerSymbolList() {
        return tickerSymbolList;
    }

    public void setTickerSymbolList(List<String> tickerSymbolList) {
        this.tickerSymbolList = tickerSymbolList;
    }
}
