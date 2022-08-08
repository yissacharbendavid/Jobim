package il.co.hyperactive.myjobim

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Job(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var employer: String = "",
    var branch: String = "",
    var title: String = "",
    var subtitle: String = "",
    var description: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var isGoodForTeenagers: Boolean = false,
    var filteringQuestion: String = "",
    var filteringRightAnswer: Boolean = false,
    var location: String = "",
    var isHidden: Boolean = false,
    var isFavorite: Boolean = false
)