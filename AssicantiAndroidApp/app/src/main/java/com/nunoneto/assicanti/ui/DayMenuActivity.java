package com.nunoneto.assicanti.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.entity.OrderPhase;
import com.nunoneto.assicanti.model.entity.realm.Price;
import com.nunoneto.assicanti.ui.fragment.CustomerDataFragment;
import com.nunoneto.assicanti.ui.fragment.MenuFragment;
import com.nunoneto.assicanti.ui.fragment.OnOrderFragmentListener;
import com.nunoneto.assicanti.ui.fragment.OptionalsFragment;
import com.nunoneto.assicanti.ui.fragment.OrderSummaryFragment;

public class DayMenuActivity extends NavigationDrawerActivity
        implements OnOrderFragmentListener {

    private int orderPhase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showMenus();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });
    }

    private void showMenus(){
        updateNavigation(OrderPhase.MENU);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,new MenuFragment(),MenuFragment.NAME)
                .addToBackStack(null)
                .commit();
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
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, OptionalsFragment.newInstance(price.getItemId(),price.getSize(),price.getTier(),price.getId()),OptionalsFragment.NAME)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showForm() {
        updateNavigation(OrderPhase.CUSTOMERDATA);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, CustomerDataFragment.newInstance(),CustomerDataFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void goToSummary(String orderNumber, String deliveryDate) {
        updateNavigation(OrderPhase.SUMMARY);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, OrderSummaryFragment.newInstance(orderNumber,deliveryDate),OrderSummaryFragment.TAG)
                .addToBackStack(null)
                .commit();

    }


}
