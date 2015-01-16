package utt.fr.if26.project.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.Keys;

/**
 * Message is an object to encapsulate contact information
 * 
 * @author NNN
 * 
 */
@SuppressWarnings("serial")
public class Contact implements Serializable {
	private int id;
	private String name;
	// The last message
	private Message message;

	public Contact(int id, String name, Message message) {
		this.id = id;
		this.name = name;
		this.message = message;
		message.getShortMessage();
		message.setAvatar(R.drawable.avatar);
	}

	public Contact(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Contact(JSONObject contacts) {
		try {
			JSONObject jsonContact = contacts.getJSONObject(Keys.CONTACT);
			JSONObject fullMessage = null;
			try {
				fullMessage = contacts.getJSONObject(Keys.MESSAGE);
			} catch (Exception e) {
			}
			name = jsonContact.getString(Keys.NAME);
			id = contacts.getInt(Keys.ID);

			if (fullMessage != null) {
				message = new Message(fullMessage).getShortMessage();
				message.getShortMessage();
				message.setAvatar(R.drawable.avatar);
			}
		} catch (JSONException e) {
			ExceptionHandler.getInstance().mHandler(e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
