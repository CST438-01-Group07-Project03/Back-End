package com.FoodSwiper.Entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 *
 * 4/26/26 Fernando - updated fields from yelp dataset
 */
@Entity
public class Restaurant extends Item{
    private String yelpId;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private double stars;
    private int reviewCount;

    @Column(length = 500)
    private String categories;

    private String imageUrl;

    public Restaurant() {
        super();
    }

    public Restaurant(String name, String yelpId, String address, String city, String state,
                      String zipCode, double stars, int reviewCount, String categories, String imageUrl) {
        super(name, "");
        this.yelpId = yelpId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.stars = stars;
        this.reviewCount = reviewCount;
        this.categories = categories;
        this.imageUrl = imageUrl;
    }

    public String getYelpId() { return yelpId; }
    public void setYelpId(String yelpId) { this.yelpId = yelpId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public double getStars() { return stars; }
    public void setStars(double stars) { this.stars = stars; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public String getCategories() { return categories; }
    public void setCategories(String categories) { this.categories = categories; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
