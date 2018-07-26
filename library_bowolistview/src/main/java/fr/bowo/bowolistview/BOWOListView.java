package fr.bowo.bowolistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;

import java.util.List;

import fr.bowo.bowolistview.animationtools.AlphaInAnimationAdapter;
import fr.bowo.bowolistview.animationtools.FadeInAnimator;

/**
 * Created by vincentchann on 15/08/2017.
 */

public class BOWOListView extends RecyclerView {

    /*
    Attributes
     */
    protected Context context;
    protected BOWOAdapter bowoAdapter;
    protected LayoutManager layoutManager;

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
        this.layoutManager = layoutManager;
        setLayoutManager(layoutManager);
        this.bowoAdapter = new BOWOAdapter(context, dataViewList, bowoListListener);
        setAdapter(this.bowoAdapter);
    }

    public void start(List<BOWODataView> dataViewList, LayoutManager layoutManager, BOWOListListener bowoListListener, BOWOListOnClickListener bowoListOnClickListener) {
        this.layoutManager = layoutManager;
        setLayoutManager(layoutManager);
        this.bowoAdapter = new BOWOAdapter(context, dataViewList, bowoListListener, bowoListOnClickListener);
        //setAdapter(this.bowoAdapter);

        this.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(bowoAdapter);
        alphaAdapter.setFirstOnly(true);
        alphaAdapter.setDuration(500);
        alphaAdapter.setInterpolator(new OvershootInterpolator(.5f));
        setAdapter(alphaAdapter);
    }

    public void reloadData(List<BOWODataView> dataViewList) {
        if (this.bowoAdapter != null) {
            int offset = this.computeVerticalScrollOffset();
            this.bowoAdapter.reloadData(dataViewList);
            if (layoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) layoutManager).scrollToPositionWithOffset(0, -offset);
            }
            else if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(0, -offset);
            }
            else {
                // TODO
            }
        }
    }

    public void addItems(int startPosition, List<BOWODataView> dataViewList) {
        bowoAdapter.addItems(startPosition, dataViewList);
    }

    public void removeItems(int position, int itemCount) {
        bowoAdapter.removeItems(position, itemCount);
    }

    public BOWOAdapter getAdapter() {
        return bowoAdapter;
    }

    /*
    Private methods
     */
}
