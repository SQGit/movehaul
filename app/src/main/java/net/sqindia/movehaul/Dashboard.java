package net.sqindia.movehaul;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.rey.material.widget.Button;

/**
 * Created by sqindia on 25-10-2016.
 */

public class Dashboard extends FragmentActivity{
    Button btn_book_now,btn_book_later;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_content);

        btn_book_now = (Button) findViewById(R.id.btn_book_now);
        btn_book_later = (Button) findViewById(R.id.btn_book_later);

        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this,Book_now.class);
                startActivity(i);
            }
        });
    }
}
