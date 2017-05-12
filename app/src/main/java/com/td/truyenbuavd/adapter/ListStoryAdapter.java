package com.td.truyenbuavd.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.truyenbuavd.R;
import com.td.truyenbuavd.helper.TDHelper;
import com.td.truyenbuavd.model.TDStory;

public class ListStoryAdapter extends BaseAdapter{
    
    private List<TDStory> listStory;
    private static LayoutInflater inflater = null;
    private Context context;
    
    public ListStoryAdapter (Context context, List<TDStory> listStory) {
        this.listStory = listStory;
        this.context = context;
        inflater = ((Activity) context).getLayoutInflater();
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return listStory.size();
    }

    public TDStory getItem(int pos) {
        // TODO Auto-generated method stub
        return listStory.get(pos);
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public class Holder{
        TextView tv_story;
    }

    public View getView(final int pos, View view, ViewGroup arg2) {
        View vi = view;
        if (view == null) {
            vi = inflater.inflate(R.layout.row_story, null);
            Holder holder = new Holder();
            holder.tv_story = (TextView)
                    vi.findViewById(R.id.tv_story);
            vi.setTag(holder);
        }
        Holder holder = (Holder) vi.getTag();
        holder.tv_story.setText(listStory.get(pos).name);
        
        return vi;
    }
}
