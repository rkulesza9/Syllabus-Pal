package illeagle99.syllabuspal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.fundamental.Assignment;

public class ColorsSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors_settings);

        /*color background*/
        //BkgndHandler bh = new BkgndHandler(this, new Syllabus(this),null,null);
        //bh.colorBackgroundComponents(R.id.acs);

        /*setup listview*/
        //setupListview();
    }

    public void setupListview(){
        ListView options = (ListView) findViewById(R.id.colorOptions1);
        String[] optionsSTRs = {"Not Started", "In Progress", "Complete", "Running out of Time", "Ran out of Time"};
        ColorsSettingsAdapter adapter = new ColorsSettingsAdapter(this);
        options.setAdapter(adapter);
        final Context context = this;
        options.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> aview, View view, int a, long b){
                Intent intent = new Intent(context,ThemeSelectionActivity.class);
                intent.putExtra("status",a);
                startActivity(intent);
            }
        });
    }

    public void onResume(){
        super.onResume();
        /*color background*/
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.acs);

        /*setup listview*/
        setupListview();
    }

    public static class ColorsSettingsAdapter extends ArrayAdapter<String> {
        private Context cont;
        private int r;

        public ColorsSettingsAdapter(Context context){
            super(context,android.R.layout.simple_list_item_1,new String[]{"Not Started","In Progress","Complete",
                    "Running Out Of Time", "Ran Out Of Time"});

            cont = context;
            r = 0;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View view = super.getView(position,convertView,parent);
            setColor(position,view);
            return view;
        }

        private void setColor(int position,View convertView){
            if(position == 0) SDColorsUtility.color(cont,convertView, Assignment.NOT_STARTED,true);
            if(position == 1) SDColorsUtility.color(cont,convertView,Assignment.IN_PROGRESS,true);
            if(position == 2) SDColorsUtility.color(cont,convertView,Assignment.COMPLETE,true);
            if(position == 3) SDColorsUtility.color(cont,convertView,Assignment.RUN_OUT_OF_TIME,true);
            if(position == 4) SDColorsUtility.color(cont,convertView,Assignment.RAN_OUT_OF_TIME,true);
        }
    }

}
