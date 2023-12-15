package com.example.lab11;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText ed_book,ed_price;
    private Button btn_query,btn_insert,btn_update,btn_delete;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_book = findViewById(R.id.ed_book);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw =new MyDBHelper(this).getWritableDatabase();
        //----------------insert-----------
        btn_insert.setOnClickListener(view -> {
            if(ed_book.length() <1 || ed_price.length()<1)
                Toast.makeText(MainActivity.this,"null",Toast.LENGTH_SHORT).show();
            else {
                try{
                    dbrw.execSQL("INSERT INTO myTable(book,price) values(?,?)",
                            new Object[]{ed_book.getText().toString(),ed_price.getText().toString()});
                    Toast.makeText(MainActivity.this,"aad book name" + ed_book.getText().toString() +
                            "price" +ed_price.getText().toString(),Toast.LENGTH_SHORT).show();
                    ed_book.setText("");
                    ed_price.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "add failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //----------------update-----------
        btn_update.setOnClickListener(view -> {
            if(ed_book.length() <1 || ed_price.length()<1)
                Toast.makeText(MainActivity.this,"null",Toast.LENGTH_SHORT).show();
            else {
                try{
                    dbrw.execSQL("UPDATE myTable SET price = "
                            +ed_price.getText().toString()
                            +" WHERE book LIKE '"
                            + ed_book.getText().toString() + "'");
                    Toast.makeText(MainActivity.this,"aad book name" + ed_book.getText().toString() +
                            "price" +ed_price.getText().toString(),Toast.LENGTH_SHORT).show();
                    ed_book.setText("");
                    ed_price.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "update failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //----------------remove-----------
        btn_delete.setOnClickListener(view -> {
            if(ed_book.length() <1 )
                Toast.makeText(MainActivity.this,"null",Toast.LENGTH_SHORT).show();
            else {
                try{
                    dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '" + ed_book.getText().toString()+"'");
                    Toast.makeText(MainActivity.this,"remove book" + ed_book.getText().toString() +
                            ed_book.getText().toString(),Toast.LENGTH_SHORT).show();
                    ed_book.setText("");
                    ed_price.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "remove failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //----------------query-----------
        btn_query.setOnClickListener(view -> {
            Cursor c;
            if(ed_book.length() <1 )
                c = dbrw.rawQuery("SELECT * FROM myTable" , null);
            else
                c = dbrw.rawQuery("SELECT*FROM myTable WHERE book LIKE '" +
                        ed_book.getText().toString()+"'",null);

            c.moveToFirst();
            items.clear();
            Toast.makeText(MainActivity.this,"have"+c.getCount()+"items",Toast.LENGTH_SHORT).show();
            for(int i =0; i<c.getCount();i++) {
                items.add("book:"+ c.getString(0)+"\t\t\t\tPrice:"+ c.getString(1));
                c.moveToNext();
            }
            adapter.notifyDataSetChanged();
            c.close();
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dbrw.close();
    }
}