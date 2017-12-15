package com.bizarrecoding.example.bakemania;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.bizarrecoding.example.bakemania.adapters.RemoteIngredientViewsFactory;

/**
 * Created by Herik on 22/10/2017.
 */

public class RemoteIngredientViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        long rid = intent.getLongExtra("rid",0);
        Log.d("WIDGET service","action: "+intent.getAction());
        return new RemoteIngredientViewsFactory(this.getApplicationContext(),rid);
    }
}
