package com.marker.fabel.android_client;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.marker.fabel.android_client.models.Mark;


public class MarkValueItem extends RecyclerView.ViewHolder {
    private TextView author;
    private TextView value;
    private TextView dt;

    private Mark item;

    public Mark getItem() { return item; }

    public void setItem(Mark s) {
        item = s;
        author.setText(item.getAuthor().getPhone());
        value.setText(item.getValue().toString());
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy HH:mm"); // getString(R.string.uid_param);
        dt.setText( df.format( item.getDt() ) );
    }

    public MarkValueItem(View view, final DetailActivity context) {
        super(view);
        this.author = (TextView) view.findViewById(R.id.mark_author);
        this.value = (TextView) view.findViewById(R.id.mark_value);
        this.dt = (TextView) view.findViewById(R.id.mark_dt);

        CardView cv = (CardView) view.findViewById(R.id.mviCardView);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onMarkClick(item);
            }
        });
    }

}
