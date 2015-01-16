package utt.fr.if26.project.support;

public class Keys {

	/**
	 * Local storage names
	 */
	public static final String FILE_NAME = "sharedPreferences";
	public static final String SAVING_STATE = "savingState";
	public static final String SAVING_MAP_STATE = "savingMapState";

	/**
	 * Type of request method
	 */
	public static final String POST_METHOD = "POST";
	public static final String GET_METHOD = "GET";

	/**
	 * Common parameters
	 */
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String IMG = "img";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String MESSAGE = "message";
	public static final String MESSAGES = "messages";
	public static final String TOKEN = "token";
	public static final String ERROR = "error";
	public static final String FRIENDS = "friends";
	public static final String CONTACTS = "contacts";
	public static final String CONTACT = "contact";
	public static final String LONGITUDE = "longtitude";
	public static final String LATITUDE = "latitude";
	public static final String DEFAULT_LONGITUDE = "-1";
	public static final String DEFAULT_LATITUDE = "-1";
	public static final String SENT = "sent";
	public static final String DATE = "date";
	public static final String REFUSED_ERROR = "error_msg";
	public static final String INDICATOR = "indicator";
	public static final String EMPTY_MESSAGE = "";
	public static final String FRIENDS_LIST = "friend_list";

	/**
	 * Kilometer unit
	 */
	public static final int METERS_PER_KILOMETER = 1000;

	/**
	 * Type of friends
	 */
	public static final int ALREADY_FRIEND = 0;
	public static final int INVITING_FRIEND = 1;
	public static final int NOT_YET_FRIEND = 2;
	public static final int REQUESTED_FRIEND = 3;
	public static final int NOT_AFFECTED = -1;

	/**
	 * Web services
	 */
	public static final String ROOT = "http://projetif26.byethost7.com/";
	public static final String LOGIN_URL = ROOT + "login.php";
	public static final String REGISTRATION_URL = ROOT + "createAccount.php";
	public static final String INVITATIONS_URL = ROOT + "getInvitations.php";
	public static final String ACCEPT_INVITATION_URL = ROOT
			+ "acceptFriend.php";
	public static final String REFUSE_INVITATION_URL = ROOT
			+ "refuseFriend.php";
	public static final String SEND_REQUEST_URL = ROOT + "listSentRequests.php";
	public static final String ALREADY_FRIENDS_URL = ROOT + "getFriends.php";
	public static final String UPDATE_LOCATION_URL = ROOT
			+ "updateLocation.php";
	public static final String GET_LOCATION_URL = ROOT + "getLocations.php";
	public static final String SEND_REQUEST = ROOT + "requestFriend.php";
	public static final String GET_CONTACTS_URL = ROOT + "contacts.php";
	public static final String GET_MESSAGES_URL = ROOT + "messages.php";
	public static final String GET_MESSAGE_URL = ROOT + "message.php";
	public static final String CREATE_CONTACT = ROOT + "createContact.php";

}
