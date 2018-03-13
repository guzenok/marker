package com.marker.fabel.android_client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.marker.fabel.android_client.models.Mark;
import com.marker.fabel.android_client.models.Sheet;


public class MarkDescrItem extends RecyclerView.ViewHolder {
    private TextView author;
    private TextView descr;

    private Mark item;

    public Mark getItem() { return item; }

    public void setItem(Mark s) {
        item = s;
        author.setText(item.getAuthor().getPhone());
        descr.setText(item.getDescr());
    }

    public MarkDescrItem(View view, final DetailActivity context) {
        super(view);
        this.author = (TextView) view.findViewById(R.id.mark_author);
        this.descr = (TextView) view.findViewById(R.id.mark_descr);

        CardView cv = (CardView) view.findViewById(R.id.mdiCardView);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onMarkClick(item);
            }
        });
    }

}
