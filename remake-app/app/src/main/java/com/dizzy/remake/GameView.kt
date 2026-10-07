package com.dizzy.remake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.dizzy.remake.core.*

class GameView(context: Context) : View(context) {
    private val clock = FixedStepClock()
    private val input = InputState()
    private val world = World(
        Room(
            0,
            2048f,
            240f,
            listOf(Solid(com.dizzy.remake.core.RectF(0f, 220f, 2048f, 20f)))
        ),
        Player(128f, 180f)
    )
    private val engine = Engine(world, input)
    private val viewport = Viewport()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var running = false

    private val frame = object : Runnable {
        override fun run() {
            if (!running || !isAttachedToWindow) return
            val now = System.nanoTime()
            clock.advance(now) { engine.tick() }
            invalidate()
            postOnAnimation(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        running = true
        clock.reset(System.nanoTime())
        removeCallbacks(frame)
        postOnAnimation(frame)
    }

    override fun onDetachedFromWindow() {
        running = false
        removeCallbacks(frame)
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width <= 0 || height <= 0) return

        viewport.follow(world.player.x, world.player.y, world.room, width, height)
        val sx = width.toFloat() / viewport.width
        val sy = height.toFloat() / viewport.height

        canvas.drawColor(Color.rgb(25, 35, 55))
        paint.color = Color.rgb(58, 104, 61)
        canvas.drawRect(0f, 220f * sy, width.toFloat(), height.toFloat(), paint)

        paint.color = Color.WHITE
        canvas.drawOval(
            (world.player.x - viewport.x) * sx,
            (world.player.y - viewport.y) * sy,
            (world.player.x - viewport.x + 24f) * sx,
            (world.player.y - viewport.y + 24f) * sy,
            paint
        )

        paint.textSize = 18f * resources.displayMetrics.density
        canvas.drawText(
            "Dizzy • 50 Hz • szeroki świat",
            20f,
            32f * resources.displayMetrics.density,
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val down = event.actionMasked != MotionEvent.ACTION_UP &&
            event.actionMasked != MotionEvent.ACTION_CANCEL

        if (!down) {
            input.left = false
            input.right = false
            input.a = false
            return true
        }

        input.left = event.x < width * 0.25f
        input.right = event.x >= width * 0.25f && event.x < width * 0.5f
        input.a = event.x > width * 0.72f
        return true
    }
}
