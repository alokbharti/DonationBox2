package com.example.donationbox.ui.ngo;

public class ProductListForNgo {

    private String productDetails;
    private String productUserName;
    private String productUserAddress;
    private String productQuality;
    private String productImageUrl;
    private boolean isClaimed;
    private int productPincode;

    public ProductListForNgo(String productDetails, String productUserName, String productUserAddress, String productQuality, String productImageUrl, boolean isClaimed, int productPincode) {
        this.productDetails = productDetails;
        this.productUserName = productUserName;
        this.productUserAddress = productUserAddress;
        this.productQuality = productQuality;
        this.isClaimed = isClaimed;
        this.productPincode = productPincode;
        this.productImageUrl = productImageUrl;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductUserName() {
        return productUserName;
    }

    public void setProductUserName(String productUserName) {
        this.productUserName = productUserName;
    }

    public String getProductUserAddress() {
        return productUserAddress;
    }

    public void setProductUserAddress(String productUserAddress) {
        this.productUserAddress = productUserAddress;
    }

    public String getProductQuality() {
        return productQuality;
    }

    public void setProductQuality(String productQuality) {
        this.productQuality = productQuality;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public void setClaimed(boolean claimed) {
        isClaimed = claimed;
    }

    public int getProductPincode() {
        return productPincode;
    }

    public void setProductPincode(int productPincode) {
        this.productPincode = productPincode;
    }
}
