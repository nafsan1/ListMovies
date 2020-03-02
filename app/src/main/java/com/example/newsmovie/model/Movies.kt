package com.example.newsmovie.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseMovie(

    @SerializedName("results")
    @Expose
    val movies: List<Movie>

)
/*@Entity(tableName = "movie_favorite")
data class MovieEntity(

    @ColumnInfo(name = "idMovie")
    @SerializedName("idMovie")
    @Expose
    val idMovie: Int?

){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}*/
@Entity(tableName = "movie_favorite")
data class Movie(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    val id: Int?,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    val title: String?,
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    val release_date: String?,
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    val poster_path: String?,
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    val overview: String?

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(release_date)
        parcel.writeString(poster_path)
        parcel.writeString(overview)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }


}

