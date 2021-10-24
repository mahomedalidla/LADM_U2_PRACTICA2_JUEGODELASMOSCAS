package mx.tecnm.tepic.ladm_u2_practica2_juegodelasmoscas

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

var direccion = true

class Mosca(punteroLienzo: Lienzo) {

    var x = (0..2).random().toFloat()
    var y = (0..25).random().toFloat()

    private var velX = (50..75).random().toInt()
    private var velY = (50..75).random().toInt()

    var moscaIzquierda = BitmapFactory.decodeResource(punteroLienzo.resources, R.drawable.moscaizq)
    var moscaDerecha = BitmapFactory.decodeResource(punteroLienzo.resources, R.drawable.moscaderecha)
    var estado = 200

    var vivo = true

    fun caja(tX:Float,tY:Float): Boolean {
        var x2 = x + moscaIzquierda.width
        var y2 = y + moscaIzquierda.height

        if(tX in x..x2){
            if(tY in y..y2){
                return true
            }
        }
        return false
    }

    fun mover(ancho:Int, alto:Int){
        x+= velX
        y+= velY
        if(x <= -39||x>=ancho){
            velX*=-1
            direccion=!direccion
        }

        else if(y<=-39||y>=alto){
            velY*=-1
        }
    }

    fun pintar(c: Canvas, p: Paint){
        if(direccion) {
            c.drawBitmap(moscaIzquierda, x, y, p)
        } else {
            c.drawBitmap(moscaDerecha, x, y, p)
        }
    }
}