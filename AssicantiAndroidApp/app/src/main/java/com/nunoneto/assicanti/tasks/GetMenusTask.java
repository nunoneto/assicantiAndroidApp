package com.nunoneto.assicanti.tasks;

import android.os.AsyncTask;

import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.WeekMenu;
import com.nunoneto.assicanti.ui.fragment.MenuFragment;
import com.nunoneto.assicanti.webscraper.WebScrapper;

/**
 * Created by NB20301 on 13/07/2016.
 */
public class GetMenusTask extends AsyncTask<Boolean,Void,Void> {

    private MenuFragment menuFragment;

    public GetMenusTask(MenuFragment menuFragment){
        this.menuFragment = menuFragment;
    }


    @Override
    protected Void doInBackground(Boolean... forceUpdate) {
        WebScrapper.getInstance().getMenus(forceUpdate[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        menuFragment.loadWeekMenu();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
