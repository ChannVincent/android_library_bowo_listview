package fr.bowo.bowolistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vincentchann on 15/08/2017.
 */

public class BOWOListView extends RecyclerView {

    /*
    Attributes
     */
    protected Context context;
    protected BOWOAdapter bowoAdapter;
    /*
    Constructor
     */
    public BOWOListView(Context context) {
        super(context);
        this.context = context;
    }

    public BOWOListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BOWOListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /*
    Public methods
     */
    public void start(List<BOWODataView> dataViewList, LayoutManager layoutManager, BOWOListListener bowoListListener) {
        setLayoutManager(layoutManager);
        this.bowoAdapter = new BOWOAdapter(context, dataViewList, bowoListListener);
        setAdapter(this.bowoAdapter);
    }

    public void reloadData(List<BOWODataView> dataViewList) {
        if (this.bowoAdapter != null) {
            this.bowoAdapter.reloadData(dataViewList);
        }
    }

    public void addItems(int startPosition, List<BOWODataView> dataViewList) {
        bowoAdapter.addItems(startPosition, dataViewList);
    }

    public void removeItems(int position, int itemCount) {
        bowoAdapter.removeItems(position, itemCount);
    }

    /*
    Private methods
     */
}
