package net.gotev.uploadservice;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

class BroadcastData implements Parcelable {
    public static final Parcelable.Creator<BroadcastData> CREATOR = new Parcelable.Creator<BroadcastData>() {
        public BroadcastData createFromParcel(Parcel in) {
            return new BroadcastData(in);
        }

        public BroadcastData[] newArray(int size) {
            return new BroadcastData[size];
        }
    };
    private Exception exception;
    private String id;
    private byte[] responseBody;
    private int responseCode;
    private Status status;
    private long totalBytes;
    private long uploadedBytes;

    public enum Status {
        IN_PROGRESS,
        ERROR,
        COMPLETED,
        CANCELLED
    }

    public BroadcastData() {
        this.responseBody = new byte[0];
    }

    public Intent getIntent() {
        Intent intent = new Intent(UploadService.getActionBroadcast());
        intent.putExtra("broadcastData", this);
        return intent;
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(this.id);
        parcel.writeInt(this.status.ordinal());
        parcel.writeSerializable(this.exception);
        parcel.writeLong(this.uploadedBytes);
        parcel.writeLong(this.totalBytes);
        parcel.writeInt(this.responseCode);
        parcel.writeInt(this.responseBody.length);
        parcel.writeByteArray(this.responseBody);
    }

    private BroadcastData(Parcel in) {
        this.responseBody = new byte[0];
        this.id = in.readString();
        this.status = Status.values()[in.readInt()];
        this.exception = (Exception) in.readSerializable();
        this.uploadedBytes = in.readLong();
        this.totalBytes = in.readLong();
        this.responseCode = in.readInt();
        this.responseBody = new byte[in.readInt()];
        in.readByteArray(this.responseBody);
    }

    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.id;
    }

    public BroadcastData setId(String id2) {
        this.id = id2;
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public BroadcastData setStatus(Status status2) {
        this.status = status2;
        return this;
    }

    public Exception getException() {
        return this.exception;
    }

    public BroadcastData setException(Exception exception2) {
        this.exception = exception2;
        return this;
    }

    public long getUploadedBytes() {
        return this.uploadedBytes;
    }

    public BroadcastData setUploadedBytes(long uploadedBytes2) {
        this.uploadedBytes = uploadedBytes2;
        return this;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    public BroadcastData setTotalBytes(long totalBytes2) {
        this.totalBytes = totalBytes2;
        return this;
    }

    public int getProgressPercent() {
        return (int) ((this.uploadedBytes * 100) / this.totalBytes);
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public BroadcastData setResponseCode(int responseCode2) {
        this.responseCode = responseCode2;
        return this;
    }

    public byte[] getResponseBody() {
        return this.responseBody;
    }

    public BroadcastData setResponseBody(byte[] responseBody2) {
        this.responseBody = responseBody2;
        return this;
    }
}
