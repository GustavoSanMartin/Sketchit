package ca.gustavo.sketchit.view

interface DrawingListener {
    fun onStartDraw(x: Float, y: Float)
    fun onDraw(x: Float, y: Float)
    fun onEndDraw()
}