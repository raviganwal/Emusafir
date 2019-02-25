package com.emusafir.utility;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.TimePicker;
import android.widget.Toast;

import com.emusafir.fragments.DatePickerFragment;
import com.emusafir.model.buslayoutmodel.SeatModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import static com.emusafir.utility.Constant.MIN_DATE;
import static com.emusafir.utility.Constant.WHICH_BUTTON;
import static com.emusafir.utility.Constant.dd_MMM_yy;
import static com.emusafir.utility.Constant.yyyy_MM_dd;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static Context context;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static int mHour = 0;
    private static int mMinute = 0;
    public static String mdate = null;
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");
    private final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");

    public static void openDatePickerDialog(AppCompatActivity activity, String whichButton, String minDate) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WHICH_BUTTON, whichButton);
        bundle.putString(MIN_DATE, minDate);
        newFragment.show(activity.getSupportFragmentManager(), "date picker");
        newFragment.setArguments(bundle);
    }

    public static String dateFormat_yyyy_MM_dd(String str_dd_MMM_yy) {
        Date date = null;
        try {
            DateFormat inputFormat = new SimpleDateFormat(dd_MMM_yy);
            DateFormat outputFormat = new SimpleDateFormat(yyyy_MM_dd);
            date = inputFormat.parse(str_dd_MMM_yy);
            String outputDateStr = outputFormat.format(date);
            return outputDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SeatModel> uniqueList(List<SeatModel> mList, SeatModel model) {
        if (mList.size() == 0) {
            mList.add(model);
            return mList;
        }
        for (int i = 0; i < mList.size(); i++) {
            if (!doesSeatExist(mList, model)) {
                mList.add(model);
            }
        }
        return mList;
    }

    public static boolean doesSeatExist(List<SeatModel> mList, SeatModel model) {
        for (SeatModel seat : mList) {
            if (seat.getSeatNo().equalsIgnoreCase(model.getSeatNo()))
                return true;
        }
        return false;
    }

    public static String dateFormat_dd_MMM_yy(String str_yyyy_MM_dd) {
        Date date = null;
        try {
            DateFormat inputFormat = new SimpleDateFormat(yyyy_MM_dd);
            DateFormat outputFormat = new SimpleDateFormat(dd_MMM_yy);
            date = inputFormat.parse(str_yyyy_MM_dd);
            String outputDateStr = outputFormat.format(date);
            return outputDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dd_MMM_yy, Locale.US);
        return sdf.format(c.getTime());
    }

    public static void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, .0f);
        fadeOut.setDuration(300);
        fadeOut.setRepeatCount(0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .0f, 1f);
        fadeIn.setDuration(300);
        fadeIn.setRepeatCount(0);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

//        mAnimationSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mAnimationSet.start();
//            }
//        });
        mAnimationSet.start();
    }

    public static void transParentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = activity.getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private static Random random = new Random();

    public static TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 3, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(5));
        return shake;
    }

    public static int generateRandomPositive() {
        return Math.abs(random.nextInt());
    }


    public static boolean isPasswordValidator(String password) {
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showMessage(String message, Activity activity) {
        hideKeyboard(activity);
        if (message != null && activity != null) {

            int version = Build.VERSION.SDK_INT;
            if (version <= Build.VERSION_CODES.LOLLIPOP) {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            } else {
                themeSnackBar(activity, message);
            }
        }
    }

    public final static boolean isValidData(String str) {
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("ResourceAsColor")
    public static void themeSnackBar(Activity activity, String message) {
        hideKeyboard(activity);
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#4B4A4C"));
        snackbar.show();


    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        // String imageEncoded = Base64Coder.encodeTobase64(image);

        // Log.d("LOOK", imageEncoded);
        return imageEncoded;
    }





  /*  public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

    /* public static boolean isValidName(String firstName){
         if (firstName.matches("^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+$" ))
             return true;
         else
             return false;
  }*/
    public static String validateString(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    public static boolean isValidName(String firstName) {
        if (firstName.matches("^[A-Z]+[\\-'\\s]?[a-zA-Z ]+$"))
            return true;
        else
            return false;
    }

    public static boolean isvalidProfession(String profession) {
        if (profession.matches("^[A-Z]+[\\-'\\s]?[a-zA-Z]+$"))
            return true;
        else
            return false;
    }

    public static String timeFormat24hrs(long timeInMillis) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm");
        return dateFormat.format(cal1.getTime());
    }

    public static String travelTime(long timeInMilliSeconds) {
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = null;
        if (minutes > 0)
            time = minutes % 60 + " m";
        if (hours > 0)
            time = hours % 24 + " h " + minutes % 60 + " m";
        if (days > 0)
            time = days + " d " + hours % 24 + " h " + minutes % 60 + " m ";

        return time;
    }

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        Log.d("now", String.valueOf(now));
        if (time > now || time <= 0) {
            return "";
        }

        // TODO: localize
        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;  //Default subsampling size
        // See if image raw height and width is bigger than that of required view
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            //bigger
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public static void isPickTime(Context ctx) {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mdate = hourOfDay + ":" + minute;


                        //sun_time.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }
   /* public static boolean isPasswordValidator(String password) {
        String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
        //  String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    }*/


}