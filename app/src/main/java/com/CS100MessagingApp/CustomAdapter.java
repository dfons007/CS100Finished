package com.CS100MessagingApp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class CustomAdapter extends BaseAdapter
{
    Activity activity;
    List<UserModel> users;
    LayoutInflater inflater;

    // Short to create constructor using Alt+Insert for Windows


    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    public CustomAdapter(GroupChat activity, List<UserModel> users) {
        // I don't know how to fix this error. I'm not really sure what it wants.
        this.activity = activity;
        this.users = users;

        inflater = activity.getlayoutInflator();
    }

    @Override
    public int getCount()
    {
        return users.size();
    }

    @Override
    public Object getItem(int i)
    {
        return i;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if(view == null)
        {
            // I don't know how to fix this error either. Once again, I don't know what it wants.
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);

            holder = new ViewHolder();

            holder.tvUserName = (TextView)view.findViewById(R.id.tv_user_name);
            holder.ivCheckBox = (ImageView)view.findViewById(R.id.iv_check_box);

            view.setTag(holder);
        } else
            holder = (ViewHolder)view.getTag();

        UserModel model = users.get(i);

        holder.tvUserName.setText(model.getUserName());

        if(model.isSelected())
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);

        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.unchecked);

        return view;
    }

    public void updateRecords(List<UserModel> users)
    {
        this.users = users;

        notifyDataSetChanged();
    }


    class ViewHolder
    {
        TextView tvUserName;
        ImageView ivCheckBox;
    }
}
