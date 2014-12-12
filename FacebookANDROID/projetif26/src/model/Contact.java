package model;

import info.androidhive.info.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Message is an object to encapsulate contact information
 * 
 * @author NNN
 *
 */
public class Contact {
	/**
	 * The id of the contact
	 */
	private int id;
	/**
	 * The name of the contact
	 */
	private String name;
	/**
	 * The last message
	 */
	private Message message;

	public Contact(int id, String name, Message message) {
		this.id = id;
		this.name = name;
		this.message = message;
		message.getShortMessage();
		message.setAvatar(R.drawable.avatar);
	}

	public Contact(JSONObject contacts) throws JSONException {
		JSONObject JSONcontact = contacts.getJSONObject("contact");
		JSONObject messagefull = contacts.getJSONObject("message");
		message = new Message(messagefull).getShortMessage();
		name = JSONcontact.getString("first_name") + " "
				+ JSONcontact.getString("last_name");
		id = contacts.getInt("id");
		message.getShortMessage();
		message.setAvatar(R.drawable.avatar);
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
