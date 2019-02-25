package com.emusafir.userauth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.emusafir.MainActivity;
import com.emusafir.R;
import com.emusafir.utility.App;
import com.emusafir.utility.ConnectivityReceiver;
import com.emusafir.utility.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = SplashActivity.class.getSimpleName();
//    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
//    private static final int REQUEST_PERMISSION_SETTING = 101;
//    String[] permissionsRequired = new String[]{Manifest.permission.READ_SMS,
//            Manifest.permission.RECEIVE_SMS};
//    private SharedPreferences permissionStatus;
//    private boolean sentToSettings = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private MyProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utils.transParentStatusBar(this);
//        mDialog = new MyProgressDialog(this);
//        mDialog.show();
//        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        checkConnection();
    }


    class MyTask2 extends Thread {
        @Override
        public void run() {

            super.run();
            try {
                sleep(
                        1000);
//                if (App.getInstance().getPrefManager().isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
//                }
//                else{
//                    startActivity(new Intent(SplashActivity.this, SocialLoginActivity.class));
//                    finish();
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        App.getInstance().setConnectivityListener(this);

    }

    private void displayFirebaseRegId() {
        String regId = App.getInstance().getPrefManager().getFcmRegistrationId();
        Log.e(TAG, "TAG    " + regId);

        proceedAfterPermission();

    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = getString(R.string.connected_to_internet);
            color = Color.WHITE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create channel to show notifications.
                String channelId = getString(R.string.default_notification_channel_id);
                String channelName = getString(R.string.default_notification_channel_name);
                NotificationManager notificationManager =
                        getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_LOW));
            }

            // If a notification message is tapped, any data accompanying the notification
            // message is available in the intent extras. In this sample the launcher
            // intent is fired when the notification is tapped, so any accompanying data would
            // be handled here. If you want a different intent fired, set the click_action
            // field of the notification message to the desired intent. The launcher intent
            // is used when no click_action is specified.
            //
            // Handle possible data accompanying notification message.
            // [START handle_data_extras]
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    Object value = getIntent().getExtras().get(key);
                    Log.d(TAG, "Key: " + key + " Value: " + value);
                }
            }
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, msg);
//                            Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                            App.getInstance().getPrefManager().setFcmRegistrationId(token);
                            displayFirebaseRegId();
                        }
                    });


        } else {


            message = getString(R.string.not_connected_to_internet);
            color = Color.RED;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            finish();
//            Snackbar snackbar = Snackbar
//                    .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(color);
//            snackbar.show();
        }


    }

//    private void permissions() {
//
//        if (ActivityCompat.checkSelfPermission(SplashActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(SplashActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
//                ) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissionsRequired[0])
//                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissionsRequired[1])
//                    ) {
//                //Show Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//                builder.setTitle("Need Permission");
//                builder.setMessage("This app needs receive sms permission.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
//                //Previously Permission Request was cancelled with 'Dont Ask Again',
//                // Redirect to Settings after showing Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//                builder.setTitle("Need Permission");
//                builder.setMessage("This app needs receive sms permission.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        sentToSettings = true;
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant needs sms and location", Toast.LENGTH_LONG).show();
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else {
//                //just request the permission
//                ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//            }
//
//
//            SharedPreferences.Editor editor = permissionStatus.edit();
//            editor.putBoolean(permissionsRequired[0], true);
//            editor.commit();
//        } else {
//            //You already have the permission, just go ahead.
//            proceedAfterPermission();
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
//            //check if all permissions are granted
//            boolean allgranted = false;
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    allgranted = true;
//                } else {
//                    allgranted = false;
//                    break;
//                }
//            }
//
//            if (allgranted) {
//                proceedAfterPermission();
//            } else if (
//
//                    ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissionsRequired[0])
//                            || ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissionsRequired[1])
//
//                    ) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//                builder.setTitle("Need Permission");
//                builder.setMessage("This app needs receive sms permission.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else {
//                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PERMISSION_SETTING) {
//            if (ActivityCompat.checkSelfPermission(SplashActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }

    private void proceedAfterPermission() {
        MyTask2 th = new MyTask2();
        th.start();
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (sentToSettings) {
//            if (ActivityCompat.checkSelfPermission(SplashActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
