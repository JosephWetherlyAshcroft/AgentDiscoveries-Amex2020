package org.softwire.training.models;

import javax.persistence.*;

@Entity
@Table(name = "location_reports")
@SecondaryTable(name="agents", pkJoinColumns = @PrimaryKeyJoinColumn(name="agent_id"))
public class LocationStatusReport2 extends ReportBase {
//public class LocationStatusReport2 extends LocationStatusReport {

    private int locationId;


    @Column(name = "location_id", nullable = false)
    public int getLocationId() {
        return locationId;
    }
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private String callSign;
    @Column(name = "call_sign", nullable = false)
    public String getCallSign() {
        return callSign;
    }
    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }



    @Override
    public String toString(){
        return "locationId: "+ locationId + ", body: " + super.getReportBody();
    }
}
