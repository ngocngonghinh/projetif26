package model;

import info.androidhive.info.R;

import org.json.JSONException;
import org.json.JSONObject;

import controler.InputValidation;

/**
 * Message is a class to encapsulate message information/fields
 * 
 * @author NNN
 *
 */
public class Message {
	/**
	 * The content of the message
	 */
	private String message;
	/**
	 * boolean to determine, who is sender of this message
	 */
	private boolean sent;
	/**
	 * the date time of message
	 */
	private String date;
	private int avatar;

	/**
	 * Constructor to make a Message object
	 */
	public Message(String message, boolean isMine, String datetime) {
		super();
		this.message = message;
		this.sent = isMine;
		this.setDatetime(datetime);
		this.setAvatar(R.drawable.avatar);
	}

	public Message(JSONObject messagefull) throws JSONException {
		this.message = InputValidation.reverseString(messagefull
				.getString("message"));
		this.sent = messagefull.getBoolean("sent");
		this.date = new DateTime().convertDate(messagefull.getString("date"));
		this.setAvatar(R.drawable.avatar);

	}

	public Message getShortMessage() {
		if (message.length() >= 25)
			message = message.substring(0, 25) + "...";
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isMine() {
		return sent;
	}

	public void setMine(boolean isMine) {
		this.sent = isMine;
	}

	public String getDatetime() {
		return date;
	}

	public void setDatetime(String datetime) {
		this.date = datetime;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
}
