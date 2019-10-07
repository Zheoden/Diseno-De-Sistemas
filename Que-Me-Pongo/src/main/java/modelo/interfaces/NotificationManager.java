package modelo.interfaces;

import modelo.clases.Evento;
import modelo.clases.Usuario;

public interface NotificationManager {

	void emailSend(String tipoServidor, Usuario user, Evento evento) throws Exception;

}
