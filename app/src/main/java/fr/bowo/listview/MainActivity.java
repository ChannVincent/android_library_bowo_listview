package fr.bowo.listview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.bowo.bowolistview.BOWODataView;
import fr.bowo.bowolistview.BOWOListListener;
import fr.bowo.bowolistview.BOWOListView;

public class MainActivity extends AppCompatActivity implements BOWOListListener {

    /*
    Attributes
     */
    protected BOWOListView listView;
    protected final int LIST_VIEW_BUTTON_IDX = 1;
    protected final int GRID_VIEW_BUTTON_IDX = 2;
    protected final int STAGGERED_GRID_VIEW_BUTTON_IDX = 3;
    protected final int SWIPE_RECYCLER_VIEW_IDX = 4;
    protected final int EXPENDABLE_RECYCLER_VIEW_IDX = 5;
    protected final int SPANNED_GRID_VIEW_VIEW_IDX = 6;

    /*
    Life cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (BOWOListView) findViewById(R.id.list_view);
        listView.start(getDataViewList(), new LinearLayoutManager(this, OrientationHelper.VERTICAL, false), this);
    }

    @Override
    public void onBindViewHolder(View itemView, final BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.cell_title);
        Button cellButton = (Button) itemView.findViewById(R.id.cell_button);

        if (cellTitle != null) {
            cellTitle.setText(dataView.getTitle());
        }

        if (cellButton != null) {
            cellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (dataView.getId()) {
                        case LIST_VIEW_BUTTON_IDX:
                            Intent intentList = new Intent(MainActivity.this, ListActivity.class);
                            startActivity(intentList);
                            break;

                        case GRID_VIEW_BUTTON_IDX:
                            Intent intentGrid = new Intent(MainActivity.this, GridActivity.class);
                            startActivity(intentGrid);
                            break;

                        case STAGGERED_GRID_VIEW_BUTTON_IDX:
                            Intent intentStaggeredGrid = new Intent(MainActivity.this, StaggeredGridActivity.class);
                            startActivity(intentStaggeredGrid);
                            break;

                        case SWIPE_RECYCLER_VIEW_IDX:
                            Intent intentSwipeRecycler = new Intent(MainActivity.this, SwipeActivity.class);
                            startActivity(intentSwipeRecycler);
                            break;

                        case EXPENDABLE_RECYCLER_VIEW_IDX:
                            Intent intentRecycler = new Intent(MainActivity.this, ExpandableActivity.class);
                            startActivity(intentRecycler);
                            break;

                        case SPANNED_GRID_VIEW_VIEW_IDX:
                            Intent intentSpannedGrid = new Intent(MainActivity.this, SpannedGridActivity.class);
                            startActivity(intentSpannedGrid);
                            break;

                        default:
                            Toast.makeText(MainActivity.this, "No activity associated with this button", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /*
    Private methods
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_main).setTitle("ListView Demo").setId(LIST_VIEW_BUTTON_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("GridView Demo").setId(GRID_VIEW_BUTTON_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("StaggeredGridView Demo").setId(STAGGERED_GRID_VIEW_BUTTON_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("SwipeRecycler Demo").setId(SWIPE_RECYCLER_VIEW_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("ExpendableRecycler Demo").setId(EXPENDABLE_RECYCLER_VIEW_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("Spanned GridView Demo").setId(SPANNED_GRID_VIEW_VIEW_IDX));

        return result;
    }
}
