package utils;

import java.util.ArrayList;
import java.util.List;

import model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestParser {

	List<Request> requestList = new ArrayList<Request>();
	Request request;

	public List<Request> getRequestList(String json) {

		try {
			if (json != null && !json.isEmpty()) {
				JSONArray jsArr = new JSONArray(json);

				for (int i = 0; i < jsArr.length(); i++) {
					request = new Request();
					JSONObject jsonObject = jsArr.getJSONObject(i);

					String names = jsonObject.getString("names");
					String id = jsonObject.getString("id");
					String status = jsonObject.getString("status");

					request.setNames(names);
					request.setId(id);
					request.setStatus(status);

					requestList.add(request);
				}
			}

			return requestList;

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}

}
