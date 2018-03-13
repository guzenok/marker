package com.marker.fabel.android_client;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;

import com.marker.fabel.android_client.filters.RangeInputFilter;
import com.marker.fabel.android_client.models.Mark;
import com.marker.fabel.android_client.models.Sheet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateSheetActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText sheetName;
    private static EditText markValue;
    private static EditText markDescr;
    private static Button btnSaveSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sheet_activity);

        sheetName = (EditText) findViewById(R.id.sheet_name);

        markValue = (EditText) findViewById(R.id.mark_value);
        markValue.setFilters(new InputFilter[]{new RangeInputFilter("0", "10")});

        markDescr = (EditText) findViewById(R.id.mark_descr);

        btnSaveSheet = (Button) findViewById(R.id.btn_save);
        btnSaveSheet.setOnClickListener(this);
    }

    // save new sheet
    @Override
    public void onClick(View v) {
        final CreateSheetActivity activity = this;
        switch (v.getId()) {
            case R.id.btn_save:

                if( markValue.getText().length()<1 ) markValue.setText("0");
                if( markDescr.getText().length()<1 ) markDescr.setText("?");
                if(sheetName.getText().length()<1 ) {
                    App.Message(this,getString(R.string.msg_empty_fields));
                    return;
                }
                final ProgressDialog pd;
                pd = ProgressDialog.show(this,"",getString(R.string.msg_loading),true);
                App.getApi().saveSheet(sheetName.getText().toString()).enqueue(
                    new Callback<Sheet>() {
                        @Override
                        public void onResponse(Call<Sheet> call, Response<Sheet> response) {
                            if( response.body()==null) {
                                pd.dismiss();
                                Log.e("API.SaveSheet", call.toString());
                                App.Message(activity, getString(R.string.err_get_sheet));
                                return;
                            }
                            App.getApi().saveMark(response.body().getId(), Integer.parseInt(markValue.getText().toString()), markDescr.getText().toString()).enqueue(
                                new Callback<Mark>() {
                                    @Override
                                    public void onResponse(Call<Mark> call, Response<Mark> response) {
                                        pd.dismiss();
                                        App.Message(activity, getString(R.string.ok));
                                        activity.finish();
                                    }

                                    @Override
                                    public void onFailure(Call<Mark> call, Throwable t) {
                                        pd.dismiss();
                                        Log.e("API.SaveMark", call.toString(), t);
                                        App.Message(activity, getString(R.string.err_save_mark));
                                    }
                                }
                            );
                        }

                        @Override
                        public void onFailure(Call<Sheet> call, Throwable t) {
                            pd.dismiss();
                            Log.e("API.SaveSheet", call.toString(), t);
                            App.Message(activity, getString(R.string.err_save_sheet));
                        }
                    }
                );
                break;
            default:
                break;
        }
    }
}
