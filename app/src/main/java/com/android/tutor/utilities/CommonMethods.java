package com.android.tutor.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gufran on 10/20/2017.
 */

public class CommonMethods {

    private static Pattern passwordPattern,emailPattern;
    private static Matcher matcher;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        boolean isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        if (!isConnected) {
            CustomDialogs.getInternetAlertDialog(context, "Please check your internet connection or try again later.", "");
        }

        return isConnected;
    }


    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public static boolean validatePassword(final String password) {
        passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = passwordPattern.matcher(password);
        return matcher.matches();

    }

    /**
     * Validate password with regular expression
     *
     * @param email eamil for validation
     * @return true valid password, false invalid password
     */
    public static boolean validateEmail(final String email) {
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        matcher = emailPattern.matcher(email);
        return matcher.matches();

    }

    public static void requestFieldFocus(EditText editText) {
        editText.requestFocus();
    }

    public static Bitmap convertStringTOBitmap(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    public static String convertBitmapToString(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }
}
