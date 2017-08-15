package fr.bowo.bowolistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by vincentchann on 15/08/2017.
 */

public class BOWOAdapter extends RecyclerView.Adapter<BOWOViewHolder> {

    /*
    Attributes
     */
    protected BOWOListListener bowoListListener;
    protected List<BOWODataView> dataViewList;
    protected Context context;

    /*
    Constructor
     */
    public BOWOAdapter(Context context, List<BOWODataView> dataViewList, BOWOListListener bowoListListener) {
        this.context = context;
        this.dataViewList = dataViewList;
        this.bowoListListener = bowoListListener;
    }

    /*
    Life Cycle
     */
    @Override
    public BOWOViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        if (dataViewList != null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new BOWOViewHolder(inflater.inflate(dataViewList.get(position).resourceViewIdx, parent, false));
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(BOWOViewHolder holder, int position) {
        if (bowoListListener != null && dataViewList != null) {
            bowoListListener.onBindViewHolder(holder.itemView, dataViewList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
}
