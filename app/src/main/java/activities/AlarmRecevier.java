package activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.detection.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AlarmRecevier extends BroadcastReceiver {

    public AlarmRecevier(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";



    private FirebaseFirestore db;
    private Timestamp myTime;

    private int  myNotificationID = 1;
    private String channelID = "default";
    private String textTitle = "food";
    private String textContent = "2021-05-05";
    private String channel_name = "haha";
    private String channel_description = "food notification";

    //private NotificationCompat.Builder builder;

    private Button btn;
    private static final String TAG = "MyActivity";

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;

    private static PowerManager.WakeLock sCpuWakeLock;



    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        int count = Integer.parseInt(intent.getStringExtra("notiCount"));
        ArrayList<String> dates = intent.getStringArrayListExtra("notiDate");
        ArrayList<String> names = intent.getStringArrayListExtra("notiName");

        for (int i = 0; i < count; i++) {

            String date = (String) dates.get(i);


            String d[] = date.split("-");
            int yy = Integer.parseInt(d[0]);
            int mm = Integer.parseInt(d[1]);
            int dd = Integer.parseInt(d[2]);

//                                long now = System.currentTimeMillis();
//                                Date mdate = new Date(now);
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


            Calendar day = Calendar.getInstance();
            day.add(Calendar.DATE, +1);
            String getTime = new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(day.getTime());
            // String getTime = sdf.format(mdate);

            String current[] = getTime.split("-");
            int cyy = Integer.parseInt(current[0]);
            int cmm = Integer.parseInt(current[1]);
            // int cndd = Integer.parseInt(current[2]);
            String cr[] = current[2].split(" ");
            int cdd = Integer.parseInt(cr[0]);

            String cur[] = cr[1].split(":");
            int chh = Integer.parseInt(cur[0]);
            int cmmm = Integer.parseInt(cur[1]);
            int css = Integer.parseInt(cur[2]);



            if (yy == cyy && mm == cmm && dd == cdd && chh == 9&& cmmm == 0 ) { //


                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                builder = null;
                manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(
                            new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                    );
                    builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                } else {
                    builder = new NotificationCompat.Builder(context);
                }

                //알림창 클릭 시 activity 화면 부름
                Intent intent2 = new Intent(context, SubActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent2, PendingIntent.FLAG_UPDATE_CURRENT);


                //알림창 제목
                //builder.setContentTitle("알람");
                //알림창 아이콘
                String res = change(names.get(i));
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(res, "drawable", context.getPackageName()));
                builder.setSmallIcon(context.getResources().getIdentifier(res, "drawable", context.getPackageName()));
               // builder.setSmallIcon(context.getResources().getIdentifier(res, "drawable", context.getPackageName()));
                builder.setLargeIcon(bitmap);
                builder.setContentTitle("유통기한 임박 안내");
                builder.setContentText(names.get(i)+"의 유통기한이 하루 남았습니다!");
                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                //알림창 터치시 자동 삭제
                builder.setAutoCancel(true);

                builder.setContentIntent(pendingIntent);


                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "My:Tag");
                wakeLock.acquire(5000);

                Notification notification = builder.build();

                manager.notify((int) (System.currentTimeMillis() / 1000) + i, notification);


                //(int)(System.currentTimeMillis()/1000)

            }
        }
    }



    public String change(String name){
        String res = "foods";
        res.replace(" ","");
        String[] eng = context.getResources().getStringArray(R.array.eng_name);
        String[] kor = context.getResources().getStringArray(R.array.kor_name);

        for(int i=0;i<eng.length;i++){
            if(name.equals(kor[i])){
                res = eng[i];
            }
        }
        return res;
    }


}


