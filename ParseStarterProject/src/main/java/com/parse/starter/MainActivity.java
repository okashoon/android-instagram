/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText password;
    Button loginButton;
    LinearLayout layout;
    ImageView logo;

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            if (loginButton.getText().equals("Login")) {
                loginUser();
            } else if (loginButton.getText().equals("Signup")) {
                signupNewUser();
            }
        } else if (v == logo || v == layout){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public class KeyListener implements EditText.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            Log.i("aaa", "keycode = " + keyCode);
            Log.i("aaaa", "enter keycode = " + KeyEvent.KEYCODE_ENTER);

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                if (loginButton.getText().equals("Login")) {
                    loginUser();
                } else if (loginButton.getText().equals("Signup")) {
                    signupNewUser();
                }
                return true;

            }
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        username = (EditText) findViewById(R.id.userName_edit);
        username.setOnKeyListener(new KeyListener());
        password = (EditText) findViewById(R.id.password_edit);
        password.setOnKeyListener(new KeyListener());

        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(this);

        layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(this);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }


    }

    public void showUserList(){
        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);
    }

    public void loginUser() {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                    showUserList();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signupNewUser() {

        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                    showUserList();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void switchLoginSignup(View view) {
        if (loginButton.getText().equals("Login")) {
            loginButton.setText("Signup");
        } else if (loginButton.getText().equals("Signup")) {
            loginButton.setText("Login");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
