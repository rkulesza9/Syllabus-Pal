package illeagle99.syllabuspal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.fundamental.Assignment;
import illeagle99.syllabuspal.fundamental.Course;
import illeagle99.syllabuspal.fundamental.CourseManager;
import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.NotificationSettings;
import illeagle99.syllabuspal.fundamental.secondary.RealtimeTextUpdater;
import illeagle99.syllabuspal.fundamental.secondary.notifications.NotificationUtility;

public class AssEditActivity extends AppCompatActivity {
    private Course selCourse;
    private Assignment selAss;
    private boolean assIsNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ass_edit);
        selCourse = CourseManager.getSelected();
        selAss = selCourse.selected();
        assIsNew = false;
        setupListeners();
        if(selAss != null){
            configureText();
        }
        else {
            assIsNew = true;
            selAss = new Assignment();
            selAss.nSettings(new NotificationSettings());
            selCourse.select(selAss); /*keep unattached from course until saved */
            selCourse.add(selAss);
        }
        setupColors();
    }

    public void save(){
        CourseManager.save();
        NotificationUtility.setTimeToUpdate(true);
    }

    public void setupListeners(){
        EditText nameEdit = (EditText) findViewById(R.id.aae_nameedit);
        RealtimeTextUpdater listener = new RealtimeTextUpdater((TextView) findViewById(R.id.aea_nameview)){
            public void onSave(String text){
                if(text.equals(selAss.name()));
                else if(selCourse.nameCheck(text)){
                    selAss.name(text);
                    save();
                } else {
                    Context context = getApplicationContext();
                    Toast.makeText(context,text+" is currently the name of another assignment.",Toast.LENGTH_SHORT).show();
                }
            }
        };
        listener.setTextViewText("new assignment");
        nameEdit.addTextChangedListener(listener);
    }

    public void configureText(){
        String name = selAss.name();
        String from = DateUtility.format(selAss.from());
        String to = DateUtility.format(selAss.to());
        String status = selAss.status() == Assignment.NOT_STARTED ? "Not Started" :
                        selAss.status() == Assignment.IN_PROGRESS ? "In Progress" :
                        "Complete";

        ((TextView) findViewById(R.id.aea_nameview)).setText(name);
        ((EditText) findViewById(R.id.aae_nameedit)).setText(name);
        ((TextView) findViewById(R.id.aae_fromview)).setText(from);
        ((Button) findViewById(R.id.morf)).setText("starts on "+from);
        ((TextView) findViewById(R.id.aae_toview)).setText(to);
        ((Button) findViewById(R.id.ot)).setText("ends on "+to);
        ((Button) findViewById(R.id.statusbutton)).setText(status);
    }

    /* on Status Button Press -- edit out selAssIsNull checks */
    public void onStatusButtonPress(View v){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        String[] statusOptions = {"Not Started","In Progress","Complete"};
        adb.setItems(statusOptions,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface di,int item){
                if(item == 0){
                    ((Button) findViewById(R.id.statusbutton)).setText("Not Started");
                    boolean selAssIsNull = selAss == null;
                    if(selAssIsNull) selAss = new Assignment();
                    selAss.status("Not Started");
                    setupColors();
                    if(selAssIsNull) selAss = null;
                }
                if(item == 1){
                    ((Button) findViewById(R.id.statusbutton)).setText("In Progress");
                    boolean selAssIsNull = selAss == null;
                    if(selAssIsNull) selAss = new Assignment();
                    selAss.status("In Progress");
                    setupColors();
                    if(selAssIsNull) selAss = null;
                }
                if(item == 2){
                    ((Button) findViewById(R.id.statusbutton)).setText("Complete");
                    boolean selAssIsNull = selAss == null;
                    if(selAssIsNull) selAss = new Assignment();
                    selAss.status("Complete");
                    setupColors();
                    if(selAssIsNull) selAss = null;
                }
            }
        });
        adb.setTitle("Select Assignment's Status:");
        adb.create().show();
    }

    /*notif options press*/
    public void onNOptionsPress(View v){
        Intent intent = new Intent(this,NotificationSettingsActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        CourseManager.getSelected().select(null);
        super.onBackPressed();
    }

    /*setup colors*/
    public void setupColors(){
        SDColorsUtility.colorBackgroundComponents(this,assIsNew,R.id.aae_rel);
        SDColorsUtility.colorForegroundComponents(this,assIsNew,R.id.aae_nameedit,R.id.morf,R.id.ot,
                                     R.id.aae_noptions,R.id.statusbutton);
    }

    public void onFromPress(View v){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(selAss.from());
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker dp, int year, int month, int date){
                String dateStr = (month+1)+"/"+date+"/"+year;
                ((Button) findViewById(R.id.morf)).setText("starts on "+dateStr);
                ((TextView) findViewById(R.id.aae_fromview)).setText((month+1)+"/"+date+"/"+year);

                long dateLng = DateUtility.parse(dateStr);
                if(selAss.to() == 0){
                    selAss.to(dateLng); /* protects it from being 0 and breaking everything */
                    selAss.from(dateLng);
                    save();
                } else if(selAss.to() >= dateLng){
                    selAss.from(dateLng);
                    save();
                } else {
                    String toastText = "start date cannot occur after end date";
                    Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dpd.show();
    }
    public void onToPress(View v){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(selAss.to());
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker dp,int year, int month, int date){
                String dateStr = (month+1)+"/"+date+"/"+year;
                ((Button) findViewById(R.id.ot)).setText("ends on "+dateStr);
                ((TextView) findViewById(R.id.aae_toview)).setText((month+1)+"/"+date+"/"+year);

                long dateLng = DateUtility.parse(dateStr);
                if(selAss.from() == 0){
                    selAss.to(dateLng); /* protects it from being 0 and breaking everything */
                    selAss.from(dateLng);
                    save();
                } else if(dateLng >= selAss.from()){
                    selAss.to(dateLng);
                    save();
                } else {
                    String toastText = "start date cannot occur after end date";
                    Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dpd.show();
    }
}
