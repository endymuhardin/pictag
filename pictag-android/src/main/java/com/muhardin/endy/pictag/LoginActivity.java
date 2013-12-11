package com.muhardin.endy.pictag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.muhardin.endy.pictag.dto.LoginRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import training.pictag.dto.PictagServerResponse;

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

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

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
                Thread.sleep(5 * 1000); // sleep 5 seconds

                String serverReply = "{\"success\": false, \"message\": \"Wrong username/password\"}";

                ObjectMapper mapper = new ObjectMapper();
                PictagServerResponse result = mapper.readValue(serverReply, PictagServerResponse.class);
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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