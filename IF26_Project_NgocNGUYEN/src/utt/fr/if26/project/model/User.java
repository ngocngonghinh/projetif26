package utt.fr.if26.project.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.Keys;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String id;
	private String name;
	private Double longtitude;
	private Double latitude;
	private int status;

	public User(JSONObject userJsonObj) {
		try {
			this.setId(userJsonObj.getString(Keys.ID));
			this.setName(userJsonObj.getString(Keys.NAME));
			this.setLongtitude(userJsonObj.getDouble(Keys.LONGITUDE));
			this.setLatitude(userJsonObj.getDouble(Keys.LATITUDE));
			this.setStatus(Keys.NOT_AFFECTED);
		} catch (JSONException e) {
			ExceptionHandler.getInstance().mHandler(e);
		}
	}

	public static ArrayList<User> getListUsers(JSONArray jArr) {
		ArrayList<User> list = new ArrayList<User>();
		try {
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject obj = jArr.getJSONObject(i);
				User user = new User(obj);
				list.add(user);
			}
		} catch (JSONException e) {
			ExceptionHandler.getInstance().mHandler(e);
		}
		return list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
