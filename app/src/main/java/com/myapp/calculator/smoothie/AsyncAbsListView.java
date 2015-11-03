/*
 * Copyright (C) 2012 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myapp.calculator.smoothie;

/**
 * <p>The interface for an {@link android.widget.AbsListView AbsListView}
 * that can have an {@link ItemManager} associated with it. This is
 * implemented by {@link AsyncListView} and {@link AsyncGridView}.</p>
 *
 * @author Lucas Rocha <lucasr@lucasr.org>
 */
public interface AsyncAbsListView {

    /**
     * Sets an {@link ItemManager} to the view.
     *
     * @param itemManager - The {@link ItemManager}.
     */
    public void setItemManager(ItemManager itemManager);
}
