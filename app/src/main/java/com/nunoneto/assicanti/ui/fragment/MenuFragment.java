package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.Utils;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.DayMenu;
import com.nunoneto.assicanti.model.MenuType;
import com.nunoneto.assicanti.model.Price;
import com.nunoneto.assicanti.model.WeekMenu;
import com.nunoneto.assicanti.tasks.GetMenusTask;
import com.nunoneto.assicanti.ui.adapters.PriceSpinnerAdapter;
import com.nunoneto.assicanti.webscraper.WebScrapper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class MenuFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private final static String TAG = "FRAG_MENUS";

    //Tasks
    private GetMenusTask getMenusTask;

    // Views
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout scrollView;

    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMenusTask = new GetMenusTask(MenuFragment.this);
        getMenusTask.execute(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        scrollView = (LinearLayout)root.findViewById(R.id.menuContainer);
        swipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getMenusTask.getStatus() == AsyncTask.Status.PENDING || getMenusTask.getStatus() == AsyncTask.Status.RUNNING)
                    swipeRefreshLayout.setRefreshing(false);
                else{
                    getMenusTask = new GetMenusTask(MenuFragment.this);
                    getMenusTask.execute(true);
                }
            }
        });

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        getMenusTask.cancel(true);
    }

    public interface OnFragmentInteractionListener {

        void showOptionals(Price price);
    }

    public void loadWeekMenu(){
        WeekMenu menu = DataModel.getInstance().getCurrentMenu();
        Calendar cal = Utils.getCalendar();

        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

        if(scrollView.getChildCount() > 0){
            scrollView.removeAllViews();
        }

        for(MenuType menuType : menu.getTypes()){
            for(DayMenu dayMenu : menuType.getDays()){
                if( dayMenu.getDayOfWeek() == cal.get(Calendar.DAY_OF_WEEK)){

                    CardView card = (CardView) inflater.inflate(R.layout.view_menu_card,null);
                    card.setId(View.generateViewId());


                    Bitmap bitmap = BitmapFactory.decodeByteArray(menuType.getMenuTypeImage().getImage(),0,menuType.getMenuTypeImage().getImage().length);
                    bitmap = Utils.blurRenderScript(getContext(),bitmap,3);
                    ((ImageView)card.findViewById(R.id.imageMenuType)).setImageBitmap(bitmap);
                    ((TextView)card.findViewById(R.id.menuType)).setText(dayMenu.getType());
                    ((TextView)card.findViewById(R.id.menuDescription)).setText(dayMenu.getDescription());

                    PriceSpinnerAdapter adapter = new PriceSpinnerAdapter(
                            getActivity().getApplicationContext(),
                            R.layout.spinner_price,
                            menuType.getPrices()
                    );
                    final AppCompatSpinner appCompatSpinner = ((AppCompatSpinner)card.findViewById(R.id.priceSpinner));
                    appCompatSpinner.setAdapter(adapter);

                    card.findViewById(R.id.orderMenu).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Price price = (Price)appCompatSpinner.getSelectedItem();
                            MenuFragment.this.mListener.showOptionals(price);
                        }
                    });

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    int margin = Utils.dpToPx(8,getContext());
                    params.setMargins(margin,margin,margin,margin);
                    scrollView.addView(card,params);

                }
            }
        }

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

    }

}


