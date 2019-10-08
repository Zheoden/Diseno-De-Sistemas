package modelo.clases;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import modelo.dtos.Categoria;
import modelo.dtos.Color;
import modelo.dtos.Material;
import modelo.dtos.TipoPrenda;

@Entity
@Table(name = "prenda")
public class Prenda {
	@Id
	@GeneratedValue
	long id;
	@Transient
	String direccionImagen;
	@Enumerated(EnumType.STRING)
	TipoPrenda tipo;
	@Enumerated(EnumType.STRING)
	Material material;
	@Enumerated(EnumType.STRING)
	Color colorPrimario;
	@Enumerated(EnumType.STRING)
	Color colorSecundario;
	Boolean enUso;
	@ManyToMany(mappedBy="prendas")
	List<Guardarropas> guardarropas = new ArrayList<>();

	public Prenda(TipoPrenda tipo, Material material, Color colorPrimario, Color colorSecundario) {
		this(tipo, material, colorPrimario);
		this.setEnUso(false);
		if (colorPrimario != colorSecundario) {
			this.setColorSecundario(colorSecundario);
		} else {
			System.out
					.println("Se intento asignar un color secundario igual al primario. No se realizo dicha asignacion");
		}
	}

	public Prenda(TipoPrenda tipo, Material material, Color colorPrimario) {
		this(tipo, colorPrimario);
		this.setEnUso(false);
		if (this.esMaterialValido(tipo, material)) {
			this.setTela(material);
		} else {
			System.out.println(
					"Se intento crear una prenda con una combinacion Tipo - Material invalida. Se dejo el material como nulo.");
		}
	}

	public Prenda(TipoPrenda tipo, Color colorPrimario) {
		this.setTipo(tipo);
		this.setColorPrimario(colorPrimario);
		this.setEnUso(false);
	}
	
	public Prenda()
	{
		
	}

	public boolean esMaterialValido(TipoPrenda tipo, Material material) {
		return tipo.esMaterialValido(material);
	}

	public Categoria Categoria() {
		return tipo.categoria();
	}

	public TipoPrenda getTipo() {
		return tipo;
	}

	public void setTipo(TipoPrenda tipo) {
		this.tipo = tipo;
	}

	public Color getColorPrimario() {
		return colorPrimario;
	}

	public void setColorPrimario(Color colorPrimario) {
		this.colorPrimario = colorPrimario;
	}

	public Color getColorSecundario() {
		return colorSecundario;
	}

	public void setColorSecundario(Color colorSecundario) {
		this.colorSecundario = colorSecundario;
	}
	
	public Boolean getEnUso() {
		return this.enUso;
	}

	public void setEnUso(Boolean valor) {
		this.enUso = valor;
	}

	public Material getTela() {
		return material;
	}

	public void setTela(Material material) {
		this.material = material;
	}

	public String getDireccionImagen() {
		return direccionImagen;
	}

	public void setDireccionImagen(String direccionImagen) {
		this.direccionImagen = direccionImagen;
	}

	public List<Guardarropas> getGuardarropas(){
		return guardarropas;
	}
	
	public void setGuardarropas(List<Guardarropas> guardarropas) {
		this.guardarropas = guardarropas;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}