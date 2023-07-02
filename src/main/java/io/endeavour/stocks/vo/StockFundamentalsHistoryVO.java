package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.util.List;

//The first class created for Assignment 6/29
public class StockFundamentalsHistoryVO
{
    private String tickerSymbol;
    private BigDecimal currentRatio;
    private BigDecimal marketCap;
    private List<StockPriceHistoryVo> tradingHistory;

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public List<StockPriceHistoryVo> getTradingHistory() {
        return tradingHistory;
    }

    public void setTradingHistory(List<StockPriceHistoryVo> tradingHistory) {
        this.tradingHistory = tradingHistory;
    }
}
