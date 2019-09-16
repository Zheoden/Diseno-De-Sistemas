package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

import modelo.clases.Evento;
import modelo.clases.Usuario;
import modelo.interfaces.NotificationManager;


public class emailSender implements NotificationManager{

	public emailSender () {}
	
	@Override
	public Properties getProyectProperties() throws Exception {
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
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("No se pudo cargar el archivo de properties.");
		}	

		return prop;
	}
	
	@Override
	public Properties getPropertiesForServer(String tipoServidor) throws Exception {
		Properties properties = Utils.getProyectProperties();
		Properties aux = new Properties();
	    Set<String> propertyNames = properties.stringPropertyNames();
	    for (String name : propertyNames) {
	    	if(name.matches(".*" + tipoServidor + ".*")) {
		    	String propertyValue = properties.getProperty(name);
		    	aux.setProperty(name.substring(tipoServidor.length() + 1), propertyValue);
	    	}
	    }
	    return aux;
	}
	
	@Override
	public void emailSend(String tipoServidor, Usuario user, Evento evento) throws Exception {
		Properties properties = getPropertiesForServer(tipoServidor);
		
	    Session session = Session.getInstance(properties, null);
	    Message mensaje = new MimeMessage(session);
	    mensaje.setFrom(new InternetAddress(properties.getProperty("mail.from")));
	    //Si se quiere se puede enviar a varios usuarios
	    StringTokenizer emailsSt = new StringTokenizer(user.getEmail(),";,");
	    while (emailsSt.hasMoreTokens()) {
	    	String email = emailsSt.nextToken();
	    	try{
	    		mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
	    		//Message.RecipientType.TO;  para
	    		//Message.RecipientType.CC;  con copia
	    		//Message.RecipientType.BCC; con copia oculta
	    	}catch(Exception ex){
	    		//en caso que el email esté mal formado lanzará una exception y la ignoramos
	    	}
	    }
	    mensaje.setSubject("Recordatorio evento " + evento.getNombre());
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
	    mensaje.setSentDate(dateFormat.parse(dateFormat.format(date)));
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setText("Se aproxima el evento " + evento.getNombre() + " en la fecha " + evento.getFechaEvento() + " te sugiero que ");
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);
	    mensaje.setContent(multipart);
	    SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
	    try {
	        transport.connect(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
	        transport.sendMessage(mensaje, mensaje.getAllRecipients());
	        System.out.println("Email enviado correctamente");
	    } finally {
	        transport.close();
	    }
	}

}