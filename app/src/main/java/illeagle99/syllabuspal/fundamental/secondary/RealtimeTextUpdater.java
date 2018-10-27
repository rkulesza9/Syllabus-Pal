package illeagle99.syllabuspal.fundamental.secondary;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kules on 10/25/2016.
 */

public abstract class RealtimeTextUpdater implements TextWatcher {
    private TextView tv;

    public RealtimeTextUpdater(TextView tbu) {
        super();
        tv = tbu;
    }

    public void setTextViewText(String text){
        if(tv != null) tv.setText(text);
    }
    public static void setTextViewText(Activity act,String text,int id){
        TextView tv = (TextView) act.findViewById(id);
        tv.setText(text);
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public abstract void onSave(String text);

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if(tv != null) tv.setText(text);
        onSave(text);
    }
}
