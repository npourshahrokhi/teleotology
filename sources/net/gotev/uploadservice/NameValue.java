package net.gotev.uploadservice;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public final class NameValue implements Parcelable {
    public static final Parcelable.Creator<NameValue> CREATOR = new Parcelable.Creator<NameValue>() {
        public NameValue createFromParcel(Parcel in) {
            return new NameValue(in);
        }

        public NameValue[] newArray(int size) {
            return new NameValue[size];
        }
    };
    private static final String NEW_LINE = "\r\n";
    private final Charset US_ASCII;
    private final Charset UTF8;
    private final String name;
    private final String value;

    public NameValue(String name2, String value2) {
        this.US_ASCII = Charset.forName("US-ASCII");
        this.UTF8 = Charset.forName("UTF-8");
        this.name = name2;
        this.value = value2;
    }

    public final String getName() {
        return this.name;
    }

    public final String getValue() {
        return this.value;
    }

    public byte[] getMultipartBytes(boolean isUtf8) throws UnsupportedEncodingException {
        return ("Content-Disposition: form-data; name=\"" + this.name + "\"" + NEW_LINE + NEW_LINE + this.value).getBytes(isUtf8 ? this.UTF8 : this.US_ASCII);
    }

    public boolean equals(Object object) {
        if (!(object instanceof NameValue)) {
            return false;
        }
        NameValue other = (NameValue) object;
        return this.name.equals(other.name) && this.value.equals(other.value);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(this.name);
        parcel.writeString(this.value);
    }

    private NameValue(Parcel in) {
        this.US_ASCII = Charset.forName("US-ASCII");
        this.UTF8 = Charset.forName("UTF-8");
        this.name = in.readString();
        this.value = in.readString();
    }
}
