/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

package fr.pyjacpp.diakoluo;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewChange {
    public static final short None = 0;              // 00000000
    public static final short ItemChanged = 1;       // 00000001
    public static final short ItemInserted = 2;      // 00000010
    private static final short ItemMoved = 4;         // 00000100
    public static final short ItemRangeChanged = 8;  // 00001000
    private static final short ItemRangeInserted = 16;// 00010000
    private static final short ItemRangeRemoved = 32; // 00100000
    private static final short ItemRemoved = 64;      // 01000000
    private static final short DataSetChanged = 128;  // 10000000

    private int changes;
    private Integer position = null;
    private Integer positionStart = null;
    private Integer positionEnd = null;

    public RecyclerViewChange(int changes) {
        this.changes = changes;
    }

    private boolean changeIsIn(int change) {
        return (changes & change) == change;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Integer positionStart) {
        this.positionStart = positionStart;
    }

    public Integer getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Integer positionEnd) {
        this.positionEnd = positionEnd;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public void apply(RecyclerView.Adapter<?> recyclerViewAdapter) {
        if (changeIsIn(ItemChanged)) {
            recyclerViewAdapter.notifyItemChanged(position);
        } if (changeIsIn(ItemInserted)) {
            recyclerViewAdapter.notifyItemInserted(position);
        } if (changeIsIn(ItemMoved)) {
            recyclerViewAdapter.notifyItemMoved(positionStart, positionEnd);
        } if (changeIsIn(ItemRangeChanged)) {
            recyclerViewAdapter.notifyItemRangeChanged(positionStart, positionEnd - positionStart);
        } if (changeIsIn(ItemRangeInserted)) {
            recyclerViewAdapter.notifyItemRangeInserted(positionStart, positionEnd - positionStart);
        } if (changeIsIn(ItemRangeRemoved)) {
            recyclerViewAdapter.notifyItemRangeRemoved(positionStart, positionEnd - positionStart);
        } if (changeIsIn(ItemRemoved)) {
            recyclerViewAdapter.notifyItemRemoved(positionStart);
        } if (changeIsIn(DataSetChanged)) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
