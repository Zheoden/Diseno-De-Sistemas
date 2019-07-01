package test.modelo.clases;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import modelo.clases.Imagen;
import modelo.clases.Prenda;
import modelo.enums.*;
import modelo.enums.comportamiento.TipoInferior;
import modelo.enums.comportamiento.TipoSuperior;

@DisplayName("Tests para las Prendas")
public class PrendasTest {

	@Test
	@DisplayName("Test para validar la creacion de prendas con mismo color primario y secundario")
	public void validarPrendasConMismoColorPrimarioYSecundario() {
		Prenda prenda = new Prenda(TipoInferior.PANTALON, Material.JEAN, Color.AMARILLO, Color.AMARILLO);
		Assert.assertEquals(prenda.getColorPrimario(), Color.AMARILLO);
		Assert.assertEquals(prenda.getTela(), Material.JEAN);
		Assert.assertEquals(prenda.Categoria(), Categoria.PARTE_INFERIOR);
		Assert.assertEquals(prenda.getColorSecundario(), null);
	}

	@Test
	@DisplayName("Test para verificar la creacion de las prendas")
	public void constructorPrenda() {
		Prenda prenda = new Prenda(TipoSuperior.REMERACORTA, Material.ALGODON, Color.AZUL, Color.ROJO);
		Assert.assertEquals(prenda.getColorPrimario(), Color.AZUL);
		Assert.assertEquals(prenda.getColorSecundario(), Color.ROJO);
		Assert.assertEquals(prenda.getTela(), Material.ALGODON);
		Assert.assertEquals(prenda.Categoria(), Categoria.PARTE_SUPERIOR);
	}
	
	@Test
	@DisplayName("Test para verificar la creacion de prendas con tipo invalido")
	public void validarCreacionInvalida() {
		Prenda prenda = new Prenda(TipoSuperior.REMERACORTA, Material.CUERO, Color.AMARILLO, Color.ROJO);
		Assert.assertEquals(prenda.getColorPrimario(), Color.AMARILLO);
		Assert.assertEquals(prenda.getTela(), null);
		Assert.assertEquals(prenda.Categoria(), Categoria.PARTE_SUPERIOR);
		Assert.assertEquals(prenda.getColorSecundario(), Color.ROJO);
	}
	
	@Test
	@DisplayName("Test para normalizar las imagenes")
	public void renderizarImagen() {
		Imagen objetoImagen = new Imagen();
		
		Prenda prenda = new Prenda(TipoInferior.PANTALON, Material.JEAN, Color.AZUL, Color.CELESTE);
		prenda.setDireccionImagen("ImgPrendas/jeans.jpg");
		objetoImagen.normalizarImagen(prenda.getDireccionImagen());
		Assert.assertEquals(objetoImagen.getImagenRenderizada().getHeight(), 800);
		
		Prenda otraPrenda = new Prenda(TipoSuperior.REMERACORTA, Material.ALGODON, Color.BLANCO, Color.AZUL);
		otraPrenda.setDireccionImagen("ImgPrendas/remeraCorta.jpg");
		objetoImagen.normalizarImagen(otraPrenda.getDireccionImagen());
		Assert.assertEquals(objetoImagen.getImagenRenderizada().getWidth(), 600);
	}
}
