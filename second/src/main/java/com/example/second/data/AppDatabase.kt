package com.example.second.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.second.Models.EffortType
import com.example.second.Models.Exercise
import com.example.second.Models.TrainingProgram
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    version = 8,
    entities = [TrainingProgram::class, Exercise::class, EffortType::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun programDao() : ProgramDAO
    abstract fun exerciseDao() : ExerciseDAO

    private class ProgramDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            _instance?.let { database ->
                scope.launch {
                    populateDatabase(database.programDao(), database.exerciseDao())
                }
            }
        }

        suspend fun populateDatabase(programDAO: ProgramDAO, exerciseDAO: ExerciseDAO) {
            programDAO.deleteAll()
            exerciseDAO.deleteAll()
            exerciseDAO.deleteEffortTypes()
//
            var effortTypes : MutableList<EffortType> = mutableListOf()
            effortTypes.add(EffortType(0, "none", "#FFFFFF"))
            effortTypes.add(EffortType(1, "rest", "#00FF00"))
            effortTypes.add(EffortType(2, "weak", "#F4511E"))
            effortTypes.add(EffortType(3, "hard", "#FF0000"))
            exerciseDAO.insertManyEffortType(effortTypes)
            var program = TrainingProgram("Test", "#FF0000")
            var id = programDAO.insertProgram(program).toInt()
            var exercises : MutableList<Exercise> = mutableListOf()
            exercises.add(Exercise(id, 3, "run", 10000))
            exercises.add(Exercise(id, 1, "break", 15000))
            exerciseDAO.insertManyExercises(exercises)
            program = TrainingProgram("Test2", "#00FF00")
            id = programDAO.insertProgram(program).toInt()
            exercises = mutableListOf()
            exercises.add(Exercise(id, 2, "run", 10000))
            exerciseDAO.insertManyExercises(exercises)

            program = TrainingProgram("Test3", "#0000FF")
            id = programDAO.insertProgram(program).toInt()
        }
    }
    companion object{
        @Volatile private var _instance : AppDatabase? = null

        fun buildDatabase(context: Context, scope: CoroutineScope?) : AppDatabase {
            return _instance ?: synchronized(this){
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(ProgramDatabaseCallback(scope!!))
                    .build()
                _instance = inst
                return@synchronized inst
            }
        }
    }


}