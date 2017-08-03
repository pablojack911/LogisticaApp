package com.dynamicsoftware.pocho.logistica.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pocho on 17/04/2017.
 */

public class BaseModel implements Parcelable
{
    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>()
    {
        @Override
        public BaseModel createFromParcel(Parcel in)
        {
            return new BaseModel(in);
        }

        @Override
        public BaseModel[] newArray(int size)
        {
            return new BaseModel[size];
        }
    };
    private int id;

    public BaseModel()
    {
    }

    protected BaseModel(Parcel in)
    {
        id = in.readInt();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
    }
}
