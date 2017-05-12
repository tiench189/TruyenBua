package com.td.truyenbuavd.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.truyenbuavd.R;
import com.td.truyenbuavd.model.TDCategory;

public class ListCategoryAdapter extends BaseAdapter{
    
    private List<TDCategory> listCategory;
    private static LayoutInflater inflater = null;
    
    public ListCategoryAdapter (Context context, List<TDCategory> listCategory) {
        this.listCategory = listCategory;
        inflater = ((Activity) context).getLayoutInflater();
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return listCategory.size();
    }

    public TDCategory getItem(int pos) {
        // TODO Auto-generated method stub
        return listCategory.get(pos);
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public class Holder{
        TextView tv_category_title;
    }

    public View getView(int pos, View view, ViewGroup arg2) {
        View vi = view;
        if (view == null) {
            vi = inflater.inflate(R.layout.row_category, null);
            Holder holder = new Holder();
            holder.tv_category_title = (TextView)
                    vi.findViewById(R.id.tv_category_title);
            vi.setTag(holder);
        }
        Holder holder = (Holder) vi.getTag();
        holder.tv_category_title.setText(listCategory.get(pos).name);
        return vi;
    }

}
