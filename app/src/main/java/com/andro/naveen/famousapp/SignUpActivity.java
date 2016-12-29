package com.andro.naveen.famousapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity {

    String REGISTER_URL = "http://localhost/shop/api/register";
    EditText editName, editPhone, editEmail, editPassword, editConfirm;
    Button btnCreate;
    TextView textLogin;
    private static String sName;
    private static String sPhone;
    private static String sEmailId;
    private static String sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

        //On Create Account Clicked
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        //On Already A user Clicked
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {

        if (!validate()){
            onSignupFailed();
            return;
        }
        btnCreate.setEnabled(false);

        sName = editName.getText().toString();
        sPhone = editPhone.getText().toString();
        sEmailId = editEmail.getText().toString();
        sPassword = editPassword.getText().toString();

//On Signup Valid, Insert into database
        SignUpTask signUp = new SignUpTask();
        signUp.execute(sName, sPhone, sEmailId, sPassword);
    }

    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Sign up Successfull !!! Please Login!", Toast.LENGTH_LONG).show();
        btnCreate.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "User with same email id exists !!! Please Login", Toast.LENGTH_LONG).show();
        btnCreate.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String pass2 = editConfirm.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            editEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editPassword.setError("Password Length should be between 4 to 10 alphanumeric characters");
            valid = false;
        } else {
            editPassword.setError(null);
        }

        if (!password.equals(pass2)){
            Toast.makeText(SignUpActivity.this, "Password Don't Match",Toast.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }

    public void init() {

        editName = (EditText) findViewById(R.id.input_name);
        editPhone = (EditText) findViewById(R.id.input_phone);
        editEmail = (EditText) findViewById(R.id.input_email);
        editPassword = (EditText) findViewById(R.id.input_password);
        textLogin = (TextView) findViewById(R.id.link_login);
        btnCreate = (Button) findViewById(R.id.btn_signup);
        editConfirm = (EditText) findViewById(R.id.input_Conformpassword);
    }

    private class SignUpTask extends AsyncTask<String, Void, String> {
        String result = "";
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);

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
                String data = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(params[0], "UTF-8");

                data += "&" + URLEncoder.encode("phone", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");

                data += "&" + URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(params[2], "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(params[3], "UTF-8");

                URL url = new URL(REGISTER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
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
                String msg = jsonObject.getString("msg");
                String response = jsonObject.getString("response");
                if(response.equals("error")){
                    progressDialog.dismiss();
                    onSignupFailed();
                }else{
                    progressDialog.dismiss();
                    onSignupSuccess();
                }
            }catch (JSONException jsonException){
                jsonException.printStackTrace();
            }
        }
    }
}
