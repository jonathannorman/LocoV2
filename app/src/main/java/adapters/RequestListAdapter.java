package adapters;

import java.util.List;

import model.OutPerson;
import model.Request;
import model.User;
import se.loco.app.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RequestListAdapter extends ArrayAdapter<Request> {

	Context mContext;
	int layoutResourceId;
	List<Request> data = null;

	public RequestListAdapter(Context mContext, int layoutResourceId,
			List<Request> data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	static class ViewHolder {
		TextView name;
		TextView pending;
		TextView accept;
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
			viewHolder.pending = (TextView) convertView
					.findViewById(R.id.txt_pending);
			viewHolder.accept = (TextView) convertView
					.findViewById(R.id.txt_accept);
			convertView.setTag(viewHolder);

		}
		// object item based on the position
		Request objectItem = data.get(position);

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.name.setText(objectItem.getNames());
		holder.pending.setVisibility(View.GONE);
		holder.accept.setVisibility(View.GONE);

		if (objectItem.getStatus().equals("0")) {
			// pending

			holder.pending.setVisibility(View.VISIBLE);

		} else {
			// accept
			holder.accept.setVisibility(View.VISIBLE);
		}

		// fill data

		return convertView;

	}

}
