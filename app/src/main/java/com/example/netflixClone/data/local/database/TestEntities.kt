package com.example.netflixClone.data.local.database

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import kotlinx.coroutines.flow.Flow


@Entity
data class LocalMovie @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var movieId: Long = 0L,

    var title: String,
    var imageUrl: String,
    var percentFinished: Float = 0f,
    var isNetflixOnly: Boolean = false,
    var isTopTen: Boolean = false,

)

@Entity
data class LocalCategory @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 0L,
    var title: String
)

@Entity(
    primaryKeys = ["categoryId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalCategory::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = LocalMovie::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
    ]
)
data class MovieCategoryCrossRefNew constructor(
    var categoryId: Long,
    var movieId: Long
)

data class LocalCategoryWithMovies constructor(
    @Embedded var category: LocalCategory,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "movieId",
        associateBy = Junction(MovieCategoryCrossRefNew::class)
    )
    var movies: List<LocalMovie>
)

@Dao
interface LocalMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: LocalCategory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: LocalMovie): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<LocalMovie>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryWithMovies(category: MovieCategoryCrossRefNew): Long

    @Query("")
    @Transaction
    suspend fun insert(category: LocalCategory, movies: List<LocalMovie>): LongArray? {
        val rv = LongArray(movies.size + 1)
        var ix = 0
        rv[ix++] = insertCategory(category)
        if (rv[ix - 1] > -1) {
            for (t in movies) {
                rv[ix++] = insertMovie(t)
                if (rv[ix - 1] > -1) {
                    insertCategoryWithMovies(MovieCategoryCrossRefNew(rv[0], rv[ix - 1]))
                }
            }
        }
        return rv
    }

    @Transaction
    @Query("SELECT * FROM localCategory")
    fun getCategoriesWithMovies(): Flow<List<LocalCategoryWithMovies>>
}