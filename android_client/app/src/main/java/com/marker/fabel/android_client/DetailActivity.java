package com.marker.fabel.android_client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.marker.fabel.android_client.adapters.MarksDescrAdapter;
import com.marker.fabel.android_client.adapters.MarksValueAdapter;
import com.marker.fabel.android_client.models.Mark;
import com.marker.fabel.android_client.models.Sheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener, Callback<Sheet> {

    private Long sheetId;
    private Sheet sheet;

    private ProgressDialog pd;

    private TextView sheetName;
    private TextView sheetMark;
    private FloatingActionButton btnCreateMark;
    private FloatingActionButton btnDeleteSheet;
    private FloatingActionButton btnShareSheet;
    private FloatingActionButton btnMarkSheet;

    private MarksValueAdapter mva;
    private MarksDescrAdapter mda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        sheetName = (TextView) findViewById(R.id.sheet_name);
        sheetMark = (TextView) findViewById(R.id.sheet_mark);

        btnCreateMark = (FloatingActionButton) findViewById(R.id.btn_new);
        btnCreateMark.setOnClickListener(this);
        btnMarkSheet = (FloatingActionButton) findViewById(R.id.btn_mark);
        btnMarkSheet.setOnClickListener(this);
        btnShareSheet = (FloatingActionButton) findViewById(R.id.btn_share);
        btnShareSheet.setOnClickListener(this);
        btnDeleteSheet = (FloatingActionButton) findViewById(R.id.btn_delete);
        btnDeleteSheet.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        sheetId = b.getLong("sheet_id");

        mva = new MarksValueAdapter(this);
        RecyclerView rvMarksValue = (RecyclerView) findViewById(R.id.rv_marks_value);
        rvMarksValue.setHasFixedSize(true);
        rvMarksValue.setLayoutManager(new LinearLayoutManager(this));
        rvMarksValue.setAdapter(mva);

        mda = new MarksDescrAdapter(this);
        RecyclerView rvMarksDescr = (RecyclerView) findViewById(R.id.rv_marks_descr);
        rvMarksDescr.setHasFixedSize(true);
        rvMarksDescr.setLayoutManager(new LinearLayoutManager(this));
        rvMarksDescr.setAdapter(mda);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public void refreshData(){
        pd = ProgressDialog.show(this,"",getString(R.string.msg_loading),true);
        App.getApi().getSheet(this.sheetId).enqueue(this);
    }

    @Override
    public void onResponse(Call<Sheet> call, Response<Sheet> response) {
        pd.dismiss();
        sheet = response.body();
        if( sheet==null ) sheet = new Sheet();

        sheetName.setText( sheet.getName() );
        sheetMark.setText( Integer.toString(sheet.getMark()) );
        // is owner ?
        if( sheet.getAuthor().getId() != App.getUser().getId() ) {
            btnDeleteSheet.setVisibility(View.GONE);
        } else {
            btnDeleteSheet.setVisibility(View.VISIBLE);
        }
        // is known ?
        Mark m = sheet.getUsersMark(App.getUser().getId());
        if( m==null ) {
            btnShareSheet.setVisibility(View.GONE);
            btnMarkSheet.setVisibility(View.VISIBLE);
        } else {
            btnShareSheet.setVisibility(View.VISIBLE);
            btnMarkSheet.setVisibility(View.GONE);
        }

        mva.refreshData(sheet);
        mda.refreshData(sheet);
    }

    @Override
    public void onFailure(Call<Sheet> call, Throwable t) {
        pd.dismiss();
        Log.e("API.GetSheet", call.toString(), t);
        App.Message(this, getString(R.string.err_get_sheet) );
    }

    public final int PICK_CONTACT = 2015;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.btn_new: // add new mark
                intent = new Intent(this, CreateMarkActivity.class);
                intent.putExtra("sheet_id", sheetId);
                Mark m = sheet.getUsersMark( App.getUser().getId() );
                if( m!= null ) {
                    intent.putExtra("mark_value", m.getValue());
                    intent.putExtra("mark_descr", m.getDescr());
                } else {
                    intent.putExtra("mark_value", 0);
                    intent.putExtra("mark_descr", "?");
                }
                startActivity(intent);
                break;

            case R.id.btn_mark:
                final DetailActivity self = this;
                pd = ProgressDialog.show(this,"",getString(R.string.msg_loading),true);
                App.getApi().saveMark(sheetId, 0, "?").enqueue(new Callback<Mark>() {
                    @Override
                    public void onResponse(Call<Mark> call, Response<Mark> response) {
                        self.pd.dismiss();
                        App.Message(self, getString(R.string.ok) );
                        self.refreshData();

                    }
                    @Override
                    public void onFailure(Call<Mark> call, Throwable t) {
                        self.pd.dismiss();
                        Log.e("API.SaveMark", call.toString(), t);
                        App.Message(self, getString(R.string.err_save_mark) );
                    }
                });
                break;

            case R.id.btn_delete:
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage( getString(R.string.msg_delete_sheet) )
                        .setPositiveButton( getString(R.string.yes), this)
                        .setNegativeButton( getString(R.string.no), this)
                        .show();
                break;

            case R.id.btn_share:
                try {
                    intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        final Activity context = this;
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                pd = ProgressDialog.show(this,"",getString(R.string.msg_loading),true);
                App.getApi().deleteSheet( this.sheetId ).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        pd.dismiss();
                        context.finish();
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        pd.dismiss();
                        Log.e("API.SharingMark", call.toString(), t);
                        App.Message(context, getString(R.string.err_delete_sheet) );
                    }
                });
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK ){
            return;
        }
        switch (requestCode) {
            case( PICK_CONTACT ):
                pd = ProgressDialog.show(this,"",getString(R.string.msg_loading),true);
                ContentResolver cr = getContentResolver();
                String[] projection = {Contacts._ID, Contacts.HAS_PHONE_NUMBER};
                Cursor contacts = cr.query(data.getData(), projection, null, null, null);
                while (contacts.moveToNext()) {
                    if (contacts.getInt(contacts.getColumnIndex(Contacts.HAS_PHONE_NUMBER)) == 0) {
                        continue;
                    }
                    String contactId = contacts.getString(contacts.getColumnIndex(Contacts._ID));
                    //  Get all phone numbers.
                    Cursor phones = cr.query(Phone.CONTENT_URI, null,
                            Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        String number = phones.getString(phones.getColumnIndex(Phone.NORMALIZED_NUMBER));
                        if (number == null) {
                            number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
                        }
                        final ContextWrapper context = this;
                        App.getApi().sharingMark(sheetId, number).enqueue(new Callback<Mark>() {
                            @Override
                            public void onResponse(Call<Mark> call, Response<Mark> response) {
                                App.Message(context, getString(R.string.ok) );
                            }
                            @Override
                            public void onFailure(Call<Mark> call, Throwable t) {
                                Log.e("API.SharingMark", call.toString(), t);
                                App.Message(context, getString(R.string.err_save_mark) );
                            }
                        });
                    }
                    phones.close();
                }
                contacts.close();
                pd.dismiss();
                break;
        }
    }

    public void onMarkClick(Mark item) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        List<CharSequence> items = new ArrayList<CharSequence>();
        for( Mark m : this.sheet.marks){
            if( m.getValue() == 0 ) continue;
            if( m.getAuthor().getId() == item.getAuthor().getId() ) {
                StringBuilder sb = new StringBuilder();
                if( m.getDeleted()==null ) {
                    sb.append("по наст.время");
                } else {
                    sb.append(dateFormat.format(m.getDeleted()));
                }
                sb.append(" [");
                sb.append( m.getValue() );
                sb.append("]: ");
                sb.append( m.getDescr() );
                items.add(sb.toString());
            }
        }
        String[] strs = items.toArray(new String[items.size()]);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab
                .setTitle( getString(R.string.label_mark_history) )
                .setItems( strs, null)
                .show();
        return;
    }
}
