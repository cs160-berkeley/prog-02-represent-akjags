package com.example.akshayjagadeesh.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();

        ImageButton obama = (ImageButton) findViewById(R.id.imageButton);
        if (obama != null) {
            obama.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("on click");
                    Intent intent1 = new Intent(getBaseContext(), ElectionActivity.class);
                    startActivity(intent1);
                }
            });
        }
        else{System.out.println("obama is nooll");}

        TextView c1 = (TextView) findViewById(R.id.c1);
        String rep = intent.getStringExtra("REP_NAME");
        if (c1 != null) {
            c1.setText(rep);
            System.out.println("wow not null");
        }
        else {
            System.out.println("c1 is null too");
        }

    }


}
