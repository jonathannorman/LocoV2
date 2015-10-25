package utils;

import java.util.ArrayList;
import java.util.List;

import model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserListParser {

	List<User> userList = new ArrayList<User>();
	User user;

	public List<User> getUserList(String json) {

		try {
			if (json != null && !json.isEmpty()) {
				JSONArray jsArr = new JSONArray(json);

				for (int i = 0; i < jsArr.length(); i++) {
					user = new User();
					JSONObject jsonObject = jsArr.getJSONObject(i);
					String id = jsonObject.getString("id");
					String name = jsonObject.getString("name");
					String userId = jsonObject.getString("user_id");

					user.setId(id);
					user.setName(name);
					user.setUserId(userId);

					userList.add(user);
				}
			}

			return userList;
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}
}
