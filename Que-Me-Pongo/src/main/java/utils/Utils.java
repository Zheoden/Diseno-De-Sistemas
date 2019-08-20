package utils;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

import java.util.Properties;

import modelo.clases.Atuendo;
import modelo.clases.Evento;
import modelo.clases.Usuario;

public class Utils {
	
	private static final String INSTANCE_ID = "YOUR_INSTANCE_ID_HERE";
	private static final String CLIENT_ID = "YOUR_CLIENT_ID_HERE";
	private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET_HERE";
	private static final String WA_GATEWAY_URL = "http://api.whatsmate.net/v3/whatsapp/single/text/message/" + INSTANCE_ID;
	
	@SuppressWarnings("resource")
	public static String readFileFromResources(String path) {

		InputStream resourceStream = Utils.class.getClassLoader().getResourceAsStream(path);
		if (resourceStream == null)
			return "";

		return new Scanner(resourceStream, "UTF-8").useDelimiter("\\A").next();
	}

	public static Double kelvinToCelsius(Double kelvin) {
		return kelvin - 273.00;
	}
	
	public static void recordatorio(int minutosAnteriores, Evento evento, Usuario usuario) {
		int minutos = evento.getFecha().get(Calendar.MINUTE);
		int minutosAntes = minutos - minutosAnteriores;
		Calendar aux = GregorianCalendar.getInstance();
		aux.set(Calendar.MINUTE, minutosAntes);
		Date dateAux = aux.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String despertador = dateFormat.format(dateAux);

		Timer t = new Timer();
		try {
			t.schedule(new TimerTask() {
				public void run() {
					List<Atuendo> atuendosValidos = usuario.todosPosiblesAtuendosPorGuardarropaParaEvento(evento);
					System.out.println(atuendosValidos);
					System.out.print("Recordatorio de evento Ir al alamo");
				}
			}, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(despertador));
		} catch (ParseException e) {
			System.out.println(e);
		}
	}
	
	public static Properties getProyectProperties(String tipoServidor) throws Exception {
		
		Properties prop = new Properties();
		InputStream input = null ;
	
		try {
			input = new FileInputStream("system.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("No se encuentra el archivo de properties.");
		}
		
		try {
			prop.load(input);
			if(tipoServidor == "gmail")
			{
			prop.setProperty("mail.smtp.host","smtp.gmail.com");
			prop.setProperty("mail.smtp.port","587");
			}
			else
			{
				prop.setProperty("mail.smtp.host","smtp.live.com");
				prop.setProperty("mail.smtp.port","25");	
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("No se pudo cargar el archivo de properties.");
		}	
		
		return prop;
	}
	
	public static void emailSender(String tipoServidor, Usuario user, Evento evento) throws Exception {
		//objeto donde almacenamos la configuración para conectarnos al servidor
		Properties properties = Utils.getProyectProperties();
		Properties aux = new Properties();
	    Set<String> propertyNames = properties.stringPropertyNames();
	    for (String name : propertyNames) {
	    	if(name.matches(".*" + tipoServidor + ".*")) {
		    	String propertyValue = properties.getProperty(name);
		    	aux.setProperty(name.substring(tipoServidor.length() + 1), propertyValue);
	    	}
	    }
	    
	    System.out.println(aux);
	    Session session = Session.getInstance(aux, null);
		//creamos el mensaje a enviar
	    Message mensaje = new MimeMessage(session);
	    //agregamos la dirección que envía el email
	    mensaje.setFrom(new InternetAddress(properties.getProperty("mail.from")));
	    //Si se quiere se puede enviar a varios usuarios
	    StringTokenizer emailsSt = new StringTokenizer(user.getEmail(),";,");
	    while (emailsSt.hasMoreTokens()) {
	    	String email=emailsSt.nextToken();
	    	try{
	    		//agregamos las direcciones de email que reciben el email, en el primer parametro envíamos el tipo de receptor
	    		mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
	    		//Message.RecipientType.TO;  para
	    		//Message.RecipientType.CC;  con copia
	    		//Message.RecipientType.BCC; con copia oculta
	    	}catch(Exception ex){
	    		//en caso que el email esté mal formado lanzará una exception y la ignoramos
	    	}
	    }
	    mensaje.setSubject("Recordatorio evento " + evento.getNombre());
	    //agregamos una fecha al email
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
	    mensaje.setSentDate(dateFormat.parse(dateFormat.format(date)));
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setText("Se aproxima el evento " + evento.getNombre() + "en la fecha " + evento.getFechaEvento() + "te sugiero que ");
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);
	    mensaje.setContent(multipart);
	    SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
	    try {
	    	//conectar al servidor
	        transport.connect(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
	        //enviar el mensaje
	        transport.sendMessage(mensaje, mensaje.getAllRecipients());
	    } finally {
	    	//cerrar la conexión
	        transport.close();
	    }
	}

	public  void enviarEmail(String tipoServidor,String a,Evento evento, List<Atuendo> atuendos) throws Exception {
		if (tipoServidor == null) {
			tipoServidor = "smtp";
		}
		Properties properties = getProyectProperties(tipoServidor);
		//InputStream input = new FileInputStream("src/main/java/utils/" + "system" + ".properties");
		//properties.load(input);
		Session session = Session.getInstance(properties, null);
		Message mensaje = new MimeMessage(session);
		mensaje.setFrom(new InternetAddress(properties.getProperty(tipoServidor + ".mail.from")));
		// Si se quiere se puede enviar a varios usuarios
		StringTokenizer emailsSt = new StringTokenizer(a,";,");
		while (emailsSt.hasMoreTokens()) {
			String email = emailsSt.nextToken();
			try {
				mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
				// Message.RecipientType.TO; para
				// Message.RecipientType.CC; con copia
			} catch (Exception ex) {
				System.out.print(ex);
			}
		}
		mensaje.setSubject("Recordatorio evento " + evento.getNombre());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		mensaje.setSentDate(dateFormat.parse(dateFormat.format(date)));
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart
				.setText("Se aproxima el evento " + evento.getNombre() + " en la fecha " + evento.getFechaEvento());
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		mensaje.setContent(multipart);
		SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
		try {
			transport.connect(properties.getProperty(tipoServidor + ".mail.user"), properties.getProperty(tipoServidor + ".mail.password"));
			transport.sendMessage(mensaje, mensaje.getAllRecipients());
			System.out.println("Email enviado correctamente");
		} finally {
			transport.close();
		}
	}
}
