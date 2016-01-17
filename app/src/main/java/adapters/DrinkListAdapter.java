package adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.OutPersonDrink;
import se.loco.app.R;
import utils.Globals;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrinkListAdapter extends ArrayAdapter<OutPersonDrink> {

	Context mContext;
	int layoutResourceId;
	List<OutPersonDrink> data = null;

	public DrinkListAdapter(Context mContext, int layoutResourceId,
			List<OutPersonDrink> data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	static class ViewHolder {
		TextView time;
		TextView boozeType;
		TextView quantity;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.txt_time);
			viewHolder.boozeType = (TextView) convertView
					.findViewById(R.id.txt_booze_type);
			viewHolder.quantity = (TextView) convertView
					.findViewById(R.id.txt_quantity);
			convertView.setTag(viewHolder);

		}
		// object item based on the position
		OutPersonDrink objectItem = data.get(position);
		String dateTime = null;

		String input = objectItem.getTime();
		DateFormat inputFormat = new SimpleDateFormat(Globals.dateTimeFormat);
		DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			dateTime = outputFormat.format(inputFormat.parse(input));
			// fill data
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.time.setText(dateTime);
			holder.boozeType.setText(objectItem.getBoozeType());
			holder.quantity.setText(objectItem.getQuantity() + " cl");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		return convertView;

	}

}
