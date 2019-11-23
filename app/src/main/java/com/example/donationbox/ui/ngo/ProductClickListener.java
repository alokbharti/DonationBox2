package com.example.donationbox.ui.ngo;

import com.example.donationbox.ui.donate.Donor;

public interface ProductClickListener {

    void onClaimListener(Donor donor, int position);
    void onUndoClaimListener(Donor donor, int position);
}
