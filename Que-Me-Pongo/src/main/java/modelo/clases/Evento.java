package modelo.clases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.ArrayList;

@Entity
public class Evento {
	@Id
	@GeneratedValue
	long id;
	private String nombre;
	private String ciudad;
	private Calendar fecha;
	@Transient
	ArrayList<Atuendo> atuendosAceptados = new ArrayList<Atuendo>();
	@Transient
	ArrayList<Atuendo> atuendosMovimientos = new ArrayList<Atuendo>();

	public Evento(String nombreEvento, String ciudad, Calendar fecha) {
		this.nombre = nombreEvento;
		this.ciudad = ciudad;
		this.fecha = fecha;
	}

	public Date diaAnterior() {
		this.fecha.add(Calendar.DATE, -1);
		return this.fecha.getTime();
	}
    
	public  Evento() {
		
	}
	public void iniciar() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		System.out.println("Voy a " + this.getNombre() + " en " + this.getCiudad() + " en la fecha "
				+ dateFormat.format(this.fecha.getTime()));
	}

	public void aceptarAtuendo(Atuendo unAtuendo) {
		unAtuendo.aceptar();
		this.atuendosAceptados.add(unAtuendo);
		this.atuendosMovimientos.add(unAtuendo);
	}

	public void rechazarAtuendo(Atuendo unAtuendo) {
		unAtuendo.rechazar();
		this.atuendosMovimientos.add(unAtuendo);
	}

	public void deshacer() {
		Atuendo unAtuendo = atuendosMovimientos.get(atuendosMovimientos.size() - 1); // necesito el ultimo elemento de
																						// la lista de movimientos
		if (unAtuendo.getAceptado()) { // si fue aceptado lo saco de la lista
			atuendosAceptados.remove(atuendosAceptados.size() - 1); // si fue aceptado lo saco de la lista
			atuendosMovimientos.remove(atuendosMovimientos.size() - 1); // tambien de la lista de movimientos
		} else {
			atuendosMovimientos.remove(atuendosMovimientos.size() - 1); // solo lo saco de la lista de movimientos,
																		// consultar si deberiamos hacer otra cosa
		}
	}

	public ArrayList<Atuendo> getAtuendosAceptados() {
		return this.atuendosAceptados;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAtuendosAceptados(ArrayList<Atuendo> atuendosAceptados) {
		this.atuendosAceptados = atuendosAceptados;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public LocalDate getFechaEvento() {
		return LocalDateTime.ofInstant(this.fecha.toInstant(), this.fecha.getTimeZone().toZoneId()).toLocalDate();
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}