package com.andro.naveen.famousapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.naveen.famousapp.myapplication.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://localhost/shop/api/login";
    EditText editEmail, editPassword;
    TextView textSignup;
    Button btnLogin;
    private static final int REQUEST_SIGNUP = 0;
    MyApplication myApplication;

    private static String sEmailId;
    private static String sPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//Get all the views from the  activity_login.xml --> done under init function
        init();


        //On Login Click
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Goto login() function
                login();
            }
        });

        //If signup button is clicked, perform following
        textSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Start SignUp Activity
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(i, REQUEST_SIGNUP);
            }
        });
    }


    //once signup is completed, the result comes here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        //after signup, presently, after 5s delay subik() is called.
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }, 0);
            }
        }
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


    public void login() {

        if (!validate()) {
            //if the entered details are not valid, goto the following function
            onLoginFailed();
            return;
        }
        //else if the endered data is valid, perform following
        btnLogin.setEnabled(false);

        sEmailId = editEmail.getText().toString();
        sPassword = editPassword.getText().toString();

        LoginAsyncTask attempLogin = new LoginAsyncTask();
        attempLogin.execute(sEmailId, sPassword);
    }

    private void init() {
        editEmail = (EditText) findViewById(R.id.input_email);
        editPassword = (EditText) findViewById(R.id.input_password);
        textSignup = (TextView) findViewById(R.id.link_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        myApplication = (MyApplication) getApplication();
    }

    public boolean validate() {

        // This function is to check whether the entered email address and password are in the prescribed format or not

        boolean valid = false;

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty()) {
            editEmail.setError("Enter a email address");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email address");
        } else if (password.isEmpty()) {
            editPassword.setError("Please Enter Password");
        } else if(password.length() < 4 || password.length() > 10) {
            editPassword.setError("The Password should have 4 to 10 alphanumeric characters");
        } else {
            editPassword.setError(null);
            editEmail.setError(null);
            valid = true;
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
        String result = "";
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);

        @Override
        protected  void onPreExecute()
        {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String ... params){
            try{
                BufferedReader reader=null;
                String data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(params[0], "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");

                URL url = new URL(LOGIN_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                result = sb.toString();

            }catch (MalformedURLException m){
                m.printStackTrace();
            }catch (IOException io){
                io.printStackTrace();
            }

            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
                if(response.equals("error")){
                    //redirect user to login page
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email or Password din't match!!! Please Try again !", Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                }else{
                    progressDialog.dismiss();
                    btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Login Successful!!!", Toast.LENGTH_LONG).show();
                    JSONObject data = jsonObject.getJSONObject("data");
                    String email = data.getString("email");
                    String name = data.getString("name");
                    String id = data.getString("id");

                    Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                    myApplication.saveToken("EmailS",email);
                    myApplication.saveToken("NameS",name);
                    myApplication.saveToken("Id", id);
                    startActivity(intent);
                }
            }catch (JSONException jsonException){
                jsonException.printStackTrace();
            }
        }
    }
}