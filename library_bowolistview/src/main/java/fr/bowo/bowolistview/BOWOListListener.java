package fr.bowo.bowolistview;

import android.view.View;

import fr.bowo.bowolistview.refactor.done.SMADataView;

/**
 * Created by vincentchann on 07/08/16.
 */
public interface BOWOListListener {

    public void onBindViewHolder(View itemView, SMADataView dataView);
}
