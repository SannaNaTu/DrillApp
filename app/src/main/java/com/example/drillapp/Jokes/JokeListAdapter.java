package com.example.drillapp.Jokes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.drillapp.R;

import java.util.ArrayList;

public class JokeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Joke> dataModelArrayList;

    public JokeListAdapter(Context context, ArrayList<Joke> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.joke, null, true);

            holder.tvjoke = (TextView) convertView.findViewById(R.id.jokeText);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvjoke.setText("Name: "+dataModelArrayList.get(position).getJokeContent());


        return convertView;
    }

    private class ViewHolder {
        protected TextView tvjoke;

    }


}

//public void onclick
//if linearLayout. getvisibility. view.gone
//linearlayout. setvisibility.view.visible
//else
//    linearlayout.