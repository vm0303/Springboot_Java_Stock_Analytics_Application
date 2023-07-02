package io.endeavour.stocks.remote.vo;

import java.util.List;

public class CumulativeReturnWebServiceInputVO
{
    List<String> tickers;

    public List<String> getTickers() {
        return tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }
}
