package utils;

import java.util.ArrayList;
import java.util.List;

import model.OutPersonDrink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutPersonDrinkParser {

	List<OutPersonDrink> outPersonDrinkList = new ArrayList<OutPersonDrink>();
	OutPersonDrink outPersonDrink;

	public List<OutPersonDrink> getOutPersonDrinkList(String json) {

		try {
			if (json != null && !json.isEmpty()) {
				JSONArray jsArr = new JSONArray(json);

				for (int i = 0; i < jsArr.length(); i++) {
					outPersonDrink = new OutPersonDrink();
					JSONObject jsonObject = jsArr.getJSONObject(i);
					String time = jsonObject.getString("time");
					String boozeType = jsonObject.getString("boozeType");
					String quantity = jsonObject.getString("quantity");
					outPersonDrink.setTime(time);
					outPersonDrink.setBoozeType(boozeType);
					outPersonDrink.setQuantity(quantity);

					if (jsonObject.has("bac")) {
						String bac = jsonObject.getString("bac");
						outPersonDrink.setBac(bac);
					}

					outPersonDrinkList.add(outPersonDrink);

				}
			}

			return outPersonDrinkList;
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}
}
