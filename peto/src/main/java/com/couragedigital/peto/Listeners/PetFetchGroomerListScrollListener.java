package com.couragedigital.peto.Listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class PetFetchGroomerListScrollListener extends RecyclerView.OnScrollListener{

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page;

    private LinearLayoutManager layoutManager;

    int lastInScreen;

    public PetFetchGroomerListScrollListener(LinearLayoutManager linearLayoutManager, int current_page) {
        this.layoutManager = linearLayoutManager;
        this.current_page = current_page;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        lastInScreen = firstVisibleItem + visibleItemCount;

        if (!loading && (lastInScreen == totalItemCount - 3)) {
            // End has been reached

            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
