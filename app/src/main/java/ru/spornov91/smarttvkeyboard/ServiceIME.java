package ru.spornov91.smarttvkeyboard;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.util.List;

public class ServiceIME
extends InputMethodService
{
	private static String TAG = "ServiceIME";
	private static int pair[] = new int[2];
	//actions[ACTION_LEFT] 
	Intent intent;
	public void onCreate()
	{

        super.onCreate();
    }

    public void onDestroy()
	{

        super.onDestroy();
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//Log.d(TAG, Integer.toString(keyCode));
		//Toast.makeText(getApplicationContext(), Integer.toString(keyCode), Toast.LENGTH_SHORT).show();
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
				return true;

			case KeyEvent.KEYCODE_TAB:
				if (pair[0] == KeyEvent.KEYCODE_ALT_LEFT)
				{
					//"alt+tab recently"
					if (
						(Build.VERSION.SDK_INT == 4.1)
						)
					{
						intent = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
						intent.setComponent(new ComponentName("com.android.systemui", "com.android.internal.policy.impl.RecentApplicationsDialog"));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						this.startActivity(intent);
					}
					if (
						(Build.VERSION.SDK_INT >= 4.2)
						&& (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
						)
					{
						intent = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
						intent.setComponent(new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity"));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						this.startActivity(intent);
					}
					if (
						(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
						&& (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1)
						)
					{
						intent = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
						intent.setComponent(new ComponentName("com.android.systemui", "com.android.systemui.recents.RecentsActivity"));
						intent.setAction(Intent.ACTION_VIEW); 
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						this.startActivity(intent);
					}

				}
				if (pair[0] == KeyEvent.KEYCODE_CTRL_LEFT)
				{
					//"ctrl+tab prev app"
					if (
						(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
						)
					{
						ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
						List<ActivityManager.RecentTaskInfo> recentTasks = activityManager.getRecentTasks(10, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
						String packageName = recentTasks.get(1).baseIntent.getComponent().getPackageName();

						Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						this.startActivity(launchIntent);
					}
				}
				return true;

			case KeyEvent.KEYCODE_SHIFT_LEFT:
				if (pair[0] == KeyEvent.KEYCODE_CTRL_LEFT)
				{
					//"ctrl+shift"
					InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
					inputMethodManager.showInputMethodPicker();
				}
				if (pair[0] == KeyEvent.KEYCODE_ALT_LEFT)
				{
					// alt+shift
					// switch lang
					break;
				}
				return true;

			case KeyEvent.KEYCODE_1:
				if (pair[0] == KeyEvent.KEYCODE_CTRL_LEFT)
				{
					//"ctrl+1 chrome"
					intent = new Intent(Intent.ACTION_VIEW);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setPackage("com.android.chrome");
					this.startActivity(intent);
				}
				return true;
				
			case KeyEvent.KEYCODE_2:
				if (pair[0] == KeyEvent.KEYCODE_CTRL_LEFT)
				{
				//"ctrl+2 chrome"
				String pkgName = "com.android.chrome";
				Intent intent = this.getPackageManager().getLaunchIntentForPackage(pkgName);
				if (intent != null)
				{
					this.startActivity(intent);
				}
				}
				return true;

			default:
				return super.onKeyDown(keyCode, event);
		}
		return true;
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
					if (
						(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
						)
					{
						intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						this.startActivity(intent);
					}
				}
				return true;
			default:
				return super.onKeyUp(keyCode, event);
		}
	}
}
