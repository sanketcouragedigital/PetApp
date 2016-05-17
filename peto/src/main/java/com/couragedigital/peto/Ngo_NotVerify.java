package com.couragedigital.peto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Ngo_NotVerify extends AppCompatActivity implements View.OnClickListener {

    private static Button messageOkBtn;
    private static TextView notVerifyEroorLabel;
    private static View messageDividerLine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ngo_notverify);

        messageOkBtn = (Button) findViewById(R.id.messageOkBtn);
        messageDividerLine = findViewById(R.id.messageDividerLine);
        messageDividerLine.setBackgroundResource(R.color.list_internal_divider);
        messageOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.messageOkBtn) {
            //this.finish();
            finish();
            System.exit(0);
        }
    }
}

