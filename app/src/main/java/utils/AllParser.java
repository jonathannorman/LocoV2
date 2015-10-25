package utils;

import java.util.ArrayList;
import java.util.List;

import model.All;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllParser {

	List<All> allList = new ArrayList<All>();
	All all;

	public List<All> getAllList(String json) {

		try {
			if (json != null && !json.isEmpty()) {
				JSONArray jsArr = new JSONArray(json);

				for (int i = 0; i < jsArr.length(); i++) {
					all = new All();
					JSONObject jsonObject = jsArr.getJSONObject(i);

					String names = jsonObject.getString("names");
					String id = jsonObject.getString("id");

					all.setNames(names);
					all.setId(id);

					allList.add(all);
				}
			}

			return allList;

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}

}
