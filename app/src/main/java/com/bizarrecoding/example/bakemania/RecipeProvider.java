package com.bizarrecoding.example.bakemania;

import com.orm.SugarRecord;

/**
 * Created by Herik on 19/10/2017.
 */

public class RecipeProvider extends SugarRecord {
    String title;
    String edition;

    public RecipeProvider(){
    }

    public RecipeProvider(String title, String edition){
        this.title = title;
        this.edition = edition;
    }
}

