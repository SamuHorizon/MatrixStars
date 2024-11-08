package com.dev.matrixstars

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.sqrt
import android.text.InputFilter
import android.text.Spanned


class DecimalInputFilter : InputFilter {
    override fun filter(
        source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int
    ): CharSequence? {
        // Allow empty input, minus sign at the start, or valid decimal numbers
        val regex = Regex("-?\\d*\\.?\\d*")
        val result = dest.toString().substring(0, dstart) + source + dest.toString().substring(dend)
        return if (result.matches(regex)) null else ""
    }
}


class MainActivity : AppCompatActivity() {

    private lateinit var editTextCoeficienteA: EditText
    private lateinit var editTextCoeficienteB: EditText
    private lateinit var editTextCoeficienteC: EditText
    private lateinit var buttonCalcular: Button
    private lateinit var textVertice: TextView
    private lateinit var textRaices: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link UI elements
        editTextCoeficienteA = findViewById(R.id.editTextCoeficienteA)
        editTextCoeficienteB = findViewById(R.id.editTextCoeficienteB)
        editTextCoeficienteC = findViewById(R.id.editTextCoeficienteC)
        buttonCalcular = findViewById(R.id.buttonCalcular)
        textVertice = findViewById(R.id.textVertice)
        textRaices = findViewById(R.id.textRaices)
        lineChart = findViewById(R.id.lineChart)

        // Set click listener for the button
        buttonCalcular.setOnClickListener { calculate() }

        // Get reference to the chart
        val lineChart = findViewById<LineChart>(R.id.lineChart)

// Set axis label colors based on theme
        val isDarkMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES

        val labelColor = if (isDarkMode) android.graphics.Color.WHITE else android.graphics.Color.BLACK

// Set the label color for x and y axes
        lineChart.xAxis.textColor = labelColor
        lineChart.axisLeft.textColor = labelColor
        lineChart.axisRight.textColor = labelColor
    }

    private fun calculate() {
        val a = editTextCoeficienteA.text.toString().toDoubleOrNull()
        val b = editTextCoeficienteB.text.toString().toDoubleOrNull()
        val c = editTextCoeficienteC.text.toString().toDoubleOrNull()

        if (a == null || b == null || c == null || a == 0.0) {
            textVertice.text = "Invalid input or 'a' cannot be zero"
            textRaices.text = ""
            return
        }

        // Calculate vertex
        val xVertex = -b / (2 * a)
        val yVertex = a * xVertex * xVertex + b * xVertex + c
        textVertice.text = "Vertex: (${String.format("%.2f", xVertex)}, ${String.format("%.2f", yVertex)})"

        // Calculate roots
        val discriminant = b * b - 4 * a * c
        textRaices.text = when {
            discriminant > 0 -> {
                val root1 = (-b + sqrt(discriminant)) / (2 * a)
                val root2 = (-b - sqrt(discriminant)) / (2 * a)
                "Roots: ${String.format("%.2f", root1)}, ${String.format("%.2f", root2)}"
            }
            discriminant == 0.0 -> {
                val root = -b / (2 * a)
                "Root: ${String.format("%.2f", root)}"
            }
            else -> "No real roots"
        }

        // Plot the function
        plotFunction(a, b, c)
    }

    private fun plotFunction(a: Double, b: Double, c: Double) {
        val entries = ArrayList<Entry>()
        var x = -10.0
        while (x <= 10.0) {
            val y = a * x * x + b * x + c
            entries.add(Entry(x.toFloat(), y.toFloat()))
            x += 0.1
        }

        // Create a LineDataSet and configure it
        val dataSet = LineDataSet(entries, "Quadratic Function")
        dataSet.color = android.graphics.Color.BLUE
        dataSet.valueTextColor = android.graphics.Color.BLACK
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)

        // Add data to chart
        lineChart.data = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.invalidate() // Refresh the chart
    }
}
