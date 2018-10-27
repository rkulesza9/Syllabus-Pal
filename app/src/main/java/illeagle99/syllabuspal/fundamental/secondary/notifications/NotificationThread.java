package illeagle99.syllabuspal.fundamental.secondary.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;

import illeagle99.syllabuspal.CourseListActivity;
import illeagle99.syllabuspal.MainActivity;
import illeagle99.syllabuspal.R;
import illeagle99.syllabuspal.fundamental.Assignment;
import illeagle99.syllabuspal.fundamental.CourseManager;

/**
 * Created by kules on 10/23/2016.
 */

public class NotificationThread extends Thread {
    private ArrayList<Assignment> assignments = new ArrayList<Assignment>();

    /* assumes NotificationUtility has non-null syllabus, non-null context */
    public NotificationThread(){
        CourseManager.getAssesToNotify(assignments);
    }

    public void update(){
        for(int x = assignments.size()-1; x >= 0; x--) assignments.remove(x);
        CourseManager.getAssesToNotify(assignments);
    }

    public void run(){
        while(true){
            for(int x = 0; x < assignments.size(); x++){
                if(assignments.get(x).nSettings().timeToNotify()){
                    onNotificationSent(assignments.get(x));
                }
            }
        }
    }
    public String statusString(int status){
        if(status == Assignment.NOT_STARTED) return "not started";
        if(status == Assignment.IN_PROGRESS) return "in progress";
        if(status == Assignment.COMPLETE) return "complete";
        if(status == Assignment.RUN_OUT_OF_TIME) return "running out of time";
        if(status == Assignment.RAN_OUT_OF_TIME) return "ran out of time";
        return "unusual";
    }
    public void onNotificationSent(Assignment ass){
        String fmtStr = String.format("%s", statusString(ass.status()));
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(NotificationUtility.getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(ass.name())
                        .setContentText(fmtStr)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000 })
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(NotificationUtility.getContext(), CourseListActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationUtility.getContext());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) NotificationUtility.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
