package ru.spornov91.smarttvkeyboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Locale;
import android.util.Log;

public class MainActivity extends Activity 
{
	//private static final String CHANNEL_ID = "CID";
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
				
				return true;
				
			case KeyEvent.KEYCODE_SHIFT_LEFT:
				if (pair[0] == KeyEvent.KEYCODE_ALT_LEFT)
				{
				etkeyname.setText("alt+shift");
				etkeycode.setText("kc; " + keyCode + " alt+shift; " + KeyEvent.KEYCODE_ALT_LEFT + KeyEvent.KEYCODE_SHIFT_LEFT);

				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
			    String localeString = ims.getLocale();
				Locale locale = new Locale(localeString);
				String currentLanguage = locale.getDisplayLanguage();
				etlang.setText(currentLanguage);
				}
				return true;

			case KeyEvent.KEYCODE_1:
				etkeyname.setText("chrome");
				etkeycode.setText("kc; " + keyCode + " 1;");

				String urlString = "googlechrome://navigate?url=http://google.com";
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setPackage("com.android.chrome");
				startActivity(intent);
				return true;

//			case KeyEvent.KEYCODE_VOLUME_UP:
//				pair[0] = KeyEvent.KEYCODE_VOLUME_UP;
//				etkeyname.setText("+");
//				etkeycode.setText("kc; " + keyCode);
//				return true;
//
//			case KeyEvent.KEYCODE_VOLUME_DOWN:
//				if (pair[0] == KeyEvent.KEYCODE_VOLUME_UP)
//				{
//					
//					
//					pair[0] = KeyEvent.KEYCODE_VOLUME_DOWN;
//					etkeyname.setText("ralt+rshift");
//					etkeycode.setText("kc; " + keyCode);
//				}
//				return true;
				
			default:
			return super.onKeyDown(keyCode, event);
		}

		
	}

	@Override 
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
//			case KeyEvent.KEYCODE_VOLUME_UP:
//				if (pair[0] == KeyEvent.KEYCODE_VOLUME_UP)
//				{
//					pair[0] = 0;
//				}
//				return true;
				
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
	
};
