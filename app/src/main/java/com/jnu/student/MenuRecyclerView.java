package com.jnu.student;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MenuRecyclerView extends RecyclerView {
    public MenuRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return contextMenuInfo;
    }

    private ContextMenu.ContextMenuInfo contextMenuInfo;

    @Override
    public boolean showContextMenuForChild(View originalView) {
        int pos = getChildAdapterPosition(originalView);
        long itemId = getChildItemId(originalView);
        contextMenuInfo = new AdapterView.AdapterContextMenuInfo(originalView, pos, itemId);
        return super.showContextMenuForChild(originalView);
    }
}
