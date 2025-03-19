package com.example.myapplication

import android.media.MediaPlayer
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var acelerometero: Sensor? = null

    private var magnitudPrevia = 0f
    private var ultimoDisparoTiempo = 0L

    private var imagen by mutableStateOf(R.drawable.guante_cargado)
    private var mediaPlayer: MediaPlayer? = null
    private var cargado by mutableStateOf(false)
    private var tirosRestantes by mutableStateOf(4)
    private var statusMensaje by mutableStateOf("")

    private val UMBRAL_MAGNITUD = 6.0f
    private val ENFRIAMIENTO_DISPARO = 400
    private val UMBRAL_DIRECCION = 5.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        acelerometero = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        cargarGuante()

        setContent {
            PrincipalUI(imagen, statusMensaje)
        }
    }

    override fun onResume() {
        super.onResume()
        acelerometero?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val vectorMagnitud = sqrt(x * x + y * y + z * z)
            val diferenciaMagnitud = vectorMagnitud - magnitudPrevia
            val tiempoActual = System.currentTimeMillis()

            // Solo dispara si el movimiento es en el eje Z
            if (diferenciaMagnitud > UMBRAL_MAGNITUD &&
                tiempoActual - ultimoDisparoTiempo > ENFRIAMIENTO_DISPARO &&
                z > UMBRAL_DIRECCION && cargado// Asegura que sea hacia afuera
            ) {
                disparoGuante()
                ultimoDisparoTiempo = tiempoActual
            }

            magnitudPrevia = vectorMagnitud
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    private fun cargarGuante() {
        imagen = R.drawable.guante_cargado
        statusMensaje = "Cargando guante..."
        reproducirSonido(R.raw.carga) {
            statusMensaje = "Listo para disparar, extienda el brazo"
            cargado = true
            tirosRestantes = 4
        }
    }

    private fun disparoGuante() {
        if ( cargado && tirosRestantes > 0) {
            imagen = R.drawable.disparo
            reproducirSonido(R.raw.disparo) {
                tirosRestantes--
                statusMensaje = "Disparo!"

                if (tirosRestantes == 0) {
                    descargaGuante()
                }
            }
        }
    }

    private fun descargaGuante() {
        imagen = R.drawable.guante_descargado
        statusMensaje = "Guante descargado, espere un momento..."
        reproducirSonido(R.raw.descarga) {
            resetGuante()
        }
    }

    private fun resetGuante() {
        cargado = false
        tirosRestantes = 4
        cargarGuante()
    }

    private fun reproducirSonido(soundResId: Int, onComplete: (() -> Unit)? = null) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer?.setOnCompletionListener {
            onComplete?.invoke()
        }
        mediaPlayer?.start()
    }
}

@Composable
fun PrincipalUI(imageRes: Int, statusMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Guante estado",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = statusMessage,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}