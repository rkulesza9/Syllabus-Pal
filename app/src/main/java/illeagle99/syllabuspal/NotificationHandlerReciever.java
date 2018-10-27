package illeagle99.syllabuspal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import illeagle99.syllabuspal.fundamental.secondary.notifications.NotificationThread;
import illeagle99.syllabuspal.fundamental.secondary.notifications.NotificationUtility;
import illeagle99.syllabuspal.fundamental.secondary.notifications.UpdateThread;

/**
 * Created by kules on 9/5/2016.
 */
public class NotificationHandlerReciever extends BroadcastReceiver {

    public void onReceive(Context context,Intent intent){

        NotificationUtility.setContext(context);
        NotificationUtility.setupFile();

        if(threadAlreadyRunning()){
            return;
        }

        NotificationUtility.updateSyllabusFromFile(context);

        UpdateThread ut = new UpdateThread(context);
        ut.start();

        NotificationThread nt = new NotificationThread();
        nt.start();
    }

    private static boolean threadAlreadyRunning(){
        NotificationUtility.setThreadRunning(false);
        NotificationUtility.hesitate(50);
        return NotificationUtility.isThreadRunning();
    }
}
