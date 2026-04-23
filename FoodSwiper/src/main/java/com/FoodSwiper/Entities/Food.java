package com.FoodSwiper.Entities;

import java.util.Objects;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
public class Food extends Item{
    private Integer calories;

    public Food(String name, String description, Integer calories){
        super(name, description);
        this.calories = calories;
    }
    public Food(){
        this("", "", 0);
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Food food = (Food) o;
        return Objects.equals(calories, food.calories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), calories);
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + super.getId() +
                "name=" + super.getName() +
                "description=" + super.getDescription()+
                "calories=" + calories +
                '}';
    }
}
