package activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SaveSharedPreferences {
    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("userUid", userName);
        editor.commit();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString("userUid", "");
    }

    // 로그아웃
    public static void clearUserName(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void makeCryptoUser(Context context, String userName) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        String cryptoUid = AES256Crypto.AES_Encode(userName);
        editor.putString("cryptoUserUid", cryptoUid);
        editor.commit();
    }

    public static void setCryptoUser(Context context, String cryptoUserName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("cryptoUserUid", cryptoUserName);
        editor.commit();
    }

    public  static String getCryptoUserName(Context context){
        return getSharedPreferences(context).getString("cryptoUserUid","");
    }

    public static void setKeyForDB(Context context, String cryptoUserUid) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        String keyId = AES256Crypto.AES_Decode(cryptoUserUid);
        editor.putString("KeyForDB", keyId);
        editor.commit();
    }

    public static String getKeyForDB(Context context){
        return getSharedPreferences(context).getString("KeyForDB","");
    }
}
