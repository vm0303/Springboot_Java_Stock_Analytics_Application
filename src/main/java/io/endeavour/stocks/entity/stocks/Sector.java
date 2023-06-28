package io.endeavour.stocks.entity.stocks;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SECTOR_LOOKUP", schema = "ENDEAVOUR")
public class Sector
{
    @Column (name = "SECTOR_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sectorID;

    @Column (name = "SECTOR_NAME")
    private String sectorName;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<SubSector> subsectorList;

    public Integer getSectorID() {
        return sectorID;
    }

    public void setSectorID(Integer sectorID) {
        this.sectorID = sectorID;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public List<SubSector> getSubsectorList() {
        return subsectorList;
    }

    public void setSubsectorList(List<SubSector> subsectorList) {
        this.subsectorList = subsectorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return Objects.equals(sectorID, sector.sectorID);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Sector{");
        sb.append("sectorID=").append(sectorID);
        sb.append(", sectorName='").append(sectorName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorID);
    }
}
