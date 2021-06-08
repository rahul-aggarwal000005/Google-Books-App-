package com.example.googlebooksappclone;

public class BookItem {

    private final String mTitle;
    private final String mAuthor;
    private final String mDescription;
    private final String mImageUrl;
    private final String mPreviewLink;

    public BookItem(String title, String author, String description, String imageUrl,String previewLink) {
        this.mTitle = title;
        this.mAuthor = author;
        this.mDescription = description;
        this.mImageUrl = imageUrl;
        this.mPreviewLink = previewLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPreviewLink() {
        return mPreviewLink;
    }

}
