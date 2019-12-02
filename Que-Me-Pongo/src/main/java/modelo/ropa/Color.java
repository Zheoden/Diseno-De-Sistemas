package modelo.ropa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "color")
public class Color {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nombre;
	
	public Color() {}
	
	public Color(String unColor) {
		this.nombre = unColor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombreColor(String nombreColor) {
		this.nombre = nombreColor;
	}
}