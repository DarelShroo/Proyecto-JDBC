public class Habitacion {
    private String codHotel;
    private String numHabitacion;
    private int capacidad;
    private int preciodia;
    private int activa;

    public Habitacion() {
    }

    public Habitacion(String codHotel, String numHabitacion, int capacidad, int preciodia, int activa) {
        this.codHotel = codHotel;
        this.numHabitacion = numHabitacion;
        this.capacidad = capacidad;
        this.preciodia = preciodia;
        this.activa = activa;
    }

    public String getCodHotel() {
        return codHotel;
    }

    public String getNumHabitacion() {
        return numHabitacion;
    }


    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPreciodia() {
        return preciodia;
    }

    public void setPreciodia(int preciodia) {
        this.preciodia = preciodia;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }
}
