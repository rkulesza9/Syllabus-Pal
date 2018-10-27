package illeagle99.syllabuspal;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.appearance.ThemeSettings;
import illeagle99.syllabuspal.fundamental.*;
import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.GraphSettings;

public class GraphActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private PieChart mChart;
    private GraphSettings gsetts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        gsetts = CourseManager.getSelected().graphSettings();
        setupBackground();
        setupCourseInfo();

        //pie chart crap
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(SDColorsUtility.getBackgroundColor(CourseManager.getSelected().status())); /* set color to Course Bkgnd */

        mChart.setTransparentCircleColor(Color.WHITE); /* ? probably must change */
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
    }

    public void setupBackground(){
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.graphrel);
    }

    public void setupCourseInfo(){
        TextView title = (TextView) findViewById(R.id.statscname);
        TextView from = (TextView) findViewById(R.id.statsfrom);
        TextView to = (TextView) findViewById(R.id.statsto);

        title.setText(CourseManager.getSelected().name());
        to.setText(DateUtility.format(CourseManager.getSelected().to()));
        from.setText(DateUtility.format(CourseManager.getSelected().from()));
    }


    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        String[] statuses = {"Not Started","In Progress","Complete","Running Out Of Time","Ran Out Of Time"};
        double[] percTot = {gsetts.perOfTotal(Assignment.NOT_STARTED), gsetts.perOfTotal(Assignment.IN_PROGRESS),
                            gsetts.perOfTotal(Assignment.COMPLETE), gsetts.perOfTotal(Assignment.RUN_OUT_OF_TIME),
                            gsetts.perOfTotal(Assignment.RAN_OUT_OF_TIME) };
        for(int x = 0; x < 5; x++)
            entries.add(new PieEntry((float) percTot[x],statuses[x]));

        PieDataSet dataSet = new PieDataSet(entries, "Course Status");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(SDColorsUtility.getColors(ThemeSettings.notStarted())[1]);
        colors.add(SDColorsUtility.getColors(ThemeSettings.inProgress())[1]);
        colors.add(SDColorsUtility.getColors(ThemeSettings.complete())[1]);
        colors.add(SDColorsUtility.getColors(ThemeSettings.runningOutOfTime())[1]);
        colors.add(SDColorsUtility.getColors(ThemeSettings.ranOutOfTime())[1]);

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
    }

}
