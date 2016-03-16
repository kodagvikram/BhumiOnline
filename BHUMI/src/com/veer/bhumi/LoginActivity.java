package com.veer.bhumi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.veer.bhumiVO.AppUtils;
import com.veer.bhumiVO.BhumiIMEI_VO;

public class LoginActivity extends Activity {

	public EditText userid, password;
	public Button login;
	SharedPreferences sharedPreferences;
	String device_id = "";

	private boolean isValidUser = false,isimeinoExist=false;

	public ArrayList<String> deviceIdArrayList = new ArrayList<String>() {
		{
			add("B");
			add("911441050118797");
			add("353953065743679");
			add("358352054268621");
			add("353267065740993");
		}
	};

	// ----------------------------------------------------------------------------
	List<ParseObject> Parsedata = new ArrayList<ParseObject>();
	ArrayList<BhumiIMEI_VO> parseArrayList = new ArrayList<BhumiIMEI_VO>();
	// ---------------------------------------------------------------------------

	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	private ProgressDialog mProgressDialog;

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS1:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Processing request, Please wait ...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;

		default:
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		Constants.killAll();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		try {
			Constants.activity_stack.add(this);

			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			device_id = tm.getDeviceId();
			try {
				// Parse.enableLocalDatastore(getApplicationContext());
				Parse.initialize(LoginActivity.this, AppUtils.ApplicationId,
						AppUtils.ClientKey);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("SIGN", "");
			editor.putString("SUMTOTAL", "0");
			editor.commit();

			if (sharedPreferences.getString("username", "")
					.equalsIgnoreCase("")) {

			} else {
				// Intent intent=new Intent(getApplicationContext(),
				// JantriActivity.class);
				// startActivity(intent);
				// finish();

				if (AppUtils.isNetworkAvailable(LoginActivity.this))
					new getParseDataAsync().execute();
				else
					AppUtils.ShowAlertDialog(LoginActivity.this,
							"Please check the Internet Connection.");
			}

			userid = (EditText) findViewById(R.id.usereditext);
			// password=(EditText)findViewById(R.id.passwordedittext);
			login = (Button) findViewById(R.id.logbutton);
			login.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (AppUtils.isNetworkAvailable(LoginActivity.this))
						new getParseDataAsync().execute();
					else
						AppUtils.ShowAlertDialog(LoginActivity.this,
								"Please check the Internet Connection.");
					// if(Integer.parseInt(sharedPreferences.getString("LOGINCOUNT",
					// "0"))>2)
					// {
					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// LoginActivity.this);
					// builder.setTitle("BHUMI");
					// builder.setMessage("UNAUTHORIZED USER. ")
					// .setCancelable(false)
					// .setPositiveButton("OK",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog, int id) {
					// // do things
					// }
					// });
					// AlertDialog alert = builder.create();
					// alert.show();
					// }
					// else
					// {
					// if(userid.getText().toString().equals("")||!userid.getText().toString().equalsIgnoreCase("*VrushalI@4"))
					// {
					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// LoginActivity.this);
					// builder.setTitle("BHUMI");
					// builder.setMessage("Enter Valid Userid and Password")
					// .setCancelable(false)
					// .setPositiveButton("OK",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog, int id) {
					// // do things
					// }
					// });
					// AlertDialog alert = builder.create();
					// alert.show();
					//
					// int
					// cnt=Integer.parseInt(sharedPreferences.getString("LOGINCOUNT",
					// "0"));
					// cnt++;
					// SharedPreferences.Editor editor = sharedPreferences
					// .edit();
					// //editor.putString("password",
					// password.getText().toString());
					// editor.putString("LOGINCOUNT",""+cnt);
					// editor.commit();
					//
					// }//end of if
					// else
					// {
					// for(String object: deviceIdArrayList){
					//
					// if(object.equalsIgnoreCase(device_id))
					// {
					// isValidUser=true;
					// break;
					// }
					// }
					//
					// isValidUser=true;
					//
					// if(!isValidUser)
					// {
					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// LoginActivity.this);
					// builder.setTitle("BHUMI");
					// builder.setMessage("Please Contact \n9766846506\nto Purchase the app and get Login.")
					// .setCancelable(false)
					// .setPositiveButton("OK",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog, int id) {
					// // do things
					// }
					// });
					// AlertDialog alert = builder.create();
					// alert.show();
					// }
					// else
					// {
					// SharedPreferences.Editor editor = sharedPreferences
					// .edit();
					// //editor.putString("password",
					// password.getText().toString());
					// editor.putString("username", "VALID");
					// editor.putString("LOGINCOUNT","0");
					// editor.commit();
					//
					// Intent intent=new Intent(getApplicationContext(),
					// JantriActivity.class);
					// startActivity(intent);
					// finish();
					// }
					//
					// }//end of else
					//
					// }//end of else authorize

				}// end of submit click
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}// end of oncreate()

	public class getParseDataAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				getData();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String str_resp) {
			try {

				parseArrayList.clear();
				for (int i = 0; i < Parsedata.size(); i++) {
					BhumiIMEI_VO mvo = new BhumiIMEI_VO();
					mvo.imeino = Parsedata.get(i).getString("imeino");
					mvo.isValidDate = Parsedata.get(i).getBoolean("isvaliduser");
					parseArrayList.add(mvo);
				}

				if (mProgressDialog != null)
					mProgressDialog.dismiss();

				if (parseArrayList.size() > 0) {
					for (int i = 0; i < parseArrayList.size(); i++) {
						BhumiIMEI_VO mvo = parseArrayList.get(i);
						if (mvo.imeino.equalsIgnoreCase(device_id)) {
							isimeinoExist=true;
							if (mvo.isValidDate) {
								isValidUser = true;
								break;
							} 
						}
					}// end of for

					if (!isValidUser) {
						if(isimeinoExist)
							AppUtils.ShowAlertDialog(LoginActivity.this,"Your Demo Version is Expire ..!\n Please Contact \n9766846506\nto Purchase the app and get Login.");
						else
						AppUtils.ShowAlertDialog(
								LoginActivity.this,
								"Please Contact \n9766846506\nto  Purchase the app or get (Free) ''Demo Version'' ");
					} else {
						
						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putString("username", "VALID");
						editor.putString("LOGINCOUNT", "0");
						editor.commit();
						
						Intent intent = new Intent(getApplicationContext(),
								JantriActivity.class);
						startActivity(intent);
						finish();
					}

				}// end of if for size
				else
					AppUtils.ShowAlertDialog(
							LoginActivity.this,
							"Please Contact \n9766846506\nto  Purchase the app or get (Free) ''Demo Version'' ");


			} catch (Exception e) {
				// TODO: handle exception\
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(LoginActivity.this,
						"Error in getting data from Server please check internet connection.");
				e.printStackTrace();
			}
		}

	}// end of Async getParseDataAsync class

	public void getData() {

		 ParseQuery parseQuery = new ParseQuery("Bhumiimei");
		 parseQuery.whereEqualTo("imeino", device_id);

		//ParseQuery<ParseObject> query = ParseQuery.getQuery("Bhumiimei");
		// query.orderByDescending("createdAt");
		try {

			Parsedata = parseQuery.find();
			// ParseObject.pinAllInBackground(message);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}// end of Activity
