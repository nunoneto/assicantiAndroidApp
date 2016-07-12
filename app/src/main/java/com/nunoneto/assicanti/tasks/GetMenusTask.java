package com.nunoneto.assicanti.tasks;

import android.os.AsyncTask;

import com.nunoneto.assicanti.model.Menu;
import com.nunoneto.assicanti.webscraper.WebScrapper;

import java.util.List;

/**
 * Created by NB20301 on 12/07/2016.
 */
public class GetMenusTask extends AsyncTask<Void,Void,List<Menu>> {

    @Override
    protected List<Menu> doInBackground(Void... voids) {
        return WebScrapper.getInstance().getMenus();
    }

    @Override
    protected void onPostExecute(List<Menu> menus) {
        super.onPostExecute(menus);
    }
}
