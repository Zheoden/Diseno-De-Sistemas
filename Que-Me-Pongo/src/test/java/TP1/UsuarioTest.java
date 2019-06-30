package TP1;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import modelo.clases.Atuendo;
import modelo.clases.Guardarropas;
import modelo.clases.Prenda;
import modelo.clases.Usuario;
import modelo.enums.Categoria;
import modelo.enums.Color;
import modelo.enums.Material;
import modelo.enums.comportamiento.TipoCalzado;
import modelo.enums.comportamiento.TipoInferior;
import modelo.enums.comportamiento.TipoSuperior;

public class UsuarioTest {
	
	Prenda prenda = new Prenda(TipoSuperior.REMERACORTA, Material.ALGODON, Color.AZUL);
	Prenda prenda1 = new Prenda(TipoInferior.PANTALON, Material.ALGODON, Color.AZUL);
	Prenda prenda2 = new Prenda(TipoCalzado.ZAPATILLAS, Material.ALGODON, Color.AZUL);
	Prenda prenda3 = new Prenda(TipoSuperior.REMERACORTA, Material.ALGODON, Color.ROJO);
	Prenda prenda4 = new Prenda(TipoInferior.PANTALON, Material.ALGODON, Color.ROJO);
	Prenda prenda5 = new Prenda(TipoCalzado.ZAPATILLAS, Material.ALGODON, Color.ROJO);
	ArrayList <Prenda> prendas = new ArrayList <Prenda>();
	ArrayList <Prenda> prendas1 = new ArrayList <Prenda>();

	@Test
	public void crearUsuarioCorrectamente() {
		Guardarropas guardaRopa1 = new Guardarropas(new ArrayList<Prenda>());
		Guardarropas guardaRopa2 = new Guardarropas(new ArrayList<Prenda>());
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		guardaRopas.add(guardaRopa1);
		guardaRopas.add(guardaRopa2);
		Usuario pepe = new Usuario(guardaRopas);
		Assert.assertEquals(pepe.getClass(), Usuario.class);
	}
	
	@Test
	public void verificarGuardarropasTrue() {

		Guardarropas guardaRopa1 = new Guardarropas(prendas);

		prendas1.add(prenda3);
		prendas1.add(prenda4);
		prendas1.add(prenda5);
		Guardarropas guardaRopa2 = new Guardarropas(prendas1);
		
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		guardaRopas.add(guardaRopa1);
		guardaRopas.add(guardaRopa2);
		Usuario pepe = new Usuario(guardaRopas);
		Assert.assertEquals(pepe.verificarGuardarropas(Categoria.PARTE_INFERIOR), true);
	}
	
	@Test
	public void verificarGuardarropasFalse() {
		prendas.add(prenda);
		prendas.add(prenda1);
		prendas.add(prenda2);
		Guardarropas guardaRopa1 = new Guardarropas(prendas);
		
		prendas1.add(prenda3);
		prendas1.add(prenda4);
		prendas1.add(prenda5);
		Guardarropas guardaRopa2 = new Guardarropas(prendas1);
		
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		guardaRopas.add(guardaRopa1);
		guardaRopas.add(guardaRopa2);
		Usuario pepe = new Usuario(guardaRopas);
		Assert.assertEquals(pepe.verificarGuardarropas(Categoria.ACCESORIO), false);
	}
	
	@Test
	public void getGuardarropasConPrenda() {
		prendas.add(prenda);
		prendas.add(prenda2);
		Guardarropas guardaRopa1 = new Guardarropas(prendas);
		
		prendas1.add(prenda4);
		prendas1.add(prenda5);
		Guardarropas guardaRopa2 = new Guardarropas(prendas1);
		
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		guardaRopas.add(guardaRopa1);
		guardaRopas.add(guardaRopa2);
		Usuario pepe = new Usuario(guardaRopas);
		Assert.assertEquals(pepe.getGuardarropasConPrenda(Categoria.PARTE_SUPERIOR), guardaRopa1);
		Assert.assertEquals(pepe.getGuardarropasConPrenda(Categoria.PARTE_INFERIOR), guardaRopa2);
	}
	
	@Test
	public void generarAtuendoValidoDeTresPrendas() throws Exception {
		prendas.add(prenda);
		prendas.add(prenda1);
		prendas.add(prenda2);
		Guardarropas guardaRopa1 = new Guardarropas(prendas);
		
		prendas1.add(prenda3);
		prendas1.add(prenda4);
		prendas1.add(prenda5);
		Guardarropas guardaRopa2 = new Guardarropas(prendas1);
		
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		guardaRopas.add(guardaRopa1);
		guardaRopas.add(guardaRopa2);
		Usuario pepe = new Usuario(guardaRopas);
		
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		categorias.add(Categoria.PARTE_INFERIOR);
		categorias.add(Categoria.PARTE_SUPERIOR);
		categorias.add(Categoria.CALZADO);
		
		Atuendo atuendo = pepe.sugerirAtuendo(categorias);
		Assert.assertEquals(atuendo.getPrendas().get(0).Categoria(), Categoria.PARTE_INFERIOR);
		Assert.assertEquals(atuendo.getPrendas().get(1).Categoria(), Categoria.PARTE_SUPERIOR);
		Assert.assertEquals(atuendo.getPrendas().get(2).Categoria(), Categoria.CALZADO);
	}
	
	@Test
	public void generarAtuendoValidoDeDosPrendas() throws Exception {
		prendas.add(prenda);
		prendas.add(prenda1);
		prendas.add(prenda2);
		Guardarropas guardaRopa1 = new Guardarropas(prendas);
		
		prendas1.add(prenda3);
		prendas1.add(prenda4);
		prendas1.add(prenda5);
		Guardarropas guardaRopa2 = new Guardarropas(prendas1);
		
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		guardaRopas.add(guardaRopa1);
		guardaRopas.add(guardaRopa2);
		Usuario pepe = new Usuario(guardaRopas);
		
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		categorias.add(Categoria.PARTE_INFERIOR);
		categorias.add(Categoria.CALZADO);
		
		Atuendo atuendo = pepe.sugerirAtuendo(categorias);
		Assert.assertEquals(atuendo.getPrendas().get(0).Categoria(), Categoria.PARTE_INFERIOR);
		Assert.assertEquals(atuendo.getPrendas().get(1).Categoria(), Categoria.CALZADO);
	}
	
	@Test(expected = Exception.class)
	public void generarAtuendoError() throws Exception {
		ArrayList <Guardarropas> guardaRopas = new ArrayList <Guardarropas>();
		Usuario pepe = new Usuario(guardaRopas);
		pepe.sugerirAtuendo(new ArrayList<Categoria>());
	}

}
