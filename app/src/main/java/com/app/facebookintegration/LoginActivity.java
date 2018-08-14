package com.app.facebookintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(mCallbackManager, callback2);
    }

    /*Facebook Integration*/

    FacebookCallback<LoginResult> callback2 = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {

            Log.e("callback2", "onSuccess");

//            LoginManager.getInstance().logInWithReadPermissions(
//                    CustomerSignInActivity.this, Arrays.asList("public_profile", "email"));
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            Log.e("GraphRequest", "request");
                            Log.e("LoginActivity", response.toString());
                            String id;
                            String email;
                            String name, FNAME, LNAME;
                            try {
                                AppSharedPref.getInstance(LoginActivity.this).setSocialMediaLogedIn(1);
                                email = object.getString("email");
                                id = object.getString("id");
                                name = object.getString("name");
                                if (!name.equals("")) {
                                    if (name.length() > 0) {
                                        if (name.contains(" ")) {
                                            String nameFinal[] = name.split(" ");
                                            FNAME = nameFinal[0];
                                            LNAME = nameFinal[1];

                                            AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_NAME, FNAME + " " + LNAME);
                                            //AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_IMAGE, urlFBProfilePic);
                                          /*  strFBFirstName = FNAME;
                                            strFBLastName = LNAME;
                                            //social = id;
                                            strEmail = email;
                                            try {
                                                urlFBProfilePic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }*/
                                            try {
                                                URL urlFBProfilePic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                                AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_IMAGE, urlFBProfilePic.toString());
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }

                                        } else {

                                            /*strFBFirstName = name;
                                            strFBLastName = name;
                                            //SOCIAL_ID = id;
                                            // SOCIAL_SITE = "Facebook";
                                            strEmail = email;*/

                                            try {
                                                URL urlFBProfilePic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                                AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_IMAGE, urlFBProfilePic.toString());
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }
                                            AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_NAME, name);
                                        }
                                    }

                                } else {

                                   /* strFBFirstName = name;
                                    strFBLastName = name;
                                    //SOCIAL_ID = id;
                                    //SOCIAL_SITE = "Facebook";
                                    strEmail = email;*/

                                    try {
                                        URL urlFBProfilePic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                        AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_IMAGE, urlFBProfilePic.toString());
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    AppSharedPref.getInstance(LoginActivity.this).setDataString(Constants.FACEBOOK_PROFILE_NAME, name);

                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        mCallbackManager.onActivityResult(requestCode, responseCode, intent);
    }
}
