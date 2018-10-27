package illeagle99.syllabuspal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.fundamental.Course;
import illeagle99.syllabuspal.fundamental.CourseManager;
import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.GraphSettings;
import illeagle99.syllabuspal.fundamental.secondary.RealtimeTextUpdater;
import illeagle99.syllabuspal.fundamental.secondary.notifications.NotificationUtility;


public class CourseEditActivity extends AppCompatActivity {
    private boolean newCourseFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        setupListeners();
        if (CourseManager.getSelected() != null) {
            newCourseFlag = false;
            configureTextShown();
        } else{
            newCourseFlag = true;
            CourseManager.setSelected(new Course());
            CourseManager.addCourse(CourseManager.getSelected());
        }

    }

    public void setColors(){
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.celayout);
        SDColorsUtility.colorForegroundComponents(this,false,R.id.cname2,R.id.frommy,R.id.toy);
    }

    /*stuff that makes everything work*/
    public void setupListeners() {
        EditText cname = (EditText) findViewById(R.id.cname2);
        RealtimeTextUpdater listener = new RealtimeTextUpdater((TextView) findViewById(R.id.cnamef)){
            public void onSave(String text){
                if(text.equals(CourseManager.getSelected().name()));
                else if(CourseManager.nameCheck(text)){
                    CourseManager.getSelected().name(text);
                    save();
                } else {
                    String toastText = text+" is already the name of another course";
                    Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
                }
            }
        };
        cname.addTextChangedListener(listener);
    }

    public void save(){
        CourseManager.save();
        NotificationUtility.setTimeToUpdate(true);
    }

    private void configureTextShown(){
        ((TextView) findViewById(R.id.cnamef)).setText(CourseManager.getSelected().name());
        ((TextView) findViewById(R.id.toview)).setText(DateUtility.format(CourseManager.getSelected().to()));
        ((TextView) findViewById(R.id.fromview)).setText(DateUtility.format(CourseManager.getSelected().from()));
        ((EditText) findViewById(R.id.cname2)).setText(CourseManager.getSelected().name());
        ((Button) findViewById(R.id.toy)).setText("ends on "+DateUtility.format(CourseManager.getSelected().to()));
        ((Button) findViewById(R.id.frommy)).setText("starts on "+DateUtility.format(CourseManager.getSelected().from()));
    }

    public void onResume(){
        super.onResume();
        setColors();
    }

    public void onBackPressed(){
        super.onBackPressed();
        if(newCourseFlag) CourseManager.setSelected(null);
    }

    public void onFromPress(View v){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker dp,int year, int month, int date){
                String dateStr = (month+1)+"/"+date+"/"+year;
                ((Button) findViewById(R.id.frommy)).setText("starts on "+dateStr);
                ((TextView) findViewById(R.id.fromview)).setText(dateStr);

                Long dateLng = DateUtility.parse(dateStr);
                Course course = CourseManager.getSelected();
                if(course.to() == 0){
                    course.to(dateLng);
                    course.from(dateLng);
                    save();
                }
                else if(course.to() >= course.from()){
                    course.from(dateLng);
                    save();
                } else {
                    String toastText = "start date cannot occur after end date";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        dpd.show();
    }
    public void onToPress(View v){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker dp,int year, int month, int date){
                String dateStr = (month+1)+"/"+date+"/"+year;
                ((Button) findViewById(R.id.toy)).setText("ends on "+dateStr);
                ((TextView) findViewById(R.id.toview)).setText(dateStr);

                Long dateLng = DateUtility.parse(dateStr);
                Course course = CourseManager.getSelected();
                if(course.to() == 0){
                    course.to(dateLng);
                    course.from(dateLng);
                    save();
                }
                else if(course.to() >= course.from()){
                    course.to(dateLng);
                    save();
                } else {
                    String toastText = "start date cannot occur after end date";
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dpd.show();
    }
}
