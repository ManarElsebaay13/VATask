package eg.com.test.vatask.model.dp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.com.test.vatask.model.entities.QuestionsModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface DAO {
    @Query("SELECT * FROM QuestionsModel ORDER BY id DESC")
    fun getQuestionModels(): Flowable<List<QuestionsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg questionModel: QuestionsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestionModel(questionModel: QuestionsModel): Completable
}