package http.routes;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;

import modelo.clases.Atuendo;
import modelo.clases.Evento;
import modelo.clases.Guardarropas;
import modelo.clases.Prenda;
import modelo.clases.Usuario;
import modelo.dtos.Categoria;
import modelo.dtos.Color;
import modelo.dtos.Material;
import repository.AtuendoRepository;
import repository.CategoriaRepository;
import repository.ColorRepository;
import repository.EventoRepository;
import repository.GuardarropaRepository;
import repository.MaterialRepository;
import repository.PrendaRepository;
import repository.UsuarioRepository;
import utils.JsonParser;

public class Router {

	public static void register() {
		
		UsuarioRepository userService = new UsuarioRepository();
		GuardarropaRepository guardarropaService = new GuardarropaRepository();
		EventoRepository eventoService = new EventoRepository();
		AtuendoRepository atuendoService = new AtuendoRepository();
		PrendaRepository prendaService = new PrendaRepository();
		ColorRepository colorService = new ColorRepository();
        CategoriaRepository categoriaService = new CategoriaRepository();
        MaterialRepository materialService = new MaterialRepository();

		get("/", (req, res) -> "Home");
		
// Controladores Sobre Usuarios ----------------------------------------------------------------------------------------------	
		
		get("/users", "application/json", (req, res) -> {
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userService.all());
		});
		
		post("/login" ,"application/json",(req, res) -> {

			Usuario userFind = JsonParser.read(req.body(), new TypeReference<Usuario>(){}); 
			Optional<Usuario> usuarioLogin = userService.findUserByLogin(userFind.getUsername(), userFind.getPassword());
		
			if(usuarioLogin.isPresent()) {
				res.status(200);
				return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(usuarioLogin.get());
			}
			
			res.status(400);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(null);		
		});

		get("/users/:id", "application/json" ,(req, res) -> {
			String id = req.params(":id");
			Optional<Usuario> usuarioBuscado = userService.findById(Integer.parseInt(id));
			if (usuarioBuscado.isPresent()) {
				res.status(200);
				return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(usuarioBuscado.get());
			}
			res.status(400);
			return JsonParser.getObjectMapper().writeValueAsString("No se encontro el Usuario con id: " + id);
		});
		
		delete("/users/:id/eliminarUsuario", "application/json", (req, res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString("No existe el usuario con id: " + id);
			}
			
				userService.delete(userABuscar.get());
				res.status(200);
				return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString("Se elimino el usuario con id: " + id);		
		});	
		
// Controladores Sobre Guardarropas ----------------------------------------------------------------------------------------------				
		
		post("/users/:id/crearGuardarropa", "application/json", (req, res) -> {
			long id = Integer.parseInt(req.params(":id"));
			List<Guardarropas> guardarropas = guardarropaService.findByUserId(id);
			if (guardarropas.size() < 2) {
				Usuario user = userService.findById(id).get();
				Guardarropas guardarropaCreado = JsonParser.read(req.body(), new TypeReference<Guardarropas>(){}); 	
				user.agregarGuardarropa(guardarropaCreado);
				userService.update(user);
				Usuario userActualizado = userService.findById(id).get();
				res.status(200);
				return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userActualizado.getGuardarropas());
			}	
			res.status(400);
			return JsonParser.getObjectMapper().writeValueAsString("Solo puede tener como maximo 2 Guardarropas");
		});
		
		delete("/users/:id/guardarropas/:idGuardarropa/eliminarGuardarropa", "application/json", (req, res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			long idGuardarropa = Integer.parseInt(req.params("idGuardarropa"));
			Optional<Guardarropas> guardarropaBuscado = guardarropaService.findWardrobeById(id, idGuardarropa);
			
			if(!guardarropaBuscado.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("No se encontro el Guardarropa con el id: " + guardarropaBuscado.get().getId());
			}
			
			guardarropaService.delete(guardarropaBuscado.get());

			Usuario userActualizado = userService.findById(id).get();
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userActualizado.getGuardarropas());
		});
		
