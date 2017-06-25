package com.adeoluwa.android.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Merlyne on 6/24/2017.
 */

public class IngredientsWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
