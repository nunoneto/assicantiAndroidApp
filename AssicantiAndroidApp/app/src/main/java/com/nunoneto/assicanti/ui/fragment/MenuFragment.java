package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.Utils;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.DayMenu;
import com.nunoneto.assicanti.model.MenuType;
import com.nunoneto.assicanti.model.Price;
import com.nunoneto.assicanti.network.RestService;
import com.nunoneto.assicanti.tasks.GetMenusTask;
import com.nunoneto.assicanti.ui.adapters.PriceSpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MenuFragment extends Fragment {

    public static final String NAME = "MENU";

    private OnOrderFragmentListener mListener;
    private final static String TAG = "FRAG_MENUS";

    //Tasks
    private GetMenusTask getMenusTask;

    // Views
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout scrollView;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private LinearLayout noMenuWarning;

    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMenu(false);
    }

    private void updateMenu(boolean force){
        noMenuWarning.setVisibility(View.GONE);
        contentLoadingProgressBar.show();
        getMenusTask = new GetMenusTask(MenuFragment.this);
        getMenusTask.execute(force);

    }

    @Override
    public void onResume() {
        super.onResume();
        RestService.getInstance().clearCookies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        contentLoadingProgressBar = (ContentLoadingProgressBar)root.findViewById(R.id.loading);
        scrollView = (LinearLayout)root.findViewById(R.id.menuContainer);
        swipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipeRefresh);
        noMenuWarning = (LinearLayout) root.findViewById(R.id.noMenuWarning);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getMenusTask.getStatus() == AsyncTask.Status.PENDING || getMenusTask.getStatus() == AsyncTask.Status.RUNNING)
                    swipeRefreshLayout.setRefreshing(false);
                else{
                    getMenusTask = new GetMenusTask(MenuFragment.this);
                    getMenusTask.execute(true);
                    RestService.getInstance().clearCookies();
                }
            }
        });

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderFragmentListener) {
            mListener = (OnOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderFragmentListener");
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

    private void toggleNoMenuWarning(){
        noMenuWarning.setVisibility(View.VISIBLE);

        ImageView arrow = (ImageView) noMenuWarning.findViewById(R.id.arrowImg);

        arrow.clearAnimation();
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0f, 0f, arrow.getHeight()/3);
        translation.setStartOffset(500);
        translation.setDuration(1500);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());
        translation.setRepeatCount(Animation.INFINITE);
        arrow.startAnimation(translation);

    }

    public void loadWeekMenu(){

        HashMap<MenuType,DayMenu> dayMenuList = DataModel.getInstance().getCurrentDayMenu();
        String dateTitle = null;

        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

        if(scrollView.getChildCount() > 0)
            scrollView.removeAllViews();

        if(dayMenuList == null || dayMenuList.size() <= 0){
            toggleNoMenuWarning();
        }else{
            for(MenuType menuType : dayMenuList.keySet()){
                DayMenu dayMenu = dayMenuList.get(menuType);
                CardView card = (CardView) inflater.inflate(R.layout.view_menu_card,null);
                card.setId(View.generateViewId());

                if(dateTitle == null){
                    SimpleDateFormat sdf = new SimpleDateFormat("E',' d 'de' MMMM",new Locale("pt","pt"));
                    dateTitle = sdf.format(DataModel.getInstance().getTargetDate());
                    mListener.setTitle(dateTitle);
                }

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

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        else
            contentLoadingProgressBar.hide();
    }

}


