package net.gotev.uploadservice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class UploadNotificationConfig implements Parcelable {
    public static final Parcelable.Creator<UploadNotificationConfig> CREATOR = new Parcelable.Creator<UploadNotificationConfig>() {
        public UploadNotificationConfig createFromParcel(Parcel in) {
            return new UploadNotificationConfig(in);
        }

        public UploadNotificationConfig[] newArray(int size) {
            return new UploadNotificationConfig[size];
        }
    };
    private boolean autoClearOnSuccess;
    private boolean clearOnAction;
    private Intent clickIntent;
    private String completed;
    private String error;
    private int iconResourceID;
    private String inProgress;
    private boolean ringToneEnabled;
    private String title;

    public UploadNotificationConfig() {
        this.iconResourceID = 17301589;
        this.title = "File Upload";
        this.inProgress = "Upload in progress";
        this.completed = "Upload completed successfully!";
        this.error = "Error during upload";
        this.autoClearOnSuccess = false;
        this.clearOnAction = false;
        this.clickIntent = null;
        this.ringToneEnabled = true;
    }

    public final UploadNotificationConfig setIcon(int resourceID) {
        this.iconResourceID = resourceID;
        return this;
    }

    public final UploadNotificationConfig setTitle(String title2) {
        this.title = title2;
        return this;
    }

    public final UploadNotificationConfig setInProgressMessage(String message) {
        this.inProgress = message;
        return this;
    }

    public final UploadNotificationConfig setErrorMessage(String message) {
        this.error = message;
        return this;
    }

    public final UploadNotificationConfig setCompletedMessage(String message) {
        this.completed = message;
        return this;
    }

    public final UploadNotificationConfig setAutoClearOnSuccess(boolean clear) {
        this.autoClearOnSuccess = clear;
        return this;
    }

    public final UploadNotificationConfig setClearOnAction(boolean clear) {
        this.clearOnAction = clear;
        return this;
    }

    public final UploadNotificationConfig setClickIntent(Intent clickIntent2) {
        this.clickIntent = clickIntent2;
        return this;
    }

    public final UploadNotificationConfig setRingToneEnabled(Boolean enabled) {
        this.ringToneEnabled = enabled.booleanValue();
        return this;
    }

    /* access modifiers changed from: package-private */
    public final int getIconResourceID() {
        return this.iconResourceID;
    }

    /* access modifiers changed from: package-private */
    public final String getTitle() {
        return this.title;
    }

    /* access modifiers changed from: package-private */
    public final String getInProgressMessage() {
        return this.inProgress;
    }

    /* access modifiers changed from: package-private */
    public final String getCompletedMessage() {
        return this.completed;
    }

    /* access modifiers changed from: package-private */
    public final String getErrorMessage() {
        return this.error;
    }

    /* access modifiers changed from: package-private */
    public final boolean isAutoClearOnSuccess() {
        return this.autoClearOnSuccess;
    }

    /* access modifiers changed from: package-private */
    public final boolean isClearOnAction() {
        return this.clearOnAction;
    }

    /* access modifiers changed from: package-private */
    public final boolean isRingToneEnabled() {
        return this.ringToneEnabled;
    }

    /* access modifiers changed from: package-private */
    public final PendingIntent getPendingIntent(Context context) {
        if (this.clickIntent == null) {
            return PendingIntent.getBroadcast(context, 0, new Intent(), 134217728);
        }
        return PendingIntent.getActivity(context, 1, this.clickIntent, 134217728);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        int i;
        int i2;
        int i3 = 1;
        parcel.writeInt(this.iconResourceID);
        parcel.writeString(this.title);
        parcel.writeString(this.inProgress);
        parcel.writeString(this.completed);
        parcel.writeString(this.error);
        if (this.autoClearOnSuccess) {
            i = 1;
        } else {
            i = 0;
        }
        parcel.writeByte((byte) i);
        if (this.clearOnAction) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        parcel.writeByte((byte) i2);
        if (!this.ringToneEnabled) {
            i3 = 0;
        }
        parcel.writeByte((byte) i3);
        parcel.writeParcelable(this.clickIntent, 0);
    }

    private UploadNotificationConfig(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        this.iconResourceID = in.readInt();
        this.title = in.readString();
        this.inProgress = in.readString();
        this.completed = in.readString();
        this.error = in.readString();
        if (in.readByte() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.autoClearOnSuccess = z;
        if (in.readByte() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.clearOnAction = z2;
        this.ringToneEnabled = in.readByte() != 1 ? false : z3;
        this.clickIntent = (Intent) in.readParcelable(Intent.class.getClassLoader());
    }
}
