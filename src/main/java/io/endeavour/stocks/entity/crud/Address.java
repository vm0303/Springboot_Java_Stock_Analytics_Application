package io.endeavour.stocks.entity.crud;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "ADDRESS", schema = "ENDEAVOUR_TEST_AREA")
public class Address {

    @Column(name = "ADDRESS_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressID;

    @Column(name = "ADDRESS_TYPE")
    private String addressType;

    @Column(name = "LINE_1")
    private String line1;

    @Column(name = "LINE_2")
    private String line2;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "PERSON_ID")
    Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getAddressID() {
        return addressID;
    }

    public void setAddressID(Integer addressID) {
        this.addressID = addressID;
    }


    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(addressID, address.addressID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressID);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Address{");
        sb.append("addressID=").append(addressID);
        sb.append(", addressType='").append(addressType).append('\'');
        sb.append(", line1='").append(line1).append('\'');
        sb.append(", line2='").append(line2).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
