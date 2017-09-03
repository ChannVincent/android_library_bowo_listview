# BOWO ListView

## Integration

```javascript
// project gradle
allprojects {
  repositories {
    jcenter()
    maven { url 'https://dl.bintray.com/vincentchann/maven-repositories/'}
  }
}

// module gradle
compile 'fr.bowo.bowolistview:library_bowolistview:1.0.0'
```

# Documentation

## BOWOListView

Add BOWOListView in your main layout :
```xml 
<fr.bowo.bowolistview.BOWOListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Identify it in your code and initialize it : 
```java
// see what is getDataViewList() & getLinearLayoutManager() in tutorials
listView = (BOWOListView) findViewById(R.id.list_view);
listView.start(getDataViewList(), getLinearLayoutManager(), this);
```

Make your fragment or activity implement BOWOListListener :
```java
public class ListActivity extends AppCompatActivity implements BOWOListListener {
    @Override
    public void onBindViewHolder(View itemView, BOWODataView dataView) {
        // see tutorials for concrete examples of implementation
    }
}
```

Reload the whole list : 
```java
// pass it a new set of data
listView.reloadData(List<BOWODataView> dataViewList);
```

## BOWODataView
```java
// for conveniant usage you can set common attributes
BOWODataView dataView = new BOWODataView(R.layout.cell_default)
                                    .setTitle("Title")
                                    .setSubtitle("Subtitle")
                                    .setText("long text")
                                    .setImage("image.png")
                                    .setIcon("icon.png")
                                    .setId(1);

// And then you can get them back
String title = dataView.getTitle();
String subtitle = dataView.getSubtitle();
String text = dataView.getText();
String imageName = dataView.getImage();
String iconName = dataView.getIcon();

// for more specific usage you can set generic attributes, but you need keys to get them back
final String KEY_STRING = "key_string";
final String KEY_BOOLEAN = "key_boolean";
final String KEY_INTEGER = "key_integer";
BOWODataView dataView = new BOWODataView(R.layout.cell_default)
                                    .setString(KEY_STRING, "random_string")
                                    .setBoolean(KEY_BOOLEAN, true)
                                    .setInteger(KEY_INTEGER, 3);
                                   
// get them back with their keys
String valueString = dataView.getString(KEY_STRING);
String valueBoolean = dataView.getBoolean(KEY_BOOLEAN);
String valueInteger = dataView.getInteger(KEY_INTEGER);

// And for really generic purpose
final String KEY_OBJECT = "key_object";
BOWODataView dataView = new BOWODataView(R.layout.cell_default).set(KEY_OBJECT, new Object());
Object object = dataView.getObject();
```

# Tutorial

## ListView

**ListViewActivity.java**
```java
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
    }

    /*
    BOWOListListener callback : called each time a cell is drawn 
    itemView : highest parent View of the cell, get all the identifiers from your layout given in BOWODataView
    dataView : get all the details given in @getDataViewList()
    */
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
    Return a list of BOWODataView.
    In each BOWODataView :
    - you must set a layout
    - you can set a title, subtitle, image, ...
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 1").setSubtitle("Subtitle 1").setImage("carapuce.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 2").setSubtitle("Subtitle 2").setImage("evoli.png"));
        result.add(new BOWODataView(R.layout.cell_default).setTitle("Title 3").setSubtitle("Subtitle 3").setImage("pikachu.png"));
        return result;
    }

    /*
    Return the layout manager : manage how cells will align themselves amongst the others
    */
    protected LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
    }
}
```

**cell_default.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="wrap_content">

    <ImageView
        android:id="@+id/cell_image"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:background="#cccccc"/>

    <TextView
        android:id="@+id/cell_title"
        android:textStyle="bold"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="10dp"/>

    <TextView
        android:id="@+id/cell_subtitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="5dp"
        android:layout_marginBottom="10dp"/>

</LinearLayout>
```

## GridView

**GridViewActivity.java**
```java
public class GridActivity extends AppCompatActivity implements BOWOListListener {

    /*
    Attributes
     */
    protected BOWOListView listView;
    protected final String KEY_GRID_SPAN_COUNT = "grid_span_count";

    /*
    Life cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        listView = (BOWOListView) findViewById(R.id.list_view);
        listView.start(getDataViewList(), getGridLayoutManager(3), this);
    }

    /*
    BOWOListListener callback : called each time a cell is drawn 
    */
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
    Return a list of BOWODataView.
    In each BOWODataView :
    - you must set a layout
    - you can set a title, subtitle, image, ...
    - you can set integers, boolean and strings that you can get with keys
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_grid_header).setImage("pokemon_header.jpg").setInt(KEY_GRID_SPAN_COUNT, 3));
        result.add(new BOWODataView(R.layout.cell_grid).setImage("alakazam.png"));
        result.add(new BOWODataView(R.layout.cell_grid).setImage("carapuce.png"));
        result.add(new BOWODataView(R.layout.cell_grid).setImage("evoli.png"));
        result.add(new BOWODataView(R.layout.cell_grid).setImage("pikachu.png"));
        result.add(new BOWODataView(R.layout.cell_grid_header).setImage("pokemon_header.jpg").setInt(KEY_GRID_SPAN_COUNT, 2));
        result.add(new BOWODataView(R.layout.cell_grid).setImage("roocool.png"));
        return result;
    }

    /*
    Return the layout manager : manage how cells will align themselves amongst the others
    Here, I've added a "SpanSizeLookup" listener, to resize cells if some take more than 1 span.
    */
    protected GridLayoutManager getGridLayoutManager(int span) {
        GridLayoutManager result = new GridLayoutManager(this, span);
        
        // SpanSizeLookup : resize cells if they take more than 1 span
        final List<BOWODataView> dataViewList = getDataViewList();
        result.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                BOWODataView currentDataView = dataViewList.get(position);
                if (currentDataView.getInt(KEY_GRID_SPAN_COUNT) > 0) {
                    return currentDataView.getInt(KEY_GRID_SPAN_COUNT);
                }
                else {
                    return 1;
                }
            }
        });
        return result;
    }
}
```

