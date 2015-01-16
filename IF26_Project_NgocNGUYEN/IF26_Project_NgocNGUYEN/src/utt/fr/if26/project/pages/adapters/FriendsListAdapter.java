package utt.fr.if26.project.pages.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.User;
import utt.fr.if26.project.pages.LoginPage;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsListAdapter extends BaseAdapter {

	// Declare Variables
	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<User> friendsList;
	private int nbTypes;

	public FriendsListAdapter(Context context, ArrayList<User> friendsList,
			int nbTypes) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(this.context);
		this.friendsList = new ArrayList<User>();
		for (User user : friendsList) {
			this.friendsList.add(user);
		}
		this.nbTypes = nbTypes;
	}

	@Override
	public int getCount() {
		if (this.friendsList.size() > 0) {
			return this.friendsList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return this.friendsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final User user = this.friendsList.get(position);
		// final int status = user.getStatus();
		final ViewHolder holder;

		switch (getItemViewType(position)) {

		case Keys.ALREADY_FRIEND:
			if (convertView == null) {
				convertView = this.layoutInflater.inflate(
						R.layout.friends_already_row_list, parent, false);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.friendAvatar);
				holder.title = (TextView) convertView
						.findViewById(R.id.friendName);
				holder.indicatorText = (TextView) convertView
						.findViewById(R.id.indicatorText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(user.getName());
			holder.indicatorText.setText(this.context
					.getText(R.string.already_friend));
			if (holder.imageView != null) {
				holder.imageView.setBackgroundResource(R.drawable.avatar);
			}

			break;

		case Keys.INVITING_FRIEND:
			if (convertView == null) {
				convertView = this.layoutInflater.inflate(
						R.layout.friends_invitation_row_list, parent, false);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.friendAvatar);
				holder.title = (TextView) convertView
						.findViewById(R.id.friendName);

				holder.acceptBtn = (Button) convertView
						.findViewById(R.id.acceptBtn);
				holder.acceptBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									new AcceptFriends(context,
											Keys.ACCEPT_INVITATION_URL, user,
											position).execute();
									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage(context.getText(R.string.yesno))
								.setPositiveButton(
										context.getText(R.string.yes),
										dialogClickListener)
								.setNegativeButton(
										context.getText(R.string.no),
										dialogClickListener)
								.setTitle(context.getText(R.string.alert))
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					}
				});

				holder.refuseBtn = (Button) convertView
						.findViewById(R.id.refuseBtn);
				holder.refuseBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									new RefuseFriends(context,
											Keys.REFUSE_INVITATION_URL, user,
											position).execute();
									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage(context.getText(R.string.yesno))
								.setPositiveButton(
										context.getText(R.string.yes),
										dialogClickListener)
								.setNegativeButton(
										context.getText(R.string.no),
										dialogClickListener)
								.setTitle(context.getText(R.string.alert))
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					}
				});

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(user.getName());
			if (holder.imageView != null) {
				holder.imageView.setBackgroundResource(R.drawable.avatar);
			}

			break;

		case Keys.NOT_YET_FRIEND:
			if (convertView == null) {
				convertView = this.layoutInflater.inflate(
						R.layout.friends_notyet_row_list, parent, false);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.friendAvatar);
				holder.title = (TextView) convertView
						.findViewById(R.id.friendName);

				holder.addBtn = (Button) convertView.findViewById(R.id.addBtn);
				holder.addBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									new SendRequestFriends(context,
											Keys.SEND_REQUEST, user, holder)
											.execute();
									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage(context.getText(R.string.yesno))
								.setPositiveButton(
										context.getText(R.string.yes),
										dialogClickListener)
								.setNegativeButton(
										context.getText(R.string.no),
										dialogClickListener)
								.setTitle(context.getText(R.string.alert))
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					}
				});

				holder.indicatorText = (TextView) convertView
						.findViewById(R.id.indicatorText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(user.getName());
			holder.indicatorText.setVisibility(View.INVISIBLE);
			holder.indicatorText.setText(this.context
					.getText(R.string.requested_friend));
			if (holder.imageView != null) {
				holder.imageView.setBackgroundResource(R.drawable.avatar);
			}

			break;

		case Keys.REQUESTED_FRIEND:
			if (convertView == null) {
				convertView = this.layoutInflater.inflate(
						R.layout.friends_ongoing_row_list, parent, false);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.friendAvatar);
				holder.title = (TextView) convertView
						.findViewById(R.id.friendName);
				holder.indicatorText = (TextView) convertView
						.findViewById(R.id.indicatorText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(user.getName());
			holder.indicatorText.setText(this.context
					.getText(R.string.requested_friend));
			if (holder.imageView != null) {
				holder.imageView.setBackgroundResource(R.drawable.avatar);
			}

			break;
		}

		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView indicatorText;
		Button addBtn;
		Button acceptBtn;
		Button refuseBtn;
	}

	@Override
	public int getItemViewType(int position) {
		return this.friendsList.get(position).getStatus();
	}

	@Override
	public int getViewTypeCount() {
		return this.nbTypes;
	}

	private class AcceptFriends extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;
		private User user;
		private int position;

		public AcceptFriends(Context context, String url, User user, int postion) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "",
					this.context.getString(R.string.loading), true, true, null);
			this.url = url;
			this.user = user;
			this.position = postion;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.ID, this.user.getId()));
			// String result = new JSONParser().makeHttpRequest(this.url, "GET",
			// params);
			String result = new JSONParser().makeHttpRequest(this.url,
					Keys.GET, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject jsonObject = new JSONObject(result);

				if (!jsonObject.getBoolean(Keys.ERROR)) {
					Toast.makeText(
							this.context,
							user.getName()
									+ " "
									+ this.context
											.getText(R.string.successAddition),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this.context,
							this.context.getText(R.string.failureAddition),
							Toast.LENGTH_LONG).show();
				}

				friendsList.remove(this.position);
				getFriendsListAdapter().setFriendsList(friendsList);
				getFriendsListAdapter().notifyDataSetChanged();
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	private class RefuseFriends extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;
		private User user;
		private int position;

		public RefuseFriends(Context context, String url, User user, int postion) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "",
					this.context.getString(R.string.loading), true, true, null);
			this.url = url;
			this.user = user;
			this.position = postion;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.ID, this.user.getId()));
			// String result = new JSONParser().makeHttpRequest(this.url, "GET",
			// params);
			String result = new JSONParser().makeHttpRequest(this.url,
					Keys.GET, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject jsonObject = new JSONObject(result);

				if (!jsonObject.getBoolean(Keys.ERROR)) {
					Toast.makeText(
							this.context,
							user.getName()
									+ " "
									+ this.context
											.getText(R.string.successRefusion),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this.context,
							this.context.getText(R.string.failureRefusion),
							Toast.LENGTH_LONG).show();
				}

				friendsList.remove(this.position);
				getFriendsListAdapter().setFriendsList(friendsList);
				getFriendsListAdapter().notifyDataSetChanged();
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	private class SendRequestFriends extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;
		private User user;
		private ViewHolder holder;

		public SendRequestFriends(Context context, String url, User user,
				ViewHolder holder) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "",
					this.context.getString(R.string.loading), true, true, null);
			this.url = url;
			this.user = user;
			this.holder = holder;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.ID, this.user.getId()));
			// String result = new JSONParser().makeHttpRequest(this.url, "GET",
			// params);
			String result = new JSONParser().makeHttpRequest(this.url,
					Keys.GET, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject jsonObject = new JSONObject(result);

				if (!jsonObject.getBoolean(Keys.ERROR)) {
					Toast.makeText(
							this.context,
							user.getName()
									+ " "
									+ this.context
											.getText(R.string.successRefusion),
							Toast.LENGTH_LONG).show();
					this.holder.addBtn.setVisibility(View.INVISIBLE);
					this.holder.indicatorText.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(this.context,
							this.context.getText(R.string.failureRefusion),
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	public ArrayList<User> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(ArrayList<User> friendsList) {
		this.friendsList = friendsList;
	}

	public FriendsListAdapter getFriendsListAdapter() {
		return this;
	}
}
