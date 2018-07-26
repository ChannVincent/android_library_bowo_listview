package fr.bowo.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.bowo.bowolistview.BOWODataView;
import fr.bowo.bowolistview.BOWOListListener;
import fr.bowo.bowolistview.BOWOListView;

/**
 * Created by vincentchann on 15/08/2017.
 */

public class ListActivity extends AppCompatActivity implements BOWOListListener {

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

        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.addItems(3, getDataViewSmallList());
            }
        });

        findViewById(R.id.remove_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.removeItems(4, listView.getChildCount() - 5);
            }
        });
    }

    @Override
    public void onBindViewHolder(View itemView, BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.cell_title);
        TextView cellSubtitle = (TextView) itemView.findViewById(R.id.cell_subtitle);
        ImageView cellImage = (ImageView) itemView.findViewById(R.id.cell_image);
        if (cellTitle != null) {
            cellTitle.setText(dataView.getTitle());
        }
        if (cellSubtitle != null) {
            cellSubtitle.setText(dataView.getSubtitle());
        }
        if (cellImage != null) {
            Glide.with(this)
                    .load("file:///android_asset/" + dataView.getImage())
                    .into(cellImage);
        }
    }

    /*
    Private methods
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 1").setSubtitle("Subtitle 1").setImage("carapuce.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 2").setSubtitle("Subtitle 2").setImage("evoli.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 3").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 4").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 5").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 6").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 7").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 8").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        return result;
    }

    protected List<BOWODataView> getDataViewSmallList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 1").setSubtitle("Subtitle 1").setImage("carapuce.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 2").setSubtitle("Subtitle 2").setImage("evoli.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 3").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        return result;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
    }
}
