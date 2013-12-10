package com.muhardin.endy.pictag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by endy on 12/10/13.
 */
public class MainScreenActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Intent fromPrevPage = getIntent();
        String username = fromPrevPage.getStringExtra("username");

        EditText txtUsernameOutput = (EditText) findViewById(R.id.txtUsernameOutput);
        txtUsernameOutput.setText(username, TextView.BufferType.EDITABLE);
    }
}