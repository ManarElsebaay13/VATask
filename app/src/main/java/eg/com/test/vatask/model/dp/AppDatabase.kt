package eg.com.test.vatask.model.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import eg.com.test.vatask.model.entities.QuestionsModel

 @Database(entities = [QuestionsModel::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): DAO
}