import java.awt.Point

enum class Direction(val value: Int) {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    companion object {
        fun fromInt(value: Int): Direction? {
            return entries.find { it.value == value }
        }
    }
}

data class Player(
    val id: Int,
    var score: Int = 0,
    val snake: MutableList<Point> = mutableListOf()
)