package fr.bowo.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
 * Created by vchann on 16/08/2017.
 */

public class StaggeredGridActivity extends AppCompatActivity implements BOWOListListener {

    /*
    Attributes
     */
    protected BOWOListView listView;
    protected final String KEY_FULL_SPAN = "full span";

    /*
    Life cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        listView = (BOWOListView) findViewById(R.id.list_view);
        listView.start(getDataViewList(), getStaggeredGridLayoutManager(2), this);
    }

    @Override
    public void onBindViewHolder(View itemView, BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.cell_title);
        TextView cellText = (TextView) itemView.findViewById(R.id.cell_text);
        ImageView cellImage = (ImageView) itemView.findViewById(R.id.cell_image);

        if (dataView.getBoolean(KEY_FULL_SPAN)) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }
        if (cellTitle != null) {
            cellTitle.setText(dataView.getTitle());
        }
        if (cellText != null) {
            cellText.setText(dataView.getText());
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
        result.add(new BOWODataView(R.layout.cell_grid_header).setImage("pokemon_header.jpg").setBoolean(KEY_FULL_SPAN, true));
        result.add(new BOWODataView(R.layout.cell_staggered_grid)
                .setImage("alakazam.png")
                .setTitle("Alakazam")
                .setText("Inter has ruinarum varietates a Nisibi quam tuebatur accitus Vrsicinus, cui nos obsecuturos iunxerat imperiale praeceptum"));
        result.add(new BOWODataView(R.layout.cell_staggered_grid)
                .setImage("carapuce.png")
                .setTitle("Carapuce")
                .setText("Inter has ruinarum varietates a Nisibi quam tuebatur accitus Vrsicinus"));
        result.add(new BOWODataView(R.layout.cell_staggered_grid)
                .setImage("evoli.png")
                .setTitle("Evoli")
                .setText("Inter has ruinarum varietates a Nisibi quam tuebatur accitus Vrsicinus, cui nos obsecuturos iunxerat imperiale praeceptum"));
        result.add(new BOWODataView(R.layout.cell_staggered_grid)
                .setImage("pikachu.png")
                .setTitle("Pikachu")
                .setText("Inter has ruinarum varietates a Nisibi quam tuebatur accitus Vrsicinus"));
        return result;
    }

    protected StaggeredGridLayoutManager getStaggeredGridLayoutManager(int span) {
        return new StaggeredGridLayoutManager(span, OrientationHelper.VERTICAL);
    }
}
