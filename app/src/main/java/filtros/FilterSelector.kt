package filtros

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog


class FilterSelector(private val context: Context) { // Pass the Context for accessing resources

    fun getSelectedFilter(): String? {
        val spinner = Spinner(context) // Create Spinner programmatically
        val filters = arrayOf("Selecione o seu filtro:", "Protanopia", "Deutemaropia", "Tritanopia")
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, filters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // You'll need to show this spinner in a dialog or another UI element
        // Here's a basic example using AlertDialog:

        var selectedFilter: String? = null
        val builder = AlertDialog.Builder(context)
        builder.setView(spinner)
        builder.setTitle("Selecione um filtro")
        builder.setPositiveButton("Aplicar") { dialog, _ ->
            selectedFilter = spinner.selectedItem as? String
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()

        return selectedFilter
    }
}
