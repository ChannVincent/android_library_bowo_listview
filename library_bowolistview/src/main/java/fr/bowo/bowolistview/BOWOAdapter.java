package fr.bowo.bowolistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by vincentchann on 15/08/2017.
 */

public class BOWOAdapter extends RecyclerView.Adapter<BOWOViewHolder> {

    /*
    Attributes
     */
    protected BOWOListOnClickListener bowoListOnClickListener;
    protected BOWOListListener bowoListListener;
    protected List<BOWODataView> dataViewList;
    protected Context context;
    int position;

    /*
    Constructor
     */
    public BOWOAdapter(Context context, List<BOWODataView> dataViewList, BOWOListListener bowoListListener) {
        this.context = context;
        this.dataViewList = dataViewList;
        this.bowoListListener = bowoListListener;
    }

    public BOWOAdapter(Context context, List<BOWODataView> dataViewList, BOWOListListener bowoListListener,
                       BOWOListOnClickListener bowoListOnClickListener) {
        this.context = context;
        this.dataViewList = dataViewList;
        this.bowoListListener = bowoListListener;
        this.bowoListOnClickListener = bowoListOnClickListener;
    }

    /*
    Life Cycle
     */

    @Override
    public BOWOViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (dataViewList != null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new BOWOViewHolder(inflater.inflate(dataViewList.get(position).resourceViewIdx, parent, false));
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final BOWOViewHolder holder, final int position) {
        if (bowoListListener != null && dataViewList != null) {
            bowoListListener.onBindViewHolder(holder.itemView, dataViewList.get(position));
        }

        if (bowoListOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bowoListOnClickListener.onClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        this.position = position;
        return dataViewList.get(position).resourceViewIdx;
    }

    @Override
    public int getItemCount() {
        if (dataViewList != null) {
            return dataViewList.size();
        }
        else {
            return 0;
        }
    }


    /*
    Public methods
     */
    public void reloadData(List<BOWODataView> dataViews) {
        this.dataViewList = dataViews;
        notifyDataSetChanged();
    }

    public void removeItems(int position, int itemCount) {

        int removeCount = 0;
        for (int i = 0; i < itemCount; i++) {
            if (position < dataViewList.size()) {
                dataViewList.remove(position);
                removeCount++;
            }
        }
        notifyItemRangeRemoved(position, removeCount);
    }

    public void addItems(int startPosition, List<BOWODataView> dataChildViewList) {
        if (startPosition > dataViewList.size()) {
            startPosition = dataViewList.size();
        }
        int position = startPosition;
        for (BOWODataView data : dataChildViewList) {
            dataViewList.add(position, data);
            position++;
        }
        notifyItemRangeInserted(startPosition , dataChildViewList.size());
    }
}

