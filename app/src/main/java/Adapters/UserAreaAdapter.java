package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.CS100MessagingApp.R;

import java.util.ArrayList;

public class UserAreaAdapter extends ArrayAdapter<String> {

    public UserAreaAdapter(Context context, ArrayList<String> usersinfo)
    {
        super(context,0, usersinfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        String user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewlayout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
        // Populate the data into the template view using the data object
        tvName.setText(user);
        // Return the completed view to render on screen
        return convertView;

    }






}


