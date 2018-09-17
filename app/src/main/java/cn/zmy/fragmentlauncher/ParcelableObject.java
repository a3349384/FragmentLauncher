package cn.zmy.fragmentlauncher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zmy on 2018/9/17.
 */

public class ParcelableObject implements Parcelable
{
    private int num;

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.num);
    }

    public ParcelableObject()
    {
    }

    protected ParcelableObject(Parcel in)
    {
        this.num = in.readInt();
    }

    public static final Creator<ParcelableObject> CREATOR = new Creator<ParcelableObject>()
    {
        @Override
        public ParcelableObject createFromParcel(Parcel source)
        {
            return new ParcelableObject(source);
        }

        @Override
        public ParcelableObject[] newArray(int size)
        {
            return new ParcelableObject[size];
        }
    };
}
