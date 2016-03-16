package com.veer.bhumiVO;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class AppUtils {

	/*
	 * Parse ApplicationId and ClientKey
	 */
	public static String ApplicationId = "KZnqMyxJ6F8jJNzbTuQic4DXFSxZTIjJCGZcZQkd";
	public static String ClientKey = "RdR4Ivy8jnmm7qztJq11lBQofV897X6toADjRb9g";

	// For Checking the Internet
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static void ShowAlertDialog(Context context,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle("BHUMI");
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								// do things
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
