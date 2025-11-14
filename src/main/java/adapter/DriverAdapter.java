package adapter;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

public class DriverAdapter extends AbstractTableModel {

    private Driver driver;
    private List<Ride> rides;

    private final String[] columnNames = { 
        "From", 
        "To", 
        "Date", 
        "Seats", 
        "Price" 
    };

    public DriverAdapter(Driver driver) {
        this.driver = driver;
        this.rides = driver.getCreatedRides();   // el adaptado
    }

    @Override
    public int getRowCount() {
        return rides.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ride r = rides.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return r.getFrom();
            case 1:
                return r.getTo();
            case 2:
                return r.getDate();
            case 3:
                return r.getnSeats();
            case 4:
                return r.getPrice();
            default:
                return null;
        }
    }
}
