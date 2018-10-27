package illeagle99.syllabuspal;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.appearance.ThemeSettings;
import illeagle99.syllabuspal.fundamental.Assignment;

public class ThemeSelectionActivity extends AppCompatActivity {
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_selection);
        status = getIntent().getIntExtra("status",-1);

        /*set title*/
        ((TextView)findViewById(R.id.textView7)).setText(statusStr(status));
        setupListview();
        SDColorsUtility.color(this,findViewById(R.id.ats),Assignment.NOT_STARTED,false);

    }

    public String statusStr(int status){
        if(status == Assignment.NOT_STARTED) return "Not Started";
        if(status == Assignment.IN_PROGRESS) return "In Progress";
        if(status == Assignment.COMPLETE) return "Complete";
        if(status == Assignment.RUN_OUT_OF_TIME) return "Running Out Of Time";
        if(status == Assignment.RAN_OUT_OF_TIME) return "Ran Out Of Time";
        return null;
    }

    public void setupListview(){
        ListView listview = (ListView) findViewById(R.id.listView);
        ArrayList<String> strings = new ArrayList<String>();
        for(int x = 0; x < ThemeSettings.numThemes(); x++) strings.add("");
        ThemeSelectionAdapter adapter = new ThemeSelectionAdapter(this,strings);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setChoice(i);
            }
        });
    }

    public void setChoice(int i){
        String choice = ThemeSettings.getTheme(i);
        if(status == 0) ThemeSettings.notStarted = (choice);
        if(status == 1) ThemeSettings.inProgress = (choice);
        if(status == 2) ThemeSettings.complete = (choice);
        if(status == 3) ThemeSettings.runningOutOfTime = (choice);
        if(status == 4) ThemeSettings.ranOutOfTime = (choice);
        ThemeSettings.save();
        Snackbar.make(findViewById(R.id.ats),"settings saved!",Snackbar.LENGTH_SHORT).show();
    }

    public static class ThemeSelectionAdapter extends ArrayAdapter<String> {
        private Context cont;
        private boolean hasBeenSet;

        public ThemeSelectionAdapter(Context context,ArrayList<String> strings){
            super(context,android.R.layout.simple_list_item_1,strings);
            cont = context;
            hasBeenSet = false;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View view = super.getView(position,convertView,parent);
            setColor(position,view);
            return view;
        }

        private void setColor(int position,View convertView){
            int[][] code = ThemeSettings.parseTheme(position);
            convertView.setBackgroundColor(Color.rgb(code[1][0],code[1][1],code[1][2]));
        }
    }
}
