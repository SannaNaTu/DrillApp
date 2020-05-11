package com.example.drillapp.ContactInformation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drillapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.GridItemViewHolder> implements Filterable {

    private Context context;
    private List<Contact> contacts;
    private List<Contact> contactsFull;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public RecycleAdapter(Context context, List<Contact> contacts) {

        this.context = context;
        this.contacts = contacts;
        this.contactsFull = new ArrayList<>(contacts); {
        }


    }


    @Override

    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        GridItemViewHolder viewHolder = new GridItemViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override

    public void onBindViewHolder(GridItemViewHolder holder, final int position) {
        Contact contact = contacts.get(position);

        holder.firstName.setText(contact.getFirstName()+ " " + contact.getLastName()+ " " + contact.getPhoneNumber());

    }


    @Override

    public int getItemCount() {

        return contacts.size();
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {

        this.mOnItemClickListener = onItemClickListener;

    }


    private void onItemHolderClick(GridItemViewHolder itemHolder) {

        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(null, itemHolder.itemView,

                    itemHolder.getAdapterPosition(), itemHolder.getItemId());

        }

    }

    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView firstName, phoneNumber, emailAddress;
        RecycleAdapter mAdapter;
        CardView cardView;
        LinearLayout cardContent;



        public GridItemViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.recycle_card_view);
            firstName = (TextView) itemView.findViewById(R.id.recycler_card_firstName);

            phoneNumber = (TextView) itemView.findViewById(R.id.recycler_card_phoneNumber);
            emailAddress = (TextView) itemView.findViewById(R.id.recycler_card_emailAddress);

            cardContent = (LinearLayout) itemView.findViewById(R.id.card_content);

            itemView.setOnClickListener(this);
        }


        @Override

        public void onClick(View v) {

            mAdapter.onItemHolderClick(this);

            if(cardContent.getVisibility() == View.GONE)
            {
                cardContent.setVisibility(View.VISIBLE);
            } else {
                cardContent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(contactsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact contact : contactsFull) {
                    if(contact.getFirstName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contact);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
