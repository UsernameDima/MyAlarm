package com.mycode.myalarm;

import android.app.AlarmManager;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cards.notification.R;

import java.util.Calendar;

public class MainActivity extends Activity {

    Button button;
    EditText etYear, etMon, etDay, etHour, etMin;
    Integer year, mon, day, hour, min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etYear = (EditText)findViewById(R.id.etYear);
        etMon = (EditText)findViewById(R.id.etMon);
        etDay = (EditText)findViewById(R.id.etDay);
        etHour = (EditText)findViewById(R.id.etHour);
        etMin = (EditText)findViewById(R.id.etMin);



    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                if(TextUtils.isEmpty(etYear.getText().toString())
                        ||TextUtils.isEmpty(etYear.getText().toString())
                        || TextUtils.isEmpty(etYear.getText().toString())
                        || TextUtils.isEmpty(etYear.getText().toString())
                        || TextUtils.isEmpty(etYear.getText().toString())
                        || TextUtils.isEmpty(etYear.getText().toString())
                        ){Toast.makeText(getApplicationContext(),"empty lines",Toast.LENGTH_SHORT).show(); return;}

                year = Integer.valueOf(etYear.getText().toString());
                mon = Integer.valueOf(etMon.getText().toString());
                day = Integer.valueOf(etDay.getText().toString());

                hour = Integer.valueOf(etHour.getText().toString());
                min = Integer.valueOf(etMin.getText().toString());

                restartNotify(getNotification("Edit time"));
                Intent intent = new Intent(getApplicationContext(),NotificationPublisher.class);
                intent.putExtra("text_event", "DA");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5:
                scheduleNotification(getNotification("5 second delay"), 5000);
                return true;
            case R.id.action_10:
                scheduleNotification(getNotification("10 second delay"), 10000);
                return true;
            case R.id.action_30:
                scheduleNotification(getNotification("30 second delay"), 30000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CancelAlarm(Context context) {

        Intent intent = new Intent(context, NotificationPublisher.class);

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(sender); // Отменяем будильник, связанный с интентом данного класса

    }

    private void restartNotify(Notification notification) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, mon -1); // -1 array {0....29/30}
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);}

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
    }

}
