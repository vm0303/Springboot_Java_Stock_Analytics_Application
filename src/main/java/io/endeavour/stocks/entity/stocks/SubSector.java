package io.endeavour.stocks.entity.stocks;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "SUBSECTOR_LOOKUP", schema = "ENDEAVOUR")
public class SubSector
{

    @Column(name = "SUBSECTOR_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subSectorID;

    @Column(name = "SUBSECTOR_NAME")
    private String subSectorName;


    public Integer getSubSectorID() {
        return subSectorID;
    }

    public void setSubSectorID(Integer subSectorID) {
        this.subSectorID = subSectorID;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSector subSector = (SubSector) o;
        return Objects.equals(subSectorID, subSector.subSectorID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subSectorID);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SubSector{");
        sb.append("subSectorID=").append(subSectorID);
        sb.append(", subSectorName=").append(subSectorName);
        sb.append('}');
        return sb.toString();
    }
}
