package illeagle99.syllabuspal;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.fundamental.CourseManager;
import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.NotificationSettings;
import illeagle99.syllabuspal.fundamental.secondary.RealtimeTextUpdater;
import illeagle99.syllabuspal.fundamental.secondary.notifications.NotificationUtility;

public class NotificationSettingsActivity extends AppCompatActivity {
    private NotificationSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        settings = CourseManager.getSelected().selected().nSettings();
        updateText();
        setColors();
    }

    public void updateText(){
        String frequency = NotificationSettings.frequencyAsStr(settings.frequency());
        ((Button) findViewById(R.id.ns1)).setText(DateUtility.formatTime(DateUtility.longToDate(settings.from())));
        ((Button) findViewById(R.id.ns2)).setText(DateUtility.formatTime(DateUtility.longToDate(settings.to())));
        int index = getIndexForSpinner();
        EditText tfield = (EditText) findViewById(R.id.editText3);
        long l = settings.getFreqBase() == 0 ? 1 : settings.getFreqBase();
        tfield.setText((settings.frequency()/l)+"");
        setupFrequencySetter();
        setupMultiplierSetter();
        ((Spinner) findViewById(R.id.spinner3)).setSelection(index);
    }

    private void setupMultiplierSetter(){
        EditText t = (EditText) findViewById(R.id.editText3);
        t.addTextChangedListener(new RealtimeTextUpdater(null){
            public void onSave(String text){
                long multiplier;
                try {
                    multiplier = Math.abs(Integer.parseInt(text));
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"frequency multiplier must be an integer",Toast.LENGTH_SHORT).show();
                    multiplier = 1;
                }
                settings.frequency(settings.getFreqBase() * multiplier);
            }
        });
    }

    public void save(){
        CourseManager.save();
        NotificationUtility.setTimeToUpdate(true);
    }

    private int getIndexForSpinner(){ /* compare fbase to base values */
        long choice = settings.getFreqBase();
        if(choice == NotificationSettings.NONE) return 0;
        if(choice == NotificationSettings.MINUTES) return 1;
        if(choice == NotificationSettings.HOURS) return 2;
        if(choice == NotificationSettings.DAYS) return 3;
        if(choice == NotificationSettings.WEEKS) return 4;
        return -1;
    }

    /*frequency*/
    public void setupFrequencySetter(){
        Spinner spinner = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        adapter.add("none");
        adapter.add("minutes");
        adapter.add("hours");
        adapter.add("days");
        adapter.add("weeks");
        spinner.setAdapter(adapter);

        /* spinner must be second thing adjusted for textfield value to save - fix, seperate data collection */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Context cont = getApplicationContext();
                EditText tfield = (EditText) findViewById(R.id.editText3);
                String item = (String) adapterView.getItemAtPosition(i);
                int multiplier = 0;
                try{
                    multiplier = Math.abs(Integer.parseInt(tfield.getText().toString()));
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(cont,"Only Integers Allowed",Toast.LENGTH_SHORT).show();
                }
                if(item.equals("none")){
                    settings.setFreqBase(NotificationSettings.NONE);
                    settings.frequency(settings.getFreqBase() * multiplier);
                } else if(item.equals("minutes")){
                    settings.setFreqBase(NotificationSettings.MINUTES);
                    settings.frequency(settings.getFreqBase() * multiplier);
                } else if(item.equals("hours")){
                    settings.setFreqBase(NotificationSettings.HOURS);
                    settings.frequency(settings.getFreqBase() * multiplier);
                } else if(item.equals("days")) {
                    settings.setFreqBase(NotificationSettings.DAYS);
                    settings.frequency(settings.getFreqBase() * multiplier);
                } else if(item.equals("weeks")) {
                    settings.setFreqBase(NotificationSettings.WEEKS);
                    settings.frequency(settings.getFreqBase() * multiplier);
                }
                save();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setColors(){
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.rel2);
        SDColorsUtility.colorForegroundComponents(this,false,R.id.ns1,R.id.ns2);
    }

    public void onStartTimePress(View v){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            public void onTimeSet(TimePicker tp, int hour, int minute){
                String time = hour+":";
                if(minute < 10) time += "0";
                time += minute;
                ((Button) findViewById(R.id.ns1)).setText(time);

                long startTime = DateUtility.parseTime(time);
                if(settings.to() == 0){
                    settings.to(startTime);
                    settings.from(startTime);
                    save();
                } else if(settings.to() >= startTime){
                    settings.from(startTime);
                    save();
                } else  {
                    String toastText = "end time cannot occur after start time";
                    Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
                }
            }

        },gc.get(Calendar.HOUR_OF_DAY),gc.get(Calendar.MINUTE),true);
        dialog.show();

    }
    public void onEndTimePress(View v){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            public void onTimeSet(TimePicker tp, int hour, int minute){
                String time = hour+":";
                if(minute < 10) time += "0";
                time += minute;

                long endTime = DateUtility.parseTime(time);
                if(settings.to() == 0){
                    settings.to(endTime);
                    settings.from(endTime);
                    save();
                } else if(endTime >= settings.from()){
                    settings.to(endTime);
                    save();
                } else  {
                    String toastText = "end time cannot occur after start time";
                    Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
                }
            }

        },gc.get(Calendar.HOUR_OF_DAY),gc.get(Calendar.MINUTE),true);
        dialog.show();
    }
}
