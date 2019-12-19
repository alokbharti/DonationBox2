package com.alokbharti.donationbox;

import androidx.annotation.RestrictTo;
import androidx.lifecycle.LifecycleObserver;

import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.ObservableSnapshotArray;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface FirebaseHelperAdapter<T> extends ChangeEventListener, LifecycleObserver {
    /**
     * If you need to do some setup before the adapter starts listening for change events in the
     * database, do so it here and then call {@code super.startListening()}.
     */
    void startListening();

    /**
     * Removes listeners and clears all items in the backing {@link FirebaseArray}.
     */
    void stopListening();

    ObservableSnapshotArray<T> getSnapshots();

    T getItem(int position);
}