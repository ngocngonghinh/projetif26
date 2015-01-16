package utt.fr.if26.project.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.InputValidation;
import utt.fr.if26.project.support.Keys;

/**
 * Message is a class to encapsulate message information/fields
 * 
 * @author NNN
 * 
 */
@SuppressWarnings("serial")
public class Message implements Serializable {
	// The content of the message
	private String message;
	// boolean to determine, who is sender of this message
	private boolean sent;
	// The date time of message
	private String date;
	private int avatar;

	public Message(String message, boolean isMine, String datetime) {
		super();
		this.setMessage(message);
		this.setMine(isMine);
		this.setDatetime(datetime);
		this.setAvatar(R.drawable.avatar);
	}

	public Message(JSONObject fullMessage) {
		try {
			this.message = InputValidation.reverseString(fullMessage.getString(Keys.MESSAGE));
			this.sent = fullMessage.getBoolean(Keys.SENT);
			this.date = new DateTime().convertDate(fullMessage.getString(Keys.DATE));
			this.setAvatar(R.drawable.avatar);
		} catch (JSONException e) {
			ExceptionHandler.getInstance().mHandler(e);
		}
	}

	public Message getShortMessage() {
		if (message.length() >= 25) {
			message = message.substring(0, 25) + "...";
		}

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
