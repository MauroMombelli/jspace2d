package GUI;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import GUI.supportComponents.ClientTableModel;

public class ClientTable extends JTable {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = -3398837766574954585L;

		public ClientTable(){
			super();			
			TableModel jTable1Model = 										
				new ClientTableModel();
			setModel(jTable1Model);
			setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			setShowVerticalLines(true);
			setAutoCreateRowSorter(true);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
			JTextField tf = new JTextField(); 
			tf.setBorder(BorderFactory.createEmptyBorder()); 
			tf.setBackground( Color.BLACK);
			setDefaultEditor(Object.class, new DefaultCellEditor(tf));
			
			class MyRenderer extends DefaultTableCellRenderer {
				/**
				 * 
				 */
				private static final long serialVersionUID = -983853636351632620L;

				public Component getTableCellRendererComponent(JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) {
				return super.getTableCellRendererComponent(table, value,
				isSelected,
				false, // Always false
				row, column);
				}
			}
			
			setDefaultRenderer(Object.class, new MyRenderer());
		}
		
		@Override  
	    public void changeSelection(int rowIndex, int columnIndex,  
	    		boolean toggle, boolean extend) {  
	         	super.changeSelection(rowIndex, columnIndex, true, false);  
	    }  
		
}
