package com.bizarrecoding.example.bakemania;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;
import com.bizarrecoding.example.bakemania.adapters.RemoteRecipeViewsFactory;

public class RemoteRecipeViewsService extends RemoteViewsService{

    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteRecipeViewsFactory(this.getApplicationContext());
    }
}
