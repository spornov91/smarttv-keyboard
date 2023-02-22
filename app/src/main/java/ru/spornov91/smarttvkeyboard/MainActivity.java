package ru.spornov91.smarttvkeyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends Activity 
{
	private static final String CHANNEL_ID = "CID";
	private static int pair[] = new int[2];
	//actions[ACTION_LEFT] 
	Intent intent;
	TextView etlang;
	TextView etkeyname;
	TextView etkeycode;
	Button bstart_service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		etlang =    findViewById(R.id.etlang);
		etkeyname = findViewById(R.id.etkeyname);
		etkeycode = findViewById(R.id.etkeycode);
		
		bstart_service = findViewById(R.id.bstart_service);
		bstart_service.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					createNotificationChannel();
					startService();
				}
			});

		new Thread(new Runnable() { 
				public void run()
				{
					Looper.prepare();
					while (true)
					{
						try
						{
                            Log.d("2", "2");
							new Handler(Looper.getMainLooper()).post(
								new Runnable() {
									@Override
									public void run()
									{
										//getView().setOnKeyListener(
										new OnKeyListener()
										{
											public boolean onKey(View v, int keyCode, KeyEvent event)
											{
												if (event.getAction() == KeyEvent.ACTION_DOWN)
												{
													Log.d("3", "3");
													switch (keyCode)
													{
														case KeyEvent.KEYCODE_VOLUME_UP:
															pair[0] = KeyEvent.KEYCODE_VOLUME_UP;
															etkeyname.setText("+");
															//etkeycode.setText("kc; " + keyCode);
															Toast.makeText(getApplicationContext(), "+", Toast.LENGTH_SHORT).show();
															return true;

														default:
															break;
													};
												};

												return false;
											};
										};

									}//run
								}//runable
							);//handler


							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{}
					}
				}
			}).start();
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		etkeycode.setText(Integer.toString(keyCode));
		Toast.makeText(getApplicationContext(), "" + keyCode, Toast.LENGTH_SHORT).show();

		switch (keyCode)
		{
			case KeyEvent.KEYCODE_META_LEFT:
				pair[0] = KeyEvent.KEYCODE_META_LEFT;
				return true;

			case KeyEvent.KEYCODE_CTRL_LEFT:
				pair[0] = KeyEvent.KEYCODE_CTRL_LEFT;
				return true;

			case KeyEvent.KEYCODE_ALT_LEFT:
				pair[0] = KeyEvent.KEYCODE_ALT_LEFT;
				etkeyname.setText("alt");
				etkeycode.setText("kc; " + keyCode);
				return true;

			case KeyEvent.KEYCODE_TAB:
				if (pair[0] == KeyEvent.KEYCODE_ALT_LEFT)
				{
					etkeyname.setText("alt+tab recently");
					etkeycode.setText("kc; " + keyCode);
					intent = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
					intent.setComponent(new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity"));
					startActivity(intent);
				}
				if (pair[0] == KeyEvent.KEYCODE_CTRL_LEFT)
				{
					etkeyname.setText("ctrl+tab prev app");
					etkeycode.setText("kc; " + keyCode);

					ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
					List<ActivityManager.RecentTaskInfo> recentTasks = activityManager.getRecentTasks(10, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
				    String packageName = recentTasks.get(1).baseIntent.getComponent().getPackageName();

					Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
					startActivity(launchIntent);
				}
				return true;

			case KeyEvent.KEYCODE_SHIFT_LEFT:
				if (pair[0] == KeyEvent.KEYCODE_ALT_LEFT)
				{
					etkeyname.setText("alt+shift");
					etkeycode.setText("kc; " + keyCode + " alt+shift; " + KeyEvent.KEYCODE_ALT_LEFT + KeyEvent.KEYCODE_SHIFT_LEFT);

					InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
					inputMethodManager.showInputMethodPicker();
				}
				return true;

			case KeyEvent.KEYCODE_1:
				if (pair[0] == KeyEvent.KEYCODE_CTRL_LEFT)
				{
					etkeyname.setText("ctrl+1 chrome");
					etkeycode.setText("kc; " + keyCode);

					String urlString = "googlechrome://navigate?url=http://google.com";
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setPackage("com.android.chrome");
					startActivity(intent);
				}
				return true;

			default:
				return super.onKeyDown(keyCode, event);
		}


	}

	@Override 
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_CTRL_LEFT:
				pair[0] = 0;
				return true;

			case KeyEvent.KEYCODE_ALT_LEFT:
				pair[0] = 0;
				return true;

			case KeyEvent.KEYCODE_META_LEFT:
				if (pair[0] == KeyEvent.KEYCODE_META_LEFT)
				{
					pair[0] = 0;
				    intent = new Intent(Intent.ACTION_MAIN);
				    intent.addCategory(Intent.CATEGORY_HOME);
				    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    startActivity(intent);
				}
				return true;
			default:
				return super.onKeyUp(keyCode, event);
		}
	}
	
	private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
				CHANNEL_ID,
				"smarttv-keyboard",
				NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
	};

	private void startService(){
	    Intent serviceIntent = new Intent(this, MainService.class);
	    startForegroundService(serviceIntent);
	};
};
