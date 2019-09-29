package ngkbtr.flowmanager;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class Entertainment {
    private Long id;
    private String title;
    private String tagline;
    private String url;
    private String type;
    private Boolean is_new;
    private String group;
    private Boolean instant_booking;
    private Boolean child_friendly;
    private Boolean max_persons;
    private Double duration;
    private Price price;
    private Double rating;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private transient City city;
    private Photo[] photos;
    private String status;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public Boolean getIs_new() {
        return is_new;
    }

    public String getGroup() {
        return group;
    }

    public Boolean getInstant_booking() {
        return instant_booking;
    }

    public Boolean getChild_friendly() {
        return child_friendly;
    }

    public Boolean getMax_persons() {
        return max_persons;
    }

    public Double getDuration() {
        return duration;
    }

    public Price getPrice() {
        return price;
    }

    public Double getRating() {
        return rating;
    }

    public City getCity() {
        return city;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public String getStatus() {
        return status;
    }

    static class Price{
        private BigDecimal value;
        private String currency;
        private String value_string;

        public BigDecimal getValue() {
            return value;
        }

        public String getCurrency() {
            return currency;
        }

        public String getValue_string() {
            return value_string;
        }
    }

    static class City{
        private String name_ru;
        private String name_en;
        private String iata;

        public String getName_ru() {
            return name_ru;
        }

        public String getName_en() {
            return name_en;
        }

        public String getIata() {
            return iata;
        }
    }

    static class Photo{
        private String thumbnail;
        private String medium;

        public String getThumbnail() {
            return thumbnail;
        }

        public String getMedium() {
            return medium;
        }
    }
}
