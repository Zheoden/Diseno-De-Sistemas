package test.modelo.clases;

import modelo.clases.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import modelo.enums.Color;
import modelo.enums.comportamiento.TipoPrenda;

public class EventoTest {

	Prenda camisaRojo = new Prenda(TipoPrenda.CAMISA, Color.ROJO);
	Prenda bufandaAzul = new Prenda(TipoPrenda.BUFANDA, Color.AZUL);
	Prenda pantalonVerde = new Prenda(TipoPrenda.PANTALON, Color.VERDE);
	ArrayList<Prenda> prendas1 = new ArrayList<Prenda>();
	Guardarropas guardaropa1 = new Guardarropas(prendas1);
	ArrayList<Guardarropas> guardaRopas = new ArrayList<Guardarropas>();
	Suscripcion premium = new SuscripcionPremium();
	Calendar fecha1 = GregorianCalendar.getInstance();
	Calendar fecha2 = GregorianCalendar.getInstance();

	@Test
	public void usuarioAsisteASusEventos() throws Exception {
		Usuario usuario1 = new Usuario(guardaRopas, premium);
		prendas1.add(camisaRojo);
		prendas1.add(bufandaAzul);
		prendas1.add(pantalonVerde);
		guardaRopas.add(guardaropa1);
		fecha1.set(2019, 10, 12);
		fecha1.set(Calendar.HOUR_OF_DAY, 07);
		fecha1.set(Calendar.MINUTE, 30);
		fecha2.set(2019, 06, 29);
		fecha2.set(Calendar.HOUR_OF_DAY, 21);
		fecha2.set(Calendar.MINUTE, 30);
		Evento irAlAlamo = new Evento("AlamosNigth", "Adrogue", fecha2);
		Evento developer = new Evento("Desarrollar software", "Azul", fecha1);
		usuario1.cargarEvento(irAlAlamo);
		usuario1.cargarEvento(developer);
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		usuario1.irAEventos();
		verify(out).println(startsWith("Voy a AlamosNigth en Adrogue en la fecha 2019-07-29 21:30"));
		verify(out).println(startsWith("Voy a Desarrollar software en Azul en la fecha 2019-11-12 07:30"));
	}

	@Test
	public void noEstasInvitadoAlEvento() throws Exception {
		prendas1.add(camisaRojo);
		prendas1.add(bufandaAzul);
		prendas1.add(pantalonVerde);
		guardaRopas.add(guardaropa1);
		Usuario usuario1 = new Usuario(guardaRopas, premium);
		fecha1.set(2019, 10, 12);
		fecha1.set(Calendar.HOUR_OF_DAY, 21);
		fecha1.set(Calendar.MINUTE, 30);
		Evento irAlAlamo = new Evento("Ir al alamo", "Lanus", fecha1);
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		usuario1.irAElEvento(irAlAlamo);
		verify(out).println(startsWith("No estas invitado al evento Ir al alamo"));
	}

	@Test
	public void recordatorioEventoFail() throws Exception {
		prendas1.add(camisaRojo);
		prendas1.add(bufandaAzul);
		prendas1.add(pantalonVerde);
		guardaRopas.add(guardaropa1);
		Usuario usuario1 = new Usuario(guardaRopas, premium);
		usuario1.setGuardaRopas(guardaRopas);
		fecha1.set(2019,8,2);
		fecha1.set(Calendar.HOUR_OF_DAY, 14);
		fecha1.set(Calendar.MINUTE, 46);
		fecha1.set(Calendar.SECOND, 00);
		Evento irAlAlamo = new Evento("Ir al alamo", "Bueno Aires", fecha1);
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.out.println(new PrintStream(outContent));
	    Assert.assertEquals("", outContent.toString());
	}
	
	
	@Test
	//Avisa del evento un minuto antes en este caso
	public void recordatorioEventoSucces() throws Exception {
		prendas1.add(camisaRojo);
		prendas1.add(bufandaAzul);
		prendas1.add(pantalonVerde);
		guardaRopas.add(guardaropa1);
		Usuario usuario1 = new Usuario(guardaRopas, premium);
		usuario1.setGuardaRopas(guardaRopas);
		fecha1.set(2019,7,2);
		fecha1.set(Calendar.HOUR_OF_DAY, 14);
		fecha1.set(Calendar.MINUTE, 46);
		fecha1.set(Calendar.SECOND, 00);
		Evento irAlAlamo = new Evento("Ir al alamo", "Bueno Aires", fecha1);
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		usuario1.cargarEvento(irAlAlamo);
		verify(out).println(startsWith("Recordatorio de evento Ir al alamo"));
	}


}
