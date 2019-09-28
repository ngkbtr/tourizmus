package ngkbtr.controller.request;

import java.math.BigDecimal;

public class GetHotelsRequest {
    private String location;
    private String startDate;
    private String endDate;
    private BigDecimal minPricePerNight;
    private BigDecimal maxPricePerNight;
    private Long stars;
    private Long radius;

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public BigDecimal getMinPricePerNight() {
        return minPricePerNight;
    }

    public BigDecimal getMaxPricePerNight() {
        return maxPricePerNight;
    }

    public Long getStars() {
        return stars;
    }

    public Long getRadius() {
        return radius;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setMinPricePerNight(BigDecimal minPricePerNight) {
        this.minPricePerNight = minPricePerNight;
    }

    public void setMaxPricePerNight(BigDecimal maxPricePerNight) {
        this.maxPricePerNight = maxPricePerNight;
    }

    public void setStars(Long stars) {
        this.stars = stars;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }
}
