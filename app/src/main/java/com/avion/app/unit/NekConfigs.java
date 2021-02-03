package com.avion.app.unit;

import com.avion.app.MainActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NekConfigs {
    public static final String apiURL = "http://www.avion.com.ua/api.php";
    public static final String tinkoftPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv5yse9ka3ZQE0feuGtemYv3IqOlLck8zHUM7lTr0za6lXTszRSXfUO7jMb+L5C7e2QNFs+7sIX2OQJ6a+HG8kr+jwJ4tS3cVsWtd9NXpsU40PE4MeNr5RqiNXjcDxA+L4OsEm/BlyFOEOh2epGyYUd5/iO3OiQFRNicomT2saQYAeqIwuELPs1XpLk9HLx5qPbm8fRrQhjeUD5TLO8b+4yCnObe8vy/BMUwBfq+ieWADIjwWCMp2KTpMGLz48qnaD9kdrYJ0iyHqzb2mkDhdIzkim24A3lWoYitJCBrrB2xM05sm9+OdCI1f7nPNJbl5URHobSwR94IRGT7CJcUjvwIDAQAB";
    public static final String tinkoftPassword = "r00ljp4b697ji1sf";
    public static final String tinkoftTerminalKey = "1557743767826";
    public static String paymethod = "cash";

    public static FirebaseAnalytics analytics;
    public static String last4 = "";
    public static String mail = "none";
    public static int paylogo = -1;

    public static String getUserID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        analytics = FirebaseAnalytics.getInstance(MainActivity.getAppContext());
        if (currentUser == null) {


            MainActivity.logstr("USER_ID: test_user");
            return "test_user";
        } else {

            MainActivity.logstr("USER_ID: " + currentUser.getUid());
            return currentUser.getUid();
        }
    }

    public static String getUserPgone() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null)
            return "+992918158585";
        else
            return currentUser.getPhoneNumber();
    }

    public static String getUserEmail() {
        return (mail.trim().length() != 0 && !mail.equals("none")) ? mail : " ";

    }

    public static String getAlphaNumericString(int n) {


        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";


        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {


            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());


            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
