package com.example.demo

import Direction
import Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Point

@Service
class GameModel {

    private val logger: Logger = LoggerFactory.getLogger(GameModel::class.java)

    private val size = 10
    private val fruitList = mutableListOf<Point>()
    private val players = mutableListOf<Player>()

    private val player1 = Player(1).apply {
        for (y in 0..3) {
            snake.add(Point(1, y))
        }
    }

    private val player2 = Player(2).apply {
        val x = size - 2
        for (y in 0..3) {
            snake.add(Point(x, y))
        }
    }

    init {
        val player1 = Player(1).apply {
            for (y in 0..3) {
                snake.add(Point(1, y + size / 2))
            }
        }

        val player2 = Player(2).apply {
            val x = size - 2
            for (y in 0..3) {
                snake.add(Point(x, y + size / 2 ))
            }
        }
        players.add(player1)
        players.add(player2)
        addFruit()
    }

    private fun randomFruit(): Point {
        val randomX = (0 until size).random()
        val randomY = (0 until size).random()
        return Point(randomX, randomY)
    }

    private final fun addFruit() {
        if (fruitList.size < 5) {
            var fruit = randomFruit()
            while (player1.snake.contains(fruit) || player2.snake.contains(fruit)) {
                fruit = randomFruit()
            }
            fruitList.add(fruit)
        }
    }

    fun isGameOver(newHead: Point): Boolean {
        if (newHead.x !in 0 until size) return true
        if (newHead.y !in 0 until size) return true
        for (player in players) {
            val snake = player.snake
            if (snake.contains(newHead)) return true
        }
        return false
    }

    fun getPlayers(): List<Player> = players.toList()
    fun getFruitList(): List<Point> = fruitList.toList()

    fun movePlayer(playerId: Int, direction: Direction) {
        val player = players.find { it.id == playerId } ?: return

        val head = player.snake.first()

        // Calculate the new head position based on the direction
        val newHead = when (direction) {
            Direction.UP -> Point(head.x, (head.y - 1))
            Direction.DOWN -> Point(head.x, (head.y + 1))
            Direction.LEFT -> Point((head.x - 1), head.y)
            Direction.RIGHT -> Point((head.x + 1), head.y)
        }

        logger.info("Moving player $playerId ($head) $direction to $newHead")

        //todo check for game over
        if (isGameOver(newHead)) {
            logger.info("Game Over")
            return
        }

        // Eat fruit
        val eatenFruit = fruitList.find { it == newHead }
        player.snake.add(0, newHead)
        if (eatenFruit != null) {
            player.score++
            fruitList.remove(eatenFruit)
        } else {
            player.snake.removeLast()
        }
    }

    // Temporary visual method to compare game board to client state
    fun getBoardAsString(): String {
        val board = Array(10) { CharArray(10) { 'O' } }

        // Snakes
        for (player in players) {
            for (segment in player.snake) {
                board[segment.y][segment.x] = "${player.id}".first()
            }
        }

        // Fruit
        for (fruit in fruitList) {
            board[fruit.y][fruit.x] = 'F'
        }

        val stringBuilder = StringBuilder()
        for (row in board) {
            stringBuilder.append(String(row))
            stringBuilder.append('\n')
        }

        return stringBuilder.toString()
    }
}