package com.mazimia.mobile.nurselectureroom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

/**
 * Creating a custom recyclerview so that I can get information about
 * position of selected items in it.
*/
public class CustomRecyclerView extends RecyclerView {

    private RecyclerContextMenuInfo contextMenu;
    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return contextMenu;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int longPressPos = getChildAdapterPosition(originalView);
        if (longPressPos >= 0) {
            final long itemId = getAdapter().getItemId(longPressPos);
            contextMenu = new RecyclerContextMenuInfo(longPressPos, itemId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    public static class RecyclerContextMenuInfo implements ContextMenu.ContextMenuInfo {

        final public int position;
        final public long itemId;

        public RecyclerContextMenuInfo(int position, long item) {
            this.position = position;
            this.itemId = item;
        }
    }
}
