package utt.fr.if26.project.pages.adapters;

import java.util.ArrayList;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.Contact;
import utt.fr.if26.project.model.Message;
import utt.fr.if26.project.support.ExceptionHandler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsListAdapter extends BaseAdapter {

	// Declare Variables
	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<Contact> list;

	public ContactsListAdapter(Context context, ArrayList<Contact> list) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(this.context);
		this.list = new ArrayList<Contact>();
		for (Contact contact : list) {
			this.list.add(contact);
		}
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(
					R.layout.contacts_row_list, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Contact con = this.list.get(position);
		Message mes = con.getMessage();
		try {
			holder.imageView.setImageResource(mes.getAvatar());
			holder.message.setText(mes.getMessage());
			holder.date.setText(mes.getDatetime());
			holder.name.setText(con.getName());
		} catch (Exception e) {
			ExceptionHandler.getInstance().mHandler(e);
		}

		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
		TextView message, date, name;
	}
}
