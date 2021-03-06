package fr.bowo.listview;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.bowo.bowolistview.BOWODataView;
import fr.bowo.bowolistview.BOWOListListener;
import fr.bowo.bowolistview.BOWOListView;
import fr.bowo.bowolistview.BOWOViewHolder;

/**
 * Created by bbrunell on 20/10/2017.
 */

public class ExpandableActivity extends AppCompatActivity implements BOWOListListener {
    BOWOListView listView;
    protected final String KEY_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (BOWOListView) findViewById(R.id.list_view);
        listView.start(getDataViewList(), getLinearLayoutManager(), this);

    }
    @Override
    public void onBindViewHolder(View itemView, final BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.list_item_child_name);
        if (cellTitle != null) {
            cellTitle.setText(dataView.getTitle());
        }
        if (dataView.getInt(KEY_POSITION) > -1) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<BOWODataView> result = new ArrayList<>();
                    result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("new 1").setId(3).setInt(KEY_POSITION, -1));
                    result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("New 2").setId(3).setInt(KEY_POSITION, -1));
                    result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("NEw 3").setId(3).setInt(KEY_POSITION, -1));
                    listView.addItems(dataView.getId() + 1, result);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listView.removeItems(dataView.getId() + 1, 3);
                    return true;
                }
            });
        }
    }

    /*
    Private methods
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 1").setId(0).setInt(KEY_POSITION, 0));
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 2").setId(1).setInt(KEY_POSITION, 1));
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 3").setId(2).setInt(KEY_POSITION, 2));
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 4").setId(3).setInt(KEY_POSITION, 3));
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 5").setId(4).setInt(KEY_POSITION, 4));
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 6").setId(5).setInt(KEY_POSITION, 5));
        result.add(new BOWODataView(R.layout.cell_expandable_child).setTitle("Title 7").setId(6).setInt(KEY_POSITION, 6));
        result.add(new BOWODataView(R.layout.cell_expandable_parent).setTitle("Title 8").setId(7).setInt(KEY_POSITION,7));
        result.add(new BOWODataView(R.layout.cell_expandable_parent).setTitle("Title 9").setId(8).setInt(KEY_POSITION, 8));
        return result;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this);//new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
    }
}
