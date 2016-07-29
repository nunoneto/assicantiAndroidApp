package com.nunoneto.assicanti.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.OrderPhase;
import com.nunoneto.assicanti.model.entity.realm.Price;
import com.nunoneto.assicanti.ui.fragment.CustomerDataFragment;
import com.nunoneto.assicanti.ui.fragment.MenuFragment;
import com.nunoneto.assicanti.ui.fragment.OnOrderFragmentListener;
import com.nunoneto.assicanti.ui.fragment.OptionalsFragment;
import com.nunoneto.assicanti.ui.fragment.OrderSummaryFragment;

public class DayMenuActivity extends NavigationDrawerActivity
        implements OnOrderFragmentListener {

    private final static String TAG = "DAYMENU_ACTIVITY";

    private int orderPhase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //clean state
        DataModel.getInstance().setCurrentOrder(null);
        DataModel.getInstance().setOptionalGroups(null);

        showMenus();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1);
                Log.i(TAG,entry.getId()+"");
                Log.i(TAG,entry.getName()+"");
                Log.i(TAG,entry.getClass()+"");

            }
        });
    }

    private void showMenus(){
        updateNavigation(OrderPhase.MENU);

        MenuFragment frag = new MenuFragment();
        String backStateName = frag.getClass().getName();
        replaceFragment(frag,backStateName, MenuFragment.TAG);
    }



    private void updateNavigation(int orderPhase){
        this.orderPhase = orderPhase;
        if(orderPhase > OrderPhase.MENU){

        }else{

            getSupportActionBar().setHomeButtonEnabled(false);
        }

    }

    @Override
    public void showOptionals(Price price) {
        updateNavigation(OrderPhase.OPTIONALS);

        OptionalsFragment frag = OptionalsFragment.newInstance(price.getItemId(),price.getSize(),price.getTier(),price.getId());
        String backStateName = frag.getClass().getName();
        replaceFragment(frag,backStateName, OptionalsFragment.TAG);
    }

    @Override
    public void showForm() {
        updateNavigation(OrderPhase.CUSTOMERDATA);

        CustomerDataFragment frag = CustomerDataFragment.newInstance();
        String backStateName = frag.getClass().getName();
        replaceFragment(frag,backStateName, CustomerDataFragment.TAG);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void goToSummary(String orderNumber, String deliveryDate) {
        updateNavigation(OrderPhase.SUMMARY);

        OrderSummaryFragment frag = OrderSummaryFragment.newInstance(orderNumber,deliveryDate);
        String backStateName = frag.getClass().getName();
        replaceFragment(frag,backStateName, OrderSummaryFragment.TAG);
    }

    private void replaceFragment (Fragment fragment, String backStateName, String tag){


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment,tag);
        ft.addToBackStack(null);
        ft.commit();
    }


}
