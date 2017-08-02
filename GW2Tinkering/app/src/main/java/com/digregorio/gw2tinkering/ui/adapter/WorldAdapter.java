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

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by X2V0 on 8/2/2017.
 */

public class WorldAdapter extends ArrayAdapter<World> {

    private List<World> mWorlds;

    public WorldAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<World> worlds) {
        super(context, resource, worlds);

        mWorlds = worlds;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        //return super.getView(position, view, parent);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.world_item_row, parent);
        TextView nameTextView = (TextView) view.findViewById(R.id.txtName);
        TextView populationTextView = (TextView) view.findViewById(R.id.txtPopulation);

        nameTextView.setText(mWorlds.get(position).name);
        populationTextView.setText(mWorlds.get(position).population);

        return view;

    }


}
