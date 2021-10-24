package mx.tecnm.tepic.ladm_u2_practica2_juegodelasmoscas
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

val numeroMoscas  = (80..100).random().toInt()

class Lienzo(p: MainActivity): View(p){
    var principal = p

    var countDownTimer: CountDownTimer? = object : CountDownTimer(60000, 5) {

        override fun onTick(millisUntilFinished: Long) {
            segundos = "${millisUntilFinished / 1000} Seg."
            if(puntajeAlMomento== numeroMoscas){
                result("Terminaste con todas las moscas 🥳")
                this.cancel()    }
            if(millisUntilFinished/1000L==0.toLong()){
                result("Se termino 😪,intentalo de nuevo")}
        }

        override fun onFinish() {
            this.cancel()
        }
    }

    var puntajeAlMomento = 0
    var segundos =""

    var moscas : Array<Mosca> = Array(numeroMoscas) { Mosca(this) }


    init{
        countDownTimer?.start()
        val hilo = Hilo(this)
        hilo.start()
    }

    override fun onDraw(c: Canvas){
        super.onDraw(c)
        val paint = Paint()

        c.drawColor(Color.LTGRAY)
        paint.setColor(Color.BLACK)

        (0 until numeroMoscas).forEach {
            if(moscas[it].estado>0)
                moscas[it].pintar(c, paint)
        }

        paint.textSize = 63f
        paint.setColor(Color.BLACK)
        c.drawText("Moscas muertas: " +puntajeAlMomento.toString(), 50f, 75f, paint)

        paint.textSize = 63f
        c.drawText("Tiempo: "+ segundos, 50f, 150f, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){

            MotionEvent.ACTION_DOWN -> {
                var puntero = 0

                while (puntero <= numeroMoscas-1) {

                    if (moscas[puntero].caja(event.x, event.y)) {
                        if (moscas[puntero].vivo) {
                            puntajeAlMomento++
                            val muerte = BitmapFactory.decodeResource(this.resources, R.drawable.muerte)
                            moscas[puntero].moscaIzquierda = muerte
                            moscas[puntero].moscaDerecha = muerte
                            moscas[puntero].estado = 3
                            moscas[puntero].vivo = false
                        }
                    }
                    puntero++
                }
            }
        }
        invalidate()
        return true
    }

    fun movimiento(){
        (0 until numeroMoscas).forEach {
            if(moscas[it].vivo)
                moscas[it].mover(width, height)
            moscas[it].estado = moscas[it].estado - 1
        }
        invalidate()
    }

    fun result(m: String){
        AlertDialog.Builder(this.context)
            .setTitle(m)
            .setMessage("Moscas Aplastadas: $puntajeAlMomento")
            .setPositiveButton("Volver a iniciar"){p, i ->
                run {
                    val intent = Intent(principal,MainActivity::class.java);
                    startActivity(principal,intent,null)
                    principal.finish()
                }
            }
            .show()
    }

}

class Hilo(var p: Lienzo): Thread(){
    override fun run(){
        super.run()

        while(true){
            sleep(500)
            p.principal.runOnUiThread {
                p.movimiento()
            }
        }
    }
}