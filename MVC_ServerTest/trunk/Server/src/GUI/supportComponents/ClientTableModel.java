package GUI.supportComponents;

import javax.swing.table.AbstractTableModel;

public class ClientTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3889268520660005400L;

	private String[] columnNames = {"Client","Ping"};
	
	private Object[][] data = {
	};
	
	public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    public ClientTableModel(){
    	super();
    }
    
}
