package com.example.demo

import Direction
import Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.awt.Point

data class State(
    val status: Boolean,
    val fruit: List<Point>,
    val snakes: List<Player>
    )

data class Move(
    val playerId: Int,
    val moveDirection: Int
)

@RestController
class SnakeGameController(@Autowired val game: GameModel) {

    private val logger: Logger = LoggerFactory.getLogger(SnakeGameController::class.java)

    private fun currentState() = State(
        true,
        game.getFruitList(),
        game.getPlayers()
        )


    @GetMapping("/state")
    fun getState(): State = currentState()

    @PostMapping("/move")
    fun move(@RequestBody request: Move): State {
        logger.info(request.toString())
        Direction.fromInt(request.moveDirection)?.let { direction ->
            game.movePlayer(request.playerId, direction)
        }
        return currentState()
    }

    @GetMapping("/board")
    fun getBoard(): String = game.getBoardAsString()

    @GetMapping("/players")
    fun getPlayers(): List<Player> = game.getPlayers()

    @GetMapping("/fruit")
    fun getFruit(): List<Point> = game.getFruitList()
}