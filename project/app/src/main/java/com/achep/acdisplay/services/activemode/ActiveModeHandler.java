/*
 * Copyright (C) 2014 AChep@xda <artemchep@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.achep.acdisplay.services.activemode;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Provides a callback when active mode should be started or stopped
 * to satisfy any options or for power-save reason.
 *
 * @author Artem Chepurnoy
 */
public abstract class ActiveModeHandler {

    private final Context mContext;
    private final Callback mCallback;
    private boolean mCreated;

    /**
     * Provides control callback to main service.
     *
     * @author Artem Chepurnoy
     */
    public interface Callback {

        /**
         * Notifies main service to probably start listening to sensors.
         * At this moment calling {@link #isActive()} should return {@code true}.
         *
         * @see #isActive()
         * @see #requestInactive()
         */
        public void requestActive();

        /**
         * Notifies main service to stop listening to sensors.
         * At this moment calling {@link #isActive()} should return {@code false}.
         *
         * @see #isActive()
         * @see #requestActive()
         */
        public void requestInactive();

        public void pingConsumingSensors();

    }

    public ActiveModeHandler(@NonNull Context context, @NonNull Callback callback) {
        mContext = context;
        mCallback = callback;
    }

    /**
     * Same as calling {@code getCallback().requestActive()}.
     *
     * @see Callback#requestActive()
     * @see #getCallback()
     */
    protected void requestActive() {
        getCallback().requestActive();
    }

    /**
     * Same as calling {@code getCallback().requestInactive()}.
     *
     * @see Callback#requestInactive()
     * @see #getCallback()
     */
    protected void requestInactive() {
        getCallback().requestInactive();
    }

    protected void pingConsumingSensors() {
        getCallback().pingConsumingSensors();
    }

    /**
     * @return Callback to control {@link ActiveModeService service}.
     * @see com.achep.acdisplay.services.activemode.ActiveModeHandler.Callback#requestActive()
     * @see com.achep.acdisplay.services.activemode.ActiveModeHandler.Callback#requestInactive()
     */
    @NonNull
    public Callback getCallback() {
        return mCallback;
    }

    /**
     * @return {@link ActiveModeService Service}'s context.
     */
    @NonNull
    public Context getContext() {
        return mContext;
    }

    /**
     * Called by the {@link ActiveModeService} when the service is created.
     */
    public abstract void onCreate();

    /**
     * Called by the {@link ActiveModeService} to notify a Handler
     * that it is no longer used and is being removed.
     * The handler should clean up any resources it holds
     * (threads, registered receivers, etc) at this point.
     */
    public abstract void onDestroy();

    /**
     * @return {@code true} if starting active sensors is fine, {@code false} otherwise.
     */
    public abstract boolean isActive();

    final void create() {
        mCreated = true;
        onCreate();
    }

    final void destroy() {
        onDestroy();
        mCreated = false;
    }

    final boolean isCreated() {
        return mCreated;
    }

}
