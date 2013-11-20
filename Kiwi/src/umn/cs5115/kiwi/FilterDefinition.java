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
    public static enum SortBy {
    	DUE_DATE(0), COURSE(1), TYPE(2);
    	
    	private final int value;
    	private SortBy(int value) {
    		this.value = value;
    	}
    	public final int toInt() {
    		return value;
    	}
    	public static final SortBy fromInt(int v) {
    		switch (v) {
    		case 0:
    			return DUE_DATE;
    		case 1:
    			return COURSE;
    		default:
    			return TYPE;
    		}
    	}
    }
    
    public FilterDefinition(Parcel in) {
//        i = in.readInt();
//        b = (in.readByte() != 0);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(i);
//        dest.writeByte((byte) (b ? 1 : 0));
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
