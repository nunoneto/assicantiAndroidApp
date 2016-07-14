package com.nunoneto.assicanti.ui.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.OptionalItem;

import java.util.List;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class OptionalsListAdapter extends RecyclerView.Adapter<OptionalsListAdapter.ViewHoler> {

    private List<OptionalItem> optionalItemList;
    private OptionalItemClickListener listener;

    public OptionalsListAdapter(List<OptionalItem> optionalItemList, OptionalItemClickListener listener) {
        this.optionalItemList = optionalItemList;
        this.listener = listener;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {

        public AppCompatCheckBox appCompatCheckBox;

        public ViewHoler(View itemView) {
            super(itemView);
            this.appCompatCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }

    }

    public interface OptionalItemClickListener{
        void onItemChecked(boolean checked, OptionalItem item, CompoundButton compoundButton);
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.optional_list_item,parent,false);
        ViewHoler viewHoler = new ViewHoler(item);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, final int position) {
        holder.appCompatCheckBox.setText(
                optionalItemList.get(position).getName()
        );
        holder.appCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.onItemChecked(b,OptionalsListAdapter.this.optionalItemList.get(position),compoundButton);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionalItemList.size();
    }

}
