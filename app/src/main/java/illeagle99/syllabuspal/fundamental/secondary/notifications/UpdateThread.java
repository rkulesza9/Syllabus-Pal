package illeagle99.syllabuspal.fundamental.secondary.notifications;

import android.content.Context;

/**
 * Created by kules on 10/23/2016.
 */

public class UpdateThread extends Thread {
    private Context context;

    public UpdateThread(Context context){
        this.context = context;
    }

    public void run(){
        while(true){
            if(!NotificationUtility.isThreadRunning()) onRequest();
            if(NotificationUtility.isTimeToUpdate()){
                onUpdate();
                NotificationUtility.setTimeToUpdate(false);
            }
        }
    }

    public void onRequest(){
        NotificationUtility.setThreadRunning(true);
    }
    public void onUpdate(){
        NotificationUtility.updateSyllabusFromFile(context);
    }
}
