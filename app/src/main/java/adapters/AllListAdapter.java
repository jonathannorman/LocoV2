package adapters;

import java.util.List;

import model.All;
import se.loco.app.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AllListAdapter extends ArrayAdapter<All> {

	Context mContext;
	int layoutResourceId;
	List<All> data = null;

	public AllListAdapter(Context mContext, int layoutResourceId, List<All> data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	static class ViewHolder {
		TextView names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.names = (TextView) convertView
					.findViewById(R.id.txt_name);
			convertView.setTag(viewHolder);

		}
		// object item based on the position
		All objectItem = data.get(position);

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.names.setText(objectItem.getNames());

		// fill data

		return convertView;

	}

}
