package com.example.donationbox.ui.ngo;

import com.example.donationbox.ui.donate.Donor;

public interface ProductClickListener {

    void onClaimListener(Donor donor);
    void onUndoClaimListener(Donor donor);
}
