package com.emusafir.userauth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emusafir.MainActivity;
import com.emusafir.R;
import com.emusafir.utility.App;
import com.emusafir.utility.Constant;
import com.emusafir.utility.MyProgressDialog;
import com.emusafir.utility.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class SocialLoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private MyProgressDialog myProgressDialog;
    Context mcontext;
    private static final String TAG = SocialLoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private static final int FB_SIGN_IN = 0;
    private GoogleSignInClient mSignInClient;
    //    private ProgressDialog mProgressDialog;
    private Button btnSignIn, btnLoginFb;
    private TextView tvLogIn, tvSignUp;
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private String name, birthday, address, imageUrl;
    private Bitmap thumbnail;
    private TextView tvTandC, tvSkip;

    //    public static String printKeyHash(Activity context) {
//        PackageInfo packageInfo;
//        String key = null;
//        try {
//            //getting application package name, as defined in manifest
//            String packageName = context.getApplicationContext().getPackageName();
//            //Retriving package info
//            packageInfo = context.getPackageManager().getPackageInfo(packageName,
//                    PackageManager.GET_SIGNATURES);
//
//            Log.e("Package Name=", context.getApplicationContext().getPackageName());
//
//            for (android.content.pm.Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = new String(Base64.encode(md.digest(), 0));
//
//                // String key = new String(Base64.encodeBytes(md.digest()));
//                Log.e("Key Hash=", key);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("Name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("No such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("Exception", e.toString());
//        }
//
//        return key;
//    }
    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.transParentStatusBar(this);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_social);
//        Log.e(TAG, printKeyHash(this));
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Constant.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                }
            }
        };
        displayFirebaseRegId();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register FCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));
    }

    private void displayFirebaseRegId() {
        String regId = App.getInstance().getPrefManager().getFcmRegistrationId();
        Log.e(TAG, "TAG    " + regId);

    }

    private void init() {
        mcontext = this;
        tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(this);
        tvTandC = findViewById(R.id.tvTandC);
        tvTandC.setMovementMethod(LinkMovementMethod.getInstance());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvTandC.setText(Html.fromHtml(getString(R.string.by_sign_up_you_agree), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTandC.setText(Html.fromHtml(getString(R.string.by_sign_up_you_agree)));
        }
        btnLoginFb = findViewById(R.id.btnLoginFb);
        btnLoginFb.setOnClickListener(this);

        initFacebookLogin();

        tvLogIn = findViewById(R.id.tvLogIn);
        tvLogIn.setOnClickListener(this);

        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);
        // Customizing G+ button
        /*btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
*/

    }

    private void initFacebookLogin() {
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
//        fbLoginButton.setReadPermissions(Arrays.asList(
//                "public_profile", "email", "user_birthday", "user_friends"));
        fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        Log.e("bFacebookData", bFacebookData.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(final FacebookException exception) {
                // App code
                exception.printStackTrace();
                Log.e(TAG, exception.toString() + "");
//                Toast.makeText(
//                        ChooseLoginActivity.this,
//                        R.string.error,
//                        Toast.LENGTH_LONG).show();
            }
        });
//        loginButton.setReadPermissions(Arrays.asList("public_profile,email,user_birthday"));
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.e(TAG, "onSuccess(): " + loginResult);
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @SuppressLint("LongLogTag")
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
////                                {Response:  responseCode: 200, graphObject: {"id":"184815435695123",
//// "name":"James Edition","email":"vivek.mishra@ripplesinfomatics.com","birthday":"03\/21\/1993"}, error: null}
//                                Log.e("Vix.error", response.toString());
//                                try {
//                                    Log.e(TAG, "FACBOOK LOGIN SUCCESS" + response.toString());
//                                    String userId = "", email = "", name = "", pic = "";
//                                    // getdatafromfacebuk();
//                                    if (object.has("id")) {
//                                        // userId = Util.validateString(object.getString("id"));
//                                    }
//                                    if (object.has("email")) {
//
//                                        // userId = Util.validateString(object.getString("email"));
//                                        // fbEmail1 = userId;
//
//                                        Log.e("007fbemail", userId);
//                                    }
//                                    if (object.has("first_name") && object.has("last_name")) {
//                                        //  name = (Util.validateString(object.getString("first_name")) + " " + Util.validateString(object.getString("last_name"))).trim();
//                                    }
//                                    if (!userId.equals("")) {
//                                        pic = "http://graph.facebook.com/" + userId + "/picture?type=large";
//                                        Log.e(TAG, pic);
//                                    }
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Log.e(TAG, "onCancel()");
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                Log.e(TAG, "onError(): " + e);
//            }
//        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvSkip:
                startActivity(new Intent(SocialLoginActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.tvLogIn:
                startActivity(new Intent(SocialLoginActivity.this, LoginActivity.class));
                break;
            case R.id.tvSignUp:
                startActivity(new Intent(SocialLoginActivity.this, SignUpActivity.class));
                break;

            case R.id.btn_sign_in:
//                if (mGoogleApiClient.isConnected())
//                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                signIn();
                break;
            case R.id.btnLoginFb:
                fbLoginButton.performClick();
                break;


        }


    }

    private void signIn() {
        Intent intent = mSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            try {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();

                Log.e(TAG, "display name: " + acct.getDisplayName());
                String personPhotoUrl;
                if (acct.getDisplayName() != null) {
                    String personName = acct.getDisplayName();
                    Log.e(TAG, "Name: " + personName);
                }
                if (acct.getPhotoUrl() != null) {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                    Log.e(TAG, ", Image: " + personPhotoUrl);
                }
                if (acct.getEmail() != null) {
                    String email = acct.getEmail();
                    Log.e(TAG, ", email: " + email);
                }


       /*     txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

                // updateUI(true);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "search"
//            // and the GoogleSignInResult will be available instantly.
//            Log.e(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {

        if (myProgressDialog == null) {
            myProgressDialog = new MyProgressDialog(this,R.drawable.two_dots_loader);
        }
        if (myProgressDialog != null) {
            myProgressDialog.show();
        }


    }

    private void hideProgressDialog() {

        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            startActivity(new Intent(SocialLoginActivity.this, MainActivity.class));

        } else {

        }
    }


    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.e("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
                imageUrl = profile_pic.toString();
                new MyAsync().execute(imageUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                name = object.getString("first_name");
                Log.e("first_name", object.getString("first_name") + "");
                bundle.putString("first_name", object.getString("first_name"));
            }
            if (object.has("last_name")) {
                name = name + " " + object.getString("last_name");
                bundle.putString("last_name", object.getString("last_name"));
                Log.e("last_name", object.getString("last_name") + "");
            }
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
                Log.e("email", object.getString("email") + "");
            }
//            if (object.has("gender")) {
//                if (object.getString("gender").equalsIgnoreCase("male"))
//                    gender = "1";
//                else
//                    gender = "2";
//                bundle.putString("gender", object.getString("gender"));
//                Log.e("gender", object.getString("gender") + "");
//            }
            if (object.has("birthday")) {
                birthday = object.getString("birthday");
                bundle.putString("birthday", object.getString("birthday"));
                Log.e("birthday", object.getString("birthday") + "");
            }
            if (object.has("location")) {
                address = object.getJSONObject("location").getString("name");
                bundle.putString("location", object.getJSONObject("location").getString("name"));
                Log.e("location", object.getJSONObject("location").getString("name"));
            }

            return bundle;
        } catch (JSONException e) {
            Log.e("Error", "Error parsing JSON");
        }
        return null;
    }

    public class MyAsync extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            thumbnail = bitmap;
        }
    }

}





