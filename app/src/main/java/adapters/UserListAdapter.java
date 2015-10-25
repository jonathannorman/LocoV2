package adapters;

import java.util.List;

import model.User;
import se.loco.app.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserListAdapter   extends ArrayAdapter<User> {

	 Context mContext;
	    int layoutResourceId;
	    List<User> data = null;

	    public UserListAdapter(Context mContext, int layoutResourceId, List<User> data) {

	        super(mContext, layoutResourceId, data);

	        this.layoutResourceId = layoutResourceId;
	        this.mContext = mContext;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {


	        if (convertView == null) {
	            // inflate the layout
	            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
	            convertView = inflater.inflate(layoutResourceId, parent, false);
	        }

	        // object item based on the position
	        User objectItem = data.get(position);

	        // get the TextView and then set the text (item name) and tag (item ID) values
	        TextView txtUser = (TextView) convertView.findViewById(R.id.txt_friend_name);
	        txtUser.setTag(objectItem.getId());
	        txtUser.setText(objectItem.getName());


	        return convertView;

	    }

}
