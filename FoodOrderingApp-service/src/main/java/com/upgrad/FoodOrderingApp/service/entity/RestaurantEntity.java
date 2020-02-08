package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="restaurant")
@NamedQueries({
        @NamedQuery(name = "allRestaurants", query = "select r from RestaurantEntity r ORDER BY r.restaurant_name")
})
public class RestaurantEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    private UUID uuid;

    @Column(name = "RESTAURANT_NAME")
    @Size(max = 50)
    private String restaurant_name;

    @Column(name = "PHOTO_URL")
    @Size(max = 255)
    private String photo_url;

    @Column(name = "CUSTOMER_RATING")
    private BigDecimal customer_rating;

    @Column(name = "AVERAGE_PRICE_FOR_TWO")
    private int average_price_for_two;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    private int number_of_customers_rated;

    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity addressEntity;

    @ManyToMany
    @JoinTable(name="restaurant_category")
    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public BigDecimal getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(BigDecimal customer_rating) {
        this.customer_rating = customer_rating;
    }

    public int getAverage_price_for_two() {
        return average_price_for_two;
    }

    public void setAverage_price_for_two(int average_price_for_two) {
        this.average_price_for_two = average_price_for_two;
    }

    public int getNumber_of_customers_rated() {
        return number_of_customers_rated;
    }

    public void setNumber_of_customers_rated(int number_of_customers_rated) {
        this.number_of_customers_rated = number_of_customers_rated;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

}
