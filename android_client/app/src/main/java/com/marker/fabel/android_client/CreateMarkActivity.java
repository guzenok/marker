package com.marker.fabel.android_client;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.marker.fabel.android_client.filters.RangeInputFilter;
import com.marker.fabel.android_client.models.Mark;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMarkActivity extends AppCompatActivity implements View.OnClickListener, Callback<Mark> {

    private Long sheetId;

    private ProgressDialog pd;

    private static EditText markValue;
    private static EditText markDescr;
    private static Button btnSaveSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_mark_activity);

        Bundle b = getIntent().getExtras();
        sheetId = b.getLong("sheet_id");

        markValue = (EditText) findViewById(R.id.mark_value);
        markValue.setFilters(new InputFilter[]{new RangeInputFilter("1", "10")});
        markValue.setText( Integer.toString( b.getInt("mark_value") ) );

        markDescr = (EditText) findViewById(R.id.mark_descr);
        markDescr.setText( b.getString("mark_descr") );

        btnSaveSheet = (Button) findViewById(R.id.btn_save);
        btnSaveSheet.setOnClickListener(this);
    }

    // save new sheet
    @Override
    public void onClick(View v) {
        if( markValue.getText().length()<1 ) markValue.setText("10");
        if(markDescr.getText().length()<1 ) {
            App.Message(this,getString(R.string.msg_empty_fields));
            return;
        }
        switch (v.getId()) {
            case R.id.btn_save:
                pd = ProgressDialog.show(this,"",getString(R.string.msg_loading),true);
                App.getApi().saveMark(sheetId, Integer.parseInt(markValue.getText().toString()), markDescr.getText().toString())
                        .enqueue(this);
                break;
        }
    }

    @Override
    public void onResponse(Call<Mark> call, Response<Mark> response) {
        pd.dismiss();
        App.Message(this, getString(R.string.ok));
        this.finish();
    }

    @Override
    public void onFailure(Call<Mark> call, Throwable t) {
        pd.dismiss();
        Log.e("API.SaveMark", call.toString(), t);
        App.Message(this, getString(R.string.err_save_mark));
    }

}