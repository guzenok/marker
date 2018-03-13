package com.marker.fabel.android_client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.marker.fabel.android_client.models.Sheet;


public class SheetItem extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView mark;

    private Sheet item;

    public Sheet getItem() { return item; }

    public void setItem(Sheet s) {
        item = s;
        name.setText(item.getName());
        mark.setText(item.getMark().toString());
    }

    public SheetItem(View view, final MainActivity context) {
        super(view);
        this.name = (TextView) view.findViewById(R.id.sheet_name);
        this.mark = (TextView) view.findViewById(R.id.mark_value);

        CardView cv = (CardView) view.findViewById(R.id.cardView);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onSheetClick(item);
            }
        });
    }

}
