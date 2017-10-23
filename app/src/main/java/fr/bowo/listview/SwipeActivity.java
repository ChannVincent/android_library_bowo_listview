package fr.bowo.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.bowo.bowolistview.BOWODataView;
import fr.bowo.bowolistview.BOWOListListener;
import fr.bowo.bowolistview.BOWOListView;
import fr.bowo.bowolistview.BOWOSwipeLayout;

/**
 * Created by bbrunell on 23/10/2017.
 */

public class SwipeActivity extends AppCompatActivity implements BOWOListListener {

    /*
    Attributes
     */
    protected BOWOListView listView;

    /*
    Life cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (BOWOListView) findViewById(R.id.list_view);
        listView.start(getDataViewList(), getLinearLayoutManager(), this);
    }

    @Override
    public void onBindViewHolder(final View itemView, BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.text);
        if (cellTitle != null) {
            cellTitle.setText(dataView.getTitle());
        }
        if (dataView.getId() == 1 || dataView.getId() == 3) {
            RelativeLayout button = (RelativeLayout) itemView.findViewById(R.id.delete_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BOWOSwipeLayout) itemView).close(true);
                }
            });
        }
    }

    /*
    Private methods
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_swipeable).setTitle("SWipe right edge").setId(1));
        result.add(new BOWODataView(R.layout.cell_not_swipeable).setTitle("Nope").setId(2));
        result.add(new BOWODataView(R.layout.cell_swipeable_left).setTitle("Swipe left edge").setId(3));
        result.add(new BOWODataView(R.layout.cell_swipeable).setTitle("SWipe right edge").setId(1));
        result.add(new BOWODataView(R.layout.cell_not_swipeable).setTitle("Nope").setId(2));
        result.add(new BOWODataView(R.layout.cell_swipeable_left).setTitle("Swipe left edge").setId(3));
        result.add(new BOWODataView(R.layout.cell_swipeable).setTitle("SWipe right edge").setId(1));
        result.add(new BOWODataView(R.layout.cell_not_swipeable).setTitle("Nope").setId(2));
        result.add(new BOWODataView(R.layout.cell_swipeable_left).setTitle("Swipe left edge").setId(3));
        result.add(new BOWODataView(R.layout.cell_swipeable).setTitle("SWipe right edge").setId(1));
        result.add(new BOWODataView(R.layout.cell_not_swipeable).setTitle("Nope").setId(2));
        result.add(new BOWODataView(R.layout.cell_swipeable_left).setTitle("Swipe left edge").setId(3));
        result.add(new BOWODataView(R.layout.cell_swipeable).setTitle("SWipe right edge").setId(1));
        result.add(new BOWODataView(R.layout.cell_not_swipeable).setTitle("Nope").setId(2));
        result.add(new BOWODataView(R.layout.cell_swipeable_left).setTitle("Swipe left edge").setId(3));
        result.add(new BOWODataView(R.layout.cell_swipeable).setTitle("SWipe right edge").setId(1));
        result.add(new BOWODataView(R.layout.cell_not_swipeable).setTitle("Nope").setId(2));
        result.add(new BOWODataView(R.layout.cell_swipeable_left).setTitle("Swipe left edge").setId(3));
        return result;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
    }
}
