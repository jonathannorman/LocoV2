package utils;

import java.util.ArrayList;
import java.util.List;

import model.OutPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutPersonParser {

	List<OutPerson> outPersonList = new ArrayList<OutPerson>();
	OutPerson outPerson;

	public List<OutPerson> getOutPersonList(String json) {

		try {
			if (json != null && !json.isEmpty()) {
				JSONArray jsArr = new JSONArray(json);

				for (int i = 0; i < jsArr.length(); i++) {
					outPerson = new OutPerson();
					JSONObject jsonObject = jsArr.getJSONObject(i);
					String names = jsonObject.getString("names");
					String bac = jsonObject.getString("bac");
					String latitude = jsonObject.getString("latitude");
					String longitude = jsonObject.getString("longitude");
					String friendId = jsonObject.getString("friendId");
					outPerson.setNames(names);
					outPerson.setBac(bac);
					outPerson.setLatitude(latitude);
					outPerson.setLongitude(longitude);
					outPerson.setFriendId(friendId);

					outPersonList.add(outPerson);
				}
			}

			return outPersonList;
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}
}
