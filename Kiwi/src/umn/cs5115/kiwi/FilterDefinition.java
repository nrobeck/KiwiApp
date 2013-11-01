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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	// set of courses to display, etc etc
}
