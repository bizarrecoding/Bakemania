package com.bizarrecoding.example.bakemania;

import android.content.Intent;
import android.widget.RemoteViewsService;
import com.bizarrecoding.example.bakemania.adapters.RemoteListViewsFactory;

public class RemoteListViewsService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListViewsFactory(this.getApplicationContext());
    }
}
