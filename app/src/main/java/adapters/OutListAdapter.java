package adapters;

import java.util.List;

import model.OutPerson;
import model.User;
import se.loco.app.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OutListAdapter extends ArrayAdapter<OutPerson> {

	Context mContext;
	int layoutResourceId;
	List<OutPerson> data = null;

	public OutListAdapter(Context mContext, int layoutResourceId, List<OutPerson> data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	static class ViewHolder {
		TextView name;
		TextView bac;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.txt_name);
			viewHolder.bac = (TextView) convertView.findViewById(R.id.txt_bac);
			convertView.setTag(viewHolder);

		}
		// object item based on the position
		OutPerson objectItem = data.get(position);

		// fill data
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.name.setText(objectItem.getNames());
		holder.bac.setText(objectItem.getBac());

		return convertView;

	}

}
