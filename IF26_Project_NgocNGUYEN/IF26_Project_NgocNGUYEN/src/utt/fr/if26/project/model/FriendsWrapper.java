package utt.fr.if26.project.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class FriendsWrapper implements Serializable {

	private ArrayList<User> users;

	public FriendsWrapper(ArrayList<User> data) {
		this.setUsers(data);
	}

	public ArrayList<User> getUsers() {
		return this.users;
	}

	public void setUsers(ArrayList<User> data) {
		this.users = new ArrayList<User>();
		for (User user : data) {
			this.users.add(user);
		}
	}
}
