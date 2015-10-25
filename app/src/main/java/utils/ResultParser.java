package utils;

import model.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultParser {

	public Result getParsedResults(String json) {
		Result result = new Result();
		try {
			if (json != null && !json.isEmpty()) {
				JSONObject reader = new JSONObject(json);
				result.setError(reader.getString("error"));
				result.setMessage(reader.getString("message"));

			} else {

				setResultToNull(result);
			}

			return result;
		} catch (JSONException e) {
			setResultToNull(result);
			e.printStackTrace();
			return null;
		}
	}

	public void setResultToNull(Result result) {
		result.setError(null);
		result.setMessage(null);

	}
}
