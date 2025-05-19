package org.example.bws.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignalVO {
    //最大电压
    @JsonProperty("Mx")
    private Double Mx;
    //最低电压
    @JsonProperty("Mi")
    private Double Mi;
    //最大电流
    @JsonProperty("Ix")
    private Double Ix;
    //最低电流
    @JsonProperty("Ii")
    private Double Ii;

    @Override
    public String toString() {
        return "SignalVO{" +
                "Mx=" + Mx +
                ", Mi=" + Mi +
                ", Ix=" + Ix +
                ", Ii=" + Ii +
                '}';
    }

    public Double getMi() {
        return Mi;
    }

    public void setMi(Double Mi) {
        this.Mi = Mi;
    }

    public Double getMx() {
        return Mx;
    }

    public void setMx(Double Mx) {
        this.Mx = Mx;
    }

    public Double getIx() {
        return Ix;
    }

    public void setIx(Double Ix) {
        this.Ix = Ix;
    }

    public Double getIi() {
        return Ii;
    }

    public void setIi(Double Ii) {
        this.Ii = Ii;
    }
}
