package org.softwire.training.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "location_reports")
public class LocationStatusReport extends ReportBase {

    private int locationId;
    //private String callSign;

    @Column(name = "location_id", nullable = false)
    public int getLocationId() {
        return locationId;
    }
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString(){
        return "locationId: "+ locationId + ", body: " + super.getReportBody();
    }

    //@Column(name = "call_sign", nullable = false)
    //public String getCallSign() {
    //    return callSign;
    //}
    //public void setCallSign(String callSign) {
    //    this.callSign = callSign;
    //}
}
