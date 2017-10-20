package com.bizarrecoding.example.bakemania.objects;

import android.util.Log;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe extends SugarRecord{
    @Unique long rid;
    String name;
    int servings;
    String image;
    //List<Ingredient> ingredients;
    //List<Step> steps;

    public long getRid(){ return rid; }
    public String getName() {
        return name;
    }
    public int getServings() {
        return servings;
    }
    public String getImage() {
        return image;
    }
/*    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public List<Step> getSteps() {
        return steps;
    }
*/
    public Recipe(){}

    public Recipe(int id, String name, int servings, String image, List<Ingredient> ingredients, List<Step> steps) {
        this.rid = id;
        this.name = name;
        this.servings = servings;
        this.image = image;/*
        this.ingredients = ingredients;
        this.steps = steps;*/
    }

    public Recipe(JSONObject json) {
        try {
            this.rid = (json.has("id") ? json.getLong("id") : 0)-1;
            this.name = json.has("name") ? json.getString("name") : "";
            this.servings = json.has("servings") ? json.getInt("servings") : -1;
            this.image = json.has("image") ? json.getString("image") : "";
            if(json.has("ingredients")){
                JSONArray ingredientsjs = json.getJSONArray("ingredients");
                for (int i = 0; i < ingredientsjs.length(); i++ ){
                    JSONObject ingredientObj = ingredientsjs.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(ingredientObj);
                    ingredient.setRid(rid);
                    ingredient.setIdingredient(i);
                    ingredient.setId(rid*100+i);
                    String[] values = new String[]{ String.valueOf(i) };
                    boolean exist = 0 < Ingredient.count(Ingredient.class,"idingredient=?",values);
                    ingredient.update();
                    if(!exist){
                        ingredient.save();
                    }else{
                        ingredient.update();
                    }
                }
            }
            if (json.has("steps")){
                JSONArray stepsJS = json.getJSONArray("steps");
                for (int i = 0; i < stepsJS.length(); i++ ) {
                    JSONObject stepObj = stepsJS.getJSONObject(i);
                    Step step = new Step(stepObj);
                    step.setRid(rid);
                    step.setSid(i);
                    step.setId(rid*100+i);
                    String[] values = new String[]{String.valueOf(rid*100+i)};
                    boolean exist = 0 < Step.count(Step.class, "sid=?", values);
                    //Log.d("STEPS CREATE","step sid:"+step.sid+"\nrid: "+rid+"\nfound: "+exist);
                    if(!exist){
                        step.save();
                    }else{
                        step.update();
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
    }
}