// Controladores Sobre Prendas ----------------------------------------------------------------------------------------------		
		
		post("/users/:id/guardarropas/:idGuarda/createPrenda", "application/json" ,(req, res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			Usuario userEncontrado = userABuscar.get();
			long idGuardarropa = Integer.parseInt(req.params("idGuarda"));
			Optional<Guardarropas> guardarropaBuscado = guardarropaService.findWardrobeById(id, idGuardarropa);
			
			if(!guardarropaBuscado.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("No se encontro el Guardarropa con el nombre: " + guardarropaBuscado.get().getNombre());
			}
			
			Guardarropas guardarropaEncontrado = guardarropaBuscado.get();
			Prenda prendaCreada = JsonParser.read(req.body(), new TypeReference<Prenda>(){}); 
			guardarropaEncontrado.getPrendas().add(prendaCreada);
			userService.update(userEncontrado);
			Guardarropas guardarropaActualizado = guardarropaService.findWardrobeById(id, idGuardarropa).get();
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(guardarropaActualizado);		
		});
		
		delete("/users/:id/guardarropas/:idGuardarropa/prendas/:idPrenda/eliminarPrenda", "application/json", (req, res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			long idGuardarropa = Integer.parseInt(req.params("idGuardarropa"));
			Optional<Guardarropas> guardarropaBuscado = guardarropaService.findWardrobeById(id, idGuardarropa);
			
			if(!guardarropaBuscado.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("No se encontro el Guardarropa con el id: " + guardarropaBuscado.get().getId());
			}
			
			long idPrenda = Integer.parseInt(req.params("idPrenda"));
			Optional<Prenda> prendaABuscar = prendaService.findPrendaInGuardarropaById(idGuardarropa, idPrenda);
			
			if(!prendaABuscar.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("No se encontro la Prenda con el id: " + prendaABuscar.get().getId());
			}
			
			prendaService.delete(prendaABuscar.get());
			Guardarropas guardarropaActualizado = guardarropaService.findWardrobeById(id, idGuardarropa).get();
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(guardarropaActualizado.getPrendas());	
		});
		
// Controladores Sobre Eventos ----------------------------------------------------------------------------------------------		
		
		post("/users/:id/eventos/crearEvento", "application/json", (req, res) ->{
			
			long id = Integer.parseInt(req.params(":id"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(404);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			Usuario userEncontrado = userABuscar.get();
			Evento eventoCreado = JsonParser.read(req.body(), new TypeReference<Evento>(){}); 	
			eventoCreado.setUsuario(userEncontrado);
			userEncontrado.getEventos().add(eventoCreado);
			userService.update(userEncontrado);
			Usuario userActualizado = userService.findById(id).get();
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userActualizado.getEventos());	
		});
		
		delete("/users/:id/eventos/:idEvento/eliminarEvento", "application/json", (req, res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			long idEvento = Integer.parseInt(req.params("idEvento"));
			Optional<Evento> eventoBuscado = eventoService.find(id, idEvento);
			
			if(!eventoBuscado.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("No se encontro el Evento con el id: " + eventoBuscado.get().getId());
			}
			
			eventoService.delete(eventoBuscado.get());
			Usuario usuarioActualizado = userService.findById(id).get();
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(usuarioActualizado.getEventos());	
		});
		
// Controladores Sobre Atuendos ----------------------------------------------------------------------------------------------
		
		get("/users/:id/eventos/:idEvento/sugerenciasAceptadas",(req,res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			long idEvento = Integer.parseInt(req.params(":idEvento"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(404);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			Optional <Evento> nuevoEvento = eventoService.find(id,idEvento);
			if(!nuevoEvento.isPresent()) {
				res.status(400);
				return JsonParser.getObjectMapper().writeValueAsString("El Evento:" + nuevoEvento.get().getNombre() + "no existe");
			}

			List<Atuendo> atuendosAceptados = atuendoService.findSugerenciasAceptadasParaEvento(idEvento, id);
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(atuendosAceptados);
		});
		
		get("/users/:id/eventos/:idEvento/sugerencias",(req,res) -> {
			
			long id = Integer.parseInt(req.params(":id"));
			long idEvento = Integer.parseInt(req.params(":idEvento"));
			Optional<Usuario> userABuscar = userService.findById(id);
			
			if(!userABuscar.isPresent()) {
				res.status(404);
				return JsonParser.getObjectMapper().writeValueAsString("El Usuario No Existe");
			}
			
			Optional <Evento> eventoABuscar = eventoService.find(id,idEvento);
			
			if(!eventoABuscar.isPresent()) {
				res.status(404);
				return JsonParser.getObjectMapper().writeValueAsString("El Evento:" + eventoABuscar.get().getNombre() + "no existe");
			}
			
			List<Atuendo> atuendosSugeridos = atuendoService.findSugerenciasParaEvento(idEvento, id);
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(atuendosSugeridos);
		});
		
		
		/*
		post("/users/:username/eventos/:evento/calificarAtuendo", (req,res) -> {
			
			Optional <Evento> nuevoEvento = eventoService.find(username,evento);
			if(!nuevoEvento.isPresent()) {
				res.status(404);
				return JsonParser.getObjectMapper().writeValueAsString("El Evento:" + evento + "no existe");
			}
			Evento eventoEncontrado = nuevoEvento.get(); 
			res.status(203);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(eventoEncontrado.getAtuendosAceptados());

		});
		*/
		
// Controladores Sobre Enumeradores ----------------------------------------------------------------------------------------------
		
		get("/categorias)", (req, res) -> {
			List<Categoria> categorias = categoriaService.all();
			res.status(200);
			return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(categorias);
		});

        get("/colores)", (req, res) -> {
            List<Color> colores = colorService.all();
            res.status(200);
            return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(colores);
        });


        get("/materiales)", (req, res) -> {
            List<Material> materiales = materialService.all();
            res.status(200);
            return JsonParser.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(materiales);
        });
	}	
}