package com.digregorio.gw2tinkering.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digregorio.gw2tinkering.R;
import com.digregorio.gw2tinkering.model.World;

import java.util.List;

public class WorldAdapter extends ArrayAdapter<World> {

    final Context mContext;
    List<World> mWorlds;

    public WorldAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<World> worlds) {
        super(context, resource, worlds);

        mContext = context;
        mWorlds = worlds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.world_item_row, parent, false);
        TextView nameTextView = view.findViewById(R.id.txtName);
        TextView populationTextView = view.findViewById(R.id.txtPopulation);

        nameTextView.setText(mWorlds.get(position).name);
        populationTextView.setText(mWorlds.get(position).population);

        return view;
    }

    @Override
    public int getCount() {
        return mWorlds != null ? mWorlds.size() : 0;
    }
}
