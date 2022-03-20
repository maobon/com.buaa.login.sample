package com.buaa.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SharedPreferenceUtils {

    private final SharedPreferences mSharedPreference;

    private static SharedPreferenceUtils sOurInstance = null;

    public static SharedPreferenceUtils getInstance(Context context, String filename) {
        if (sOurInstance == null) {
            synchronized (SharedPreferenceUtils.class) {
                if (sOurInstance == null) {
                    sOurInstance = new SharedPreferenceUtils(context, filename);
                }
            }
        }
        return sOurInstance;
    }

    public boolean isUsernameSaved(String username) {
        return mSharedPreference.contains(username);
    }

    private SharedPreferenceUtils(Context context, String filename) {
        mSharedPreference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    public String[] queryAllUsernames() {
        Map<String, ?> all = mSharedPreference.getAll();
        Set<String> usernames = all.keySet();
        if(usernames.size() == 0) return new String[0];
        List<String> list = new ArrayList<>(usernames);
        return list.toArray(new String[0]);
    }

    public boolean save(String key, String val) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(val.getBytes(StandardCharsets.UTF_8));
            String s = Base64.encodeToString(digest, Constants.BASE64_FLAG);
            return mSharedPreference.edit().putString(key, s).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String get(String key) {
        return mSharedPreference.getString(key, null);
    }
}
