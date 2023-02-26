package ru.spornov91.smarttvkeyboard;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity1 extends Activity 
{
	private static final String CHANNEL_ID = "CID";
	
	EditText et1;
	TextView etlang;
	TextView etkeyname;
	TextView etkeycode;
	Button bstart_service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		et1 = findViewById(R.id.et1);
		etlang =    findViewById(R.id.etlang);
		etkeyname = findViewById(R.id.etkeyname);
		etkeycode = findViewById(R.id.etkeycode);

		bstart_service = findViewById(R.id.bstart_service);
		bstart_service.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0)
				{
					createNotificationChannel();
					startService();
				}
			});
	};
	
	private void createNotificationChannel()
	{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
            NotificationChannel serviceChannel = new NotificationChannel(
				CHANNEL_ID,
				"smarttv-keyboard",
				NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
	};

	private void startService()
	{
	    Intent serviceIntent = new Intent(this, ServiceNotify.class);
	    startForegroundService(serviceIntent);
	};
};
