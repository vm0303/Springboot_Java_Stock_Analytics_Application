package io.endeavour.stocks.vo;

import java.util.List;

public class FormattedTopStocksBySubSectorVO
{
    private String subSectorName;

    private Integer subSectorID;

    private String sectorName;

    List<StockVO> topStockList;

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public Integer getSubSectorID() {
        return subSectorID;
    }

    public void setSubSectorID(Integer subSectorID) {
        this.subSectorID = subSectorID;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public List<StockVO> getTopStockList() {
        return topStockList;
    }

    public void setTopStockList(List<StockVO> topStockList) {
        this.topStockList = topStockList;
    }
}
