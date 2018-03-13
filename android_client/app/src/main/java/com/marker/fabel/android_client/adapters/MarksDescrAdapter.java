package com.marker.fabel.android_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marker.fabel.android_client.App;
import com.marker.fabel.android_client.DetailActivity;
import com.marker.fabel.android_client.MarkDescrItem;
import com.marker.fabel.android_client.MarkValueItem;
import com.marker.fabel.android_client.R;
import com.marker.fabel.android_client.models.Mark;
import com.marker.fabel.android_client.models.Sheet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarksDescrAdapter extends RecyclerView.Adapter<MarkDescrItem> {
    private DetailActivity context;
    private List<Mark> marks;

    public MarksDescrAdapter(DetailActivity context) {
        super();
        this.context = context;
    }

    public Mark getItem(int i) {
        return ( marks != null ? marks.get(i) : null);
    }

    public void refreshData(Sheet sheet) {
        marks = sheet.notEmptyMarks();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (marks != null ? marks.size() : 0);
    }

    @Override
    public MarkDescrItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.mark_descr_item, viewGroup, false);
        MarkDescrItem mainHolder = new MarkDescrItem(mainGroup, context) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }

    @Override
    public void onBindViewHolder(MarkDescrItem holder, int position) {
        Mark mark = getItem(position);
        if( mark!=null ) holder.setItem(mark);
    }
}
