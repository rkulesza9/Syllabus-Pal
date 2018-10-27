package illeagle99.syllabuspal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.fundamental.Course;
import illeagle99.syllabuspal.fundamental.CourseManager;
import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.RealtimeTextUpdater;

public class CourseOptionsActivity extends AppCompatActivity {
    private Course selCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_options);
        selCourse = CourseManager.getSelected();
        //setupTextViews();
        //setColors();
    }

    public void setColors(){
        SDColorsUtility.colorForegroundComponents(this,false,R.id.co_edit,R.id.ass,R.id.stats);
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.relly2);
    }

    public void setupTextViews(){
        RealtimeTextUpdater.setTextViewText(this,selCourse.name(),R.id.co_cname);
        RealtimeTextUpdater.setTextViewText(this, DateUtility.format(selCourse.to()),R.id.co_toview);
        RealtimeTextUpdater.setTextViewText(this,DateUtility.format(selCourse.from()),R.id.co_fromview);
    }

    /* buttons */
    public void onAssPress(View v){
        Intent intent = new Intent(this,AssListActivity.class);
        startActivity(intent);
    }
    public void onStatsPress(View v){
        Intent intent = new Intent(this,GraphActivity.class);
        startActivity(intent);
    }
    public void onEditPress(View v){
        Intent intent = new Intent(this,CourseEditActivity.class);
        startActivity(intent);
    }

    public void onResume(){
        super.onResume();
        setupTextViews();
        setColors();
    }

    public void onBackPressed(){
        CourseManager.setSelected(null);
        super.onBackPressed();
    }
}
