package com.marker.fabel.android_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import com.marker.fabel.android_client.App;
import com.marker.fabel.android_client.MainActivity;
import com.marker.fabel.android_client.R;
import com.marker.fabel.android_client.SheetItem;
import com.marker.fabel.android_client.models.Sheet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.text.TextWatcher;

public class SheetsAdapter extends RecyclerView.Adapter<SheetItem> implements TextWatcher {
    private MainActivity context;
    private View.OnClickListener clickListener;
    private String category;
    private List<Sheet> sheetList;
    private String searchStr;

    public SheetsAdapter(MainActivity context, String category) {
        super();
        this.context = context;
        this.category = category;
        refreshData();
    }

    public Sheet getItem(int i) {
        return (null != sheetList ? sheetList.get(i) : null);
    }

    public void refreshData() {
        App.getApi().getSheets(category, searchStr).enqueue(new Callback<List<Sheet>>() {
            @Override
            public void onResponse(Call<List<Sheet>> call, Response<List<Sheet>> response) {
                if( response!=null){
                    sheetList = response.body();
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Sheet>> call, Throwable t) {
                Log.e("API-call", call.toString(), t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != sheetList ? sheetList.size() : 0);
    }

    @Override
    public SheetItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.sheet_item, viewGroup, false);
        SheetItem mainHolder = new SheetItem(mainGroup, context) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }

    @Override
    public void onBindViewHolder(SheetItem holder, int position) {
        Sheet sheet = sheetList.get(position);
        holder.setItem(sheet);
    }

    public void afterTextChanged(Editable s) {
        this.searchStr = s.toString();
        if( this.searchStr.length()<1 ) {
            this.searchStr = null;
        }
        this.refreshData();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
