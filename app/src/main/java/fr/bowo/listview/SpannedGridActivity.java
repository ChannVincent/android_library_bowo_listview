package fr.bowo.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.bowo.bowolistview.BOWODataView;
import fr.bowo.bowolistview.BOWOListListener;
import fr.bowo.bowolistview.BOWOListView;
import fr.bowo.bowolistview.SpannedGridLayoutManager;

/**
 * Created by vincentchann on 15/08/2017.
 */

public class SpannedGridActivity extends AppCompatActivity implements BOWOListListener {

    /*
    Attributes
     */
    protected BOWOListView listView;
    protected final String KEY_GRID_SPAN_COUNT = "grid_span_count";
    protected final String KEY_GRID_HEIGHT = "grid_height";

    /*
    Life cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        listView = (BOWOListView) findViewById(R.id.list_view);
        listView.start(getDataViewList(), getSpannedGridLayoutManager(3), this);
    }

    @Override
    public void onBindViewHolder(View itemView, BOWODataView dataView) {
        ImageView cellImage = (ImageView) itemView.findViewById(R.id.cell_image);
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
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pokemon_header.jpg").setInt(KEY_GRID_SPAN_COUNT, 3));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("alakazam.png").setInt(KEY_GRID_HEIGHT, 2));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("carapuce.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("evoli.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("roocool.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pokemon_header.jpg").setInt(KEY_GRID_SPAN_COUNT, 2).setInt(KEY_GRID_HEIGHT, 2));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("roocool.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pokemon_header.jpg").setInt(KEY_GRID_SPAN_COUNT, 3).setInt(KEY_GRID_HEIGHT, 2));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("alakazam.png").setInt(KEY_GRID_HEIGHT, 2));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("carapuce.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("evoli.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("pokemon_header.jpg").setInt(KEY_GRID_SPAN_COUNT, 2).setInt(KEY_GRID_HEIGHT, 2));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("roocool.png"));
        result.add(new BOWODataView(R.layout.spanned_cell_grid).setImage("roocool.png"));
        return result;
    }

    protected SpannedGridLayoutManager getSpannedGridLayoutManager(int span) {
        int aspectRatio = 1;
        final List<BOWODataView> dataViewList = getDataViewList();
        SpannedGridLayoutManager result = new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                BOWODataView currentDataView = dataViewList.get(position);
                int width = 1;
                if (currentDataView.getInt(KEY_GRID_SPAN_COUNT) > 0) {
                     width = currentDataView.getInt(KEY_GRID_SPAN_COUNT);
                }
                int height = 1;
                if (currentDataView.getInt(KEY_GRID_HEIGHT) > 0) {
                    height = currentDataView.getInt(KEY_GRID_HEIGHT);
                }
                return new SpannedGridLayoutManager.SpanInfo(width, height);
            }
        }, span, aspectRatio);

        return result;
    }

}
