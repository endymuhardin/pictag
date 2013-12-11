package com.muhardin.endy.pictag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.muhardin.endy.pictag.dto.LoginRequest;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import training.pictag.dto.PictagServerResponse;

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

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        // before even connecting, check internet access first
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast toast = Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }

        new CheckUsernamePasswordToServer().execute(request);
    }

    private class CheckUsernamePasswordToServer extends AsyncTask<LoginRequest, Void, PictagServerResponse>{

        private ProgressDialog progressDialog;

        public CheckUsernamePasswordToServer(){
            progressDialog = new ProgressDialog(LoginActivity.this);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Checking username and password");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected PictagServerResponse doInBackground(LoginRequest... loginRequests) {
            try {
                // connection configuration
                String serverUrl = "http://192.168.1.148:8080/pictag-web/user/login";
                URL url = new URL(serverUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                // connection parameters
                LoginRequest login = loginRequests[0];
                StringBuilder queryParam = new StringBuilder();
                queryParam.append("username=");
                queryParam.append(login.getUsername());
                queryParam.append("&password=");
                queryParam.append(login.getPassword());
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(queryParam.toString());
                writer.close();

                // connect and receive reply
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder serverReply = new StringBuilder();
                String data;
                while ((data = reader.readLine()) != null) {
                    serverReply.append(data);
                }
                Log.d(LoginActivity.class.getName(), serverReply.toString());

                // convert reply to object
                ObjectMapper mapper = new ObjectMapper();
                PictagServerResponse result = mapper.readValue(serverReply.toString(), PictagServerResponse.class);
                return result;
            } catch (Exception e) {
                Log.d(LoginActivity.class.getName(), e.getMessage(), e);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                dialogBuilder.setTitle("Network Communication Failure").setMessage(e.getMessage());
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PictagServerResponse response) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(response.getSuccess()){
                Intent nextPage = new Intent(LoginActivity.this, MainScreenActivity.class);
                nextPage.putExtra("username", response.getMessage());
                startActivity(nextPage);
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                dialogBuilder.setTitle("Login Failure").setMessage(response.getMessage());
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        }
    }
}