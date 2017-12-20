package com.bizarrecoding.example.bakemania.objects;

import android.util.Log;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class Recipe extends SugarRecord{
    @Unique long rid;
    String name;
    int servings;
    String image;
    int remember;

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

    public void setRemember(int remember){
        this.remember= remember;
    }

    public Recipe(){}

    public Recipe(int id, String name, int servings, String image){
        this.rid = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
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
                    String[] values = new String[]{ String.valueOf(rid*100+i) };
                    boolean exist = 0 < Ingredient.count(Ingredient.class,"idingredient=?",values);
                    JSONObject ingredientObj = ingredientsjs.getJSONObject(i);
                    if(!exist){
                        Ingredient ingredient = new Ingredient(ingredientObj);
                        ingredient.setRid(rid);
                        ingredient.setIdingredient(i);
                        ingredient.setId(rid*100+i);
                        ingredient.save();
                    }else{
                        //ingredient.update();
                        try{
                            int quantity = ingredientObj.has("quantity") ? ingredientObj.getInt("quantity") : 0;
                            String measure  = ingredientObj.has("measure") ? ingredientObj.getString("measure") : "";
                            String name  = ingredientObj.has("ingredient") ? ingredientObj.getString("ingredient") : "";
                            Ingredient.executeQuery(
                                    "UPDATE INGREDIENT SET NAME = ?, MEASURE = ?, QUANTITY =? WHERE ID = ?",
                                    new String[]{name,measure,String.valueOf(quantity),String.valueOf(rid*100+i)}
                            );
                        }catch (Exception e){
                            Log.e("Error",e.getMessage());
                            e.printStackTrace();
                        }
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
                    if(!exist){
                        step.save();
                    }else{
                        step.update();
                    }
                }
            }
            this.remember = 0; //rid == 2 ? 1 : 0;
        } catch (JSONException e) {
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        Log.d("","====================================================="
                +"\nid: "+this.getId()
                +"\nname: "+this.getName()
                +"\nservings: "+this.getServings()
                +"\n=====================================================");
        return super.toString();
    }
}
