package utt.fr.if26.project.pages.adapters;

import java.util.ArrayList;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.Message;
import utt.fr.if26.project.support.ExceptionHandler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * MessageAdapter is a adapter of ListView to show list of messages
 */
public class MessageAdapter extends BaseAdapter {

	public static final int SENDER = 0;
	public static final int RECIPIENT = 1;

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ArrayList<Message> mData;
	private int typeCount = 1;

	private boolean hasSender = false;
	private boolean hasRecipient = false;

	public MessageAdapter(Context context, ArrayList<Message> data) {
		this.mData = data;
		this.mContext = context;

		if (mLayoutInflater == null) {
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}

	public void setData(ArrayList<Message> messagesList) {
		this.mData = messagesList;
		hasSender = false;
		hasRecipient = false;
		for (Message mes : messagesList) {
			if (!mes.isMine()) {
				hasSender = true;

				if (hasSender && hasRecipient) {
					break;
				} else {
					continue;
				}
			} else {
				hasRecipient = true;

				if (hasSender && hasRecipient) {
					break;
				} else {
					continue;
				}
			}
		}

		if (hasRecipient && hasSender) {
			typeCount = 2;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mData == null || mData.size() == 0) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// Get item at specific position
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Get id of specific item
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (getItemViewType(position) == SENDER) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.simplerowsender,
						parent, false);
				viewHolder = new ViewHolder(convertView);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
		} else {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.simplerowrecipient, parent, false);
				viewHolder = new ViewHolder(convertView);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
		}

		Message mes = mData.get(position);
		try {
			viewHolder.imageView.setImageResource(mes.getAvatar());
			viewHolder.message.setText(mes.getMessage());
			viewHolder.date.setText(mes.getDatetime());
		} catch (Exception e) {
			ExceptionHandler.getInstance().mHandler(e);
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView imageView;
		public TextView message, date;

		public ViewHolder(View view) {
			imageView = (ImageView) view.findViewById(R.id.img);
			message = (TextView) view.findViewById(R.id.messages);
			date = (TextView) view.findViewById(R.id.dates);

			view.setTag(this);
		}
	}

	@Override
	public int getViewTypeCount() {
		return typeCount;
	}

	@Override
	public int getItemViewType(int position) {
		Message mes = (Message) getItem(position);
		if (!mes.isMine()) {
			return SENDER;
		}
		return RECIPIENT;
	}

}
