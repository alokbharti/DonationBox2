package com.example.donationbox.ui.donate;

public class Donor {

    private String donorName;
    private String donorAddress;
    private String donorProductDetails;
    private String donorProductCategory;
    private String donorProductQuality;
    private String donorProductImageUrl;
    private String donorProductClaimedBy;
    private String donorPhoneNumber;
    private int donorPincode;
    private long donatedTimestamp;
    private boolean donorProductIsClaimed;

    public Donor() {
    }

    public Donor(String donorName, String donorAddress, String donorProductDetails, String donorProductCategory, String donorProductQuality, String donorProductImageUrl, String donorProductClaimedBy, String donorPhoneNumber, int donorPincode, long donatedTimestamp, boolean donorProductIsClaimed) {
        this.donorName = donorName;
        this.donorAddress = donorAddress;
        this.donorProductDetails = donorProductDetails;
        this.donorProductCategory = donorProductCategory;
        this.donorProductQuality = donorProductQuality;
        this.donorProductImageUrl = donorProductImageUrl;
        this.donorProductClaimedBy = donorProductClaimedBy;
        this.donorPhoneNumber = donorPhoneNumber;
        this.donorPincode = donorPincode;
        this.donatedTimestamp = donatedTimestamp;
        this.donorProductIsClaimed = donorProductIsClaimed;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(String donorAddress) {
        this.donorAddress = donorAddress;
    }

    public String getDonorProductDetails() {
        return donorProductDetails;
    }

    public void setDonorProductDetails(String donorProductDetails) {
        this.donorProductDetails = donorProductDetails;
    }

    public String getDonorProductCategory() {
        return donorProductCategory;
    }

    public void setDonorProductCategory(String donorProductCategory) {
        this.donorProductCategory = donorProductCategory;
    }

    public String getDonorProductQuality() {
        return donorProductQuality;
    }

    public void setDonorProductQuality(String donorProductQuality) {
        this.donorProductQuality = donorProductQuality;
    }

    public String getDonorProductImageUrl() {
        return donorProductImageUrl;
    }

    public void setDonorProductImageUrl(String donorProductImageUrl) {
        this.donorProductImageUrl = donorProductImageUrl;
    }

    public String getDonorProductClaimedBy() {
        return donorProductClaimedBy;
    }

    public void setDonorProductClaimedBy(String donorProductClaimedBy) {
        this.donorProductClaimedBy = donorProductClaimedBy;
    }

    public String getDonorPhoneNumber() {
        return donorPhoneNumber;
    }

    public void setDonorPhoneNumber(String donorPhoneNumber) {
        this.donorPhoneNumber = donorPhoneNumber;
    }

    public int getDonorPincode() {
        return donorPincode;
    }

    public void setDonorPincode(int donorPincode) {
        this.donorPincode = donorPincode;
    }

    public boolean getDonorProductIsClaimed() {
        return donorProductIsClaimed;
    }

    public void setDonorProductIsClaimed(boolean donorProductIsClaimed) {
        this.donorProductIsClaimed = donorProductIsClaimed;
    }

    public long getDonatedTimestamp() {
        return donatedTimestamp;
    }

    public void setDonatedTimestamp(long donatedTimestamp) {
        this.donatedTimestamp = donatedTimestamp;
    }
}
