package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class CheckInternetConnection {

    public boolean isNetworkAvailable(Context context) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        System.out.println("TRANSPORT_CELLULAR");
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        System.out.println("TRANSPORT_WIFI");
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        System.out.println("TRANSPORT_ETHERNET");
                        return true;
                    }
                } /*else {
                    System.out.println();
                }*/
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                            System.out.println("TYPE_MOBILE");
                        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                            System.out.println("TYPE_WIFI");
                        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET){
                            System.out.println("TYPE_ETHERNET");
                        }
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("exception: " + e.getMessage());
                }
            }
        }

        return false;
    }




}
