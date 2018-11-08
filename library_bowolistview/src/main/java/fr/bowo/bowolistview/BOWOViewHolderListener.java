package fr.bowo.bowolistview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vincentchann on 07/08/16.
 */
public interface BOWOViewHolderListener {

    public void onCreateViewHolder(int position, BOWODataView dataView, ViewGroup parent);
}