**cell_grid.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.percent.PercentRelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="1dp"
    android:background="#555555">

    <ImageView
        android:id="@+id/cell_image"
        app:layout_aspectRatio="100%"
        app:layout_widthPercent="100%"/>

</android.support.percent.PercentRelativeLayout>
```

## Staggered GridView

**StaggeredGridViewActivity.java**
```java
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

    /*
    BOWOListListener callback : called each time a cell is drawn 
    Here, I handle specific cells that need to be full width and take all the spans available in a row.
    */
    @Override
    public void onBindViewHolder(View itemView, BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.cell_title);
        TextView cellText = (TextView) itemView.findViewById(R.id.cell_text);
        ImageView cellImage = (ImageView) itemView.findViewById(R.id.cell_image);

        // full span cell handled here
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
    Return a list of BOWODataView.
    In each BOWODataView :
    - you must set a layout
    - you can set a title, subtitle, image, ...
    - you can set integers, boolean and strings that you can get with keys
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
    
    /*
    Return the layout manager : manage how cells will align themselves amongst the others
    */
    protected StaggeredGridLayoutManager getStaggeredGridLayoutManager(int span) {
        return new StaggeredGridLayoutManager(span, OrientationHelper.VERTICAL);
    }
}
```

**cell_staggered_grid.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="1dp"
    android:orientation="vertical">

    <android.support.percent.PercentRelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#555555">

        <ImageView
            android:id="@+id/cell_image"
            app:layout_aspectRatio="100%"
            app:layout_widthPercent="100%"/>

    </android.support.percent.PercentRelativeLayout>

    <LinearLayout
        android:padding="15dp"
        android:orientation="vertical"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <TextView
            android:id="@+id/cell_title"
            android:textStyle="bold"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

        <TextView
            android:id="@+id/cell_text"
            android:layout_marginTop="3dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

    </LinearLayout>

</LinearLayout>
```

## OnClickListener

**MainActivity.java**
```java
public class MainActivity extends AppCompatActivity implements BOWOListListener {

    /*
    Attributes
     */
    protected BOWOListView listView;
    protected final int LIST_VIEW_BUTTON_IDX = 1;
    protected final int GRID_VIEW_BUTTON_IDX = 2;
    protected final int STAGGERED_GRID_VIEW_BUTTON_IDX = 3;

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

    /*
    BOWOListListener callback : called each time a cell is drawn 
    Here, set onClickListener and identify each action thanks to its ID defined in @getDataViewList()
    */
    @Override
    public void onBindViewHolder(View itemView, final BOWODataView dataView) {
        TextView cellTitle = (TextView) itemView.findViewById(R.id.cell_title);
        Button cellButton = (Button) itemView.findViewById(R.id.cell_button);

        if (cellTitle != null) {
            cellTitle.setText(dataView.getTitle());
        }

        // Define OnClickListeners depending on ID
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

                        default:
                            Toast.makeText(MainActivity.this, "No activity associated with this button", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /*
    Return a list of BOWODataView.
    In each BOWODataView :
    - set an ID to identify its onClickListener
     */
    protected List<BOWODataView> getDataViewList() {
        List<BOWODataView> result = new ArrayList<>();
        result.add(new BOWODataView(R.layout.cell_main).setTitle("ListView Demo").setId(LIST_VIEW_BUTTON_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("GridView Demo").setId(GRID_VIEW_BUTTON_IDX));
        result.add(new BOWODataView(R.layout.cell_main).setTitle("StaggeredGridView Demo").setId(STAGGERED_GRID_VIEW_BUTTON_IDX));
        return result;
    }
}
```

**cell_main.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="horizontal"
              android:layout_width="match_parent"
              android:layout_height="wrap_content">

    <TextView
        android:text="default text"
        android:id="@+id/cell_title"
        android:layout_weight="2"
        android:layout_width="0dp"
        android:layout_height="100dp"
        android:gravity="center_vertical"
        android:layout_marginLeft="20dp"/>

    <FrameLayout
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="100dp">

        <Button
            android:text="Start"
            android:id="@+id/cell_button"
            android:layout_width="100dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center"/>

    </FrameLayout>

</LinearLayout>
```
