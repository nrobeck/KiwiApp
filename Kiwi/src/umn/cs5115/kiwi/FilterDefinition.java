package umn.cs5115.kiwi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents the state of the filtering/sorting displayed in a fragment.
 * 
 * @author mike
 *
 */
public class FilterDefinition implements Parcelable {
    // TODO: Look up how to do Parcelable
    public int i;
    public boolean b;
    
    public FilterDefinition(Parcel in) {
        i = in.readInt();
        b = (in.readByte() != 0);
    }

    public FilterDefinition() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(i);
        dest.writeByte((byte) (b ? 1 : 0));
    }
    // set of courses to display, etc etc

    public static final Parcelable.Creator<FilterDefinition> CREATOR = new Parcelable.Creator<FilterDefinition>() {
        public FilterDefinition createFromParcel(Parcel in) {
            return new FilterDefinition(in);
        }

        public FilterDefinition[] newArray(int size) {
            return new FilterDefinition[size];
        }
    };
}
