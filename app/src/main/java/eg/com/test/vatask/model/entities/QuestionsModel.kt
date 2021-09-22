package eg.com.test.vatask.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class QuestionsModel(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var firstNumber: String? = "",
        var secondNumber: String? = "",
        var answer: String? = "",
        var operator: String? = "",
        var operatorText: String? = "",
        var delayTime: Int = 0,
        var isShowLocation: Boolean? = false,
        var latitude: Double? = 0.0,
        var longitude: Double? = 0.0,
        var holderType: String? = ""
) : Serializable
