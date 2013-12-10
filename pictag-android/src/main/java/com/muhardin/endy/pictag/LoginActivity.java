package com.muhardin.endy.pictag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by endy on 12/10/13.
 */
public class LoginActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void processLogin(View loginButton){
        EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        Log.v(this.getClass().getName(), "Username : "+username);
        Log.v(this.getClass().getName(), "Password : "+password);

        Intent nextPage = new Intent(this, MainScreenActivity.class);
        nextPage.putExtra("username", username);
        startActivity(nextPage);
    }
}