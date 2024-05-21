import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellEditor
import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.Dimension
import javax.swing.event.CellEditorListener
import javax.swing.event.ChangeEvent
import javax.swing.plaf.basic.BasicTreeUI.CellEditorHandler


class TableGUI(val internalTable: interfaces.Table) {

    class RowHeaderTableModel(
        private val rowHeaders: Array<String>,
        private val data: Array<Array<String>>,
        columnNames: Array<String>
    ) : DefaultTableModel(data, columnNames) {
        override fun getValueAt(row: Int, column: Int): Any {
            return if (column == 0) rowHeaders[row] else super.getValueAt(row, column - 1)
        }

        override fun setValueAt(aValue: Any, row: Int, column: Int) {
            if (column != 0) {
                super.setValueAt(aValue, row, column - 1)
            }
        }

        override fun isCellEditable(row: Int, column: Int): Boolean {
            return column != 0 && super.isCellEditable(row, column - 1)
        }

        override fun getColumnCount(): Int {
            return super.getColumnCount() + 1
        }

        override fun getColumnName(column: Int): String {
            return if (column == 0) "Row" else super.getColumnName(column - 1)
        }
    }


    class CustomCellEditor(private val tableModel: RowHeaderTableModel, val internalTable: interfaces.Table) : AbstractCellEditor(), TableCellEditor, KeyListener {
        private val textField = JTextField()
        private var currentRow: Int = -1
        private var currentColumn: Int = -1

        init {
            textField.addKeyListener(this)
        }

        override fun getTableCellEditorComponent(
            table: JTable, value: Any?, isSelected: Boolean, row: Int, column: Int
        ): Component {
            textField.text = internalTable.cells["${'A' + row}$column"]?.formula ?: ""
            currentRow = row
            currentColumn = column

            val l = object : CellEditorListener {
                override fun editingStopped(e: ChangeEvent?) {
                    println("Editing stopped: $currentRow, $currentColumn")
                    val rowName = tableModel.getValueAt(currentRow, 0).toString() // Get the row name
                    val columnName = tableModel.getColumnName(currentColumn) // Get the column name
                    try {
                        println("Entered: ${textField.text} at $rowName, $columnName")
                        internalTable.modifyCell(rowName + columnName, textField.text)
                    } catch (e: RuntimeException) {
                        println(e.message)
                        if (!internalTable.cells.containsKey(rowName + columnName)) {
                            textField.text = ""
                        }

                    }
                    val cell = internalTable.cells[rowName + columnName]
                    if (cell != null) {
                        textField.text = cell.formula
                    } else {
                        textField.text = ""
                    }
                    tableModel.fireTableDataChanged()
                }

                override fun editingCanceled(e: ChangeEvent?) {}
            }
            addCellEditorListener(l)
            return textField
        }

        override fun getCellEditorValue(): Any {
            return textField.text
        }

        // KeyListener methods
        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) {
                stopCellEditing()  // Finish editing on Enter key press \n
            }
        }

        override fun keyReleased(e: KeyEvent) {

        }

        override fun keyTyped(e: KeyEvent) {
            // No action needed
        }

        override fun isCellEditable(event: java.util.EventObject): Boolean {
            // Allow editing only on mouse click
            if (event is java.awt.event.MouseEvent) {
                return event.clickCount == 1
            }
            return false
        }

        override fun shouldSelectCell(event: java.util.EventObject): Boolean {
            return true
        }

        override fun stopCellEditing(): Boolean {
            fireEditingStopped()  // Notify the table that editing has stopped
            return true
        }

        override fun cancelCellEditing() {
            fireEditingCanceled()  // Notify the table that editing has been canceled
        }

        fun getCurrentRow(): Int {
            return currentRow
        }

        fun getCurrentColumn(): Int {
            return currentColumn
        }
    }

    lateinit var tableModel: RowHeaderTableModel

    fun setValueAt(cellRef: String, value: Int) {
        val row: Int = cellRef.first() - 'A'
        val column: Int = cellRef.substring(1).toInt()
        tableModel.setValueAt(value.toString(), row, column)
        tableModel.fireTableDataChanged()
    }

    fun createAndShowGUI() {
        // Create the frame
        val frame = JFrame("Excel-like-editor")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(800, 600)

        // Create the data and column names
        val rowData = Array(16) { Array(10) { "" } }
        val columnNames = Array(10) { "${it + 1}" }
        val rowHeaders = arrayOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P")

        // Create the custom table model
        tableModel = RowHeaderTableModel(rowHeaders, rowData, columnNames)
        val table = JTable(tableModel)

        // Set the custom cell editor for all columns except the row header
        for (i in 1 until table.columnCount) {
            table.columnModel.getColumn(i).cellEditor = CustomCellEditor(tableModel, internalTable)
        }

        // Adjust the table's appearance
        table.autoResizeMode = JTable.AUTO_RESIZE_OFF
        table.columnModel.getColumn(0).preferredWidth = 50

        // Create scroll pane for the table
        val scrollPane = JScrollPane(table)
        scrollPane.preferredSize = Dimension(780, 550)

        frame.add(scrollPane, java.awt.BorderLayout.CENTER)

        frame.pack()
        frame.isVisible = true
    }
}




