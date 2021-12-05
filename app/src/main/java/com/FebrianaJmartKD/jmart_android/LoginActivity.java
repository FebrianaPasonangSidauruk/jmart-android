package com.FebrianaJmartKD.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import com.FebrianaJmartKD.jmart_android.model.Account;
import com.FebrianaJmartKD.jmart_android.model.Store;
import com.FebrianaJmartKD.jmart_android.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;
    private TextView tv_registerNow;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tv_registerNow = findViewById(R.id.tv_registerNow);

        etEmail.setText("febrianasidauruk@gmail.com");
        etPassword.setText("Febriana12345-");

        RequestQueue queue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                LoginRequest loginRequest = new LoginRequest(email, password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loggedAccount = gson.fromJson(response, Account.class);
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Login unsuccessful, error occured", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error occured.", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(loginRequest);
            }
        });
        tv_registerNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });
    }
    public void openRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    //Method to reload the currently logged account after it's modified
    public static void reloadLoggedAccount(String response){
        loggedAccount = gson.fromJson(response, Account.class);
    }
    //Method to insert newly created Store data to a logged account;
    public static void insertLoggedAccountStore(String response){
        Store newStore = gson.fromJson(response, Store.class);
        loggedAccount.store = newStore;
    }
}