package com.bizarrecoding.example.bakemania.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe implements Parcelable{
    int id;
    String name;
    int servings;
    String image;
    List<Ingredient> ingredients;
    List<Step> steps;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getServings() {
        return servings;
    }
    public String getImage() {
        return image;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public List<Step> getSteps() {
        return steps;
    }

    public Recipe(int id, String name, int servings, String image, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe(JSONObject json) {
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        try {
            this.id = json.has("id") ? json.getInt("id") : -1;
            this.name = json.has("name") ? json.getString("name") : "";
            this.servings = json.has("servings") ? json.getInt("servings") : -1;
            this.image = json.has("image") ? json.getString("image") : "";
            if(json.has("ingrediends")){
                JSONArray ingredientsjs = json.getJSONArray("ingredients");
                for (int i = 0; i < ingredientsjs.length(); i++ ){
                    JSONObject ingredientObj = ingredientsjs.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(ingredientObj);
                    ingredients.add(ingredient);
                }
            }
            if (json.has("steps")){
                JSONArray stepsJS = json.getJSONArray("steps");
                for (int i = 0; i < stepsJS.length(); i++ ){
                    JSONObject stepObj = stepsJS.getJSONObject(i);
                    Step step = new Step(stepObj);
                    steps.add(step);
                }
            }
        } catch (JSONException e) {
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeTypedList(steps);
        dest.writeTypedList(ingredients);
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        steps = new ArrayList<>();
        in.readTypedList(steps,Step.CREATOR);
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients,Ingredient.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
