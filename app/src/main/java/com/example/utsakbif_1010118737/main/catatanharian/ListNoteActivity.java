package com.example.utsakbif_1010118737.main.catatanharian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.utsakbif_1010118737.R;
import com.example.utsakbif_1010118737.base.DBHelper;
import com.example.utsakbif_1010118737.main.dashboard.DashboardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//05 June 2021, 10118737, Moch Rifqi Ramdhani, IF10
public class ListNoteActivity extends AppCompatActivity {

    String[] daftar, daftar2, daftar3, daftar4, daftar5, id;
    ListView listView;
    int kategori;
    private Cursor cursor;
    DBHelper dbHelper;
    public static ListNoteActivity ln;

    //Date dt = new Date();
    //SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy");
    //SimpleDateFormat time = new SimpleDateFormat("HH:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.CatatanHarian);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ProfileId:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.CatatanHarian:
                        startActivity(new Intent(getApplicationContext(), KategoriActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.About:
                        final Dialog dialog = new Dialog(ListNoteActivity.this);

                        //Memasang Title / Judul pada Custom Dialog
                        dialog.setTitle("About");

                        //Memasang Desain Layout untuk Custom Dialog
                        dialog.setContentView(R.layout.custom_design_dialog);

                        //Memasang Listener / Aksi saat tombol OK di Klik
                        Button DialogButton = dialog.findViewById(R.id.ok);
                        DialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        return true;
                }

                return false;
            }
        });

        Intent getKategori = getIntent();
        kategori = getKategori.getIntExtra("kategori", 0);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNoteActivity.this, NotesActivity.class);
                intent.putExtra("kategori", kategori);
                startActivity(intent);
            }
        });

        ln = this;
        dbHelper = new DBHelper(this);
        RefreshListNote();
    }

    public void RefreshListNote() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM note WHERE id_kategori = "+kategori, null);
        id = new String[cursor.getCount()];
        daftar = new String[cursor.getCount()];
        daftar2 = new String[cursor.getCount()];
        daftar3 = new String[cursor.getCount()];
        daftar4 = new String[cursor.getCount()];
        daftar5 = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            id[cc] = cursor.getString(0).toString();
            daftar[cc] = cursor.getString(1).toString();
            daftar2[cc] = cursor.getString(2).toString();
            daftar3[cc] = cursor.getString(3).toString();
            daftar4[cc] = cursor.getString(4).toString();
            daftar5[cc] = cursor.getString(5).toString();
        }

        listView = findViewById(R.id.listNote);

        ListNoteActivity.MyAdapter adapter = new ListNoteActivity.MyAdapter();
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final int selection = Integer.parseInt(id[arg2]);
                Intent in = new Intent(getApplicationContext(), UpdateActivity.class);
                in.putExtra("id", selection);
                startActivity(in);
            }});

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id2) {
                final int which_item = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(ListNoteActivity.this);
                builder.setTitle("Delete Note");
                builder.setMessage("Yakin menghapus note ini?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("delete from note where id ="+ id[position]);
                        RefreshListNote();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
                return true;
            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_notes, parent, false);

            TextView noteTitle = convertView.findViewById(R.id.text_title);
            TextView noteDesc = convertView.findViewById(R.id.text_content);
            TextView noteDate = convertView.findViewById(R.id.text_date);
            TextView noteTime = convertView.findViewById(R.id.text_time);

            noteDate.setText(daftar[position]);
            noteTime.setText(daftar2[position]);
            noteTitle.setText(daftar3[position]);
            noteDesc.setText(daftar4[position]);

            return convertView;
        }
    }
}