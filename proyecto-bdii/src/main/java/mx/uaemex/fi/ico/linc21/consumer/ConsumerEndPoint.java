package mx.uaemex.fi.ico.linc21.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.quarkus.vertx.web.Body;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mx.uaemex.fi.ico.linc21.entities.Cliente;
import mx.uaemex.fi.ico.linc21.security.TokenIssuer;
import static mx.uaemex.fi.ico.linc21.security.AllowedEntities.*;
import static mx.uaemex.fi.ico.linc21.consumer.ConsumerService.*;

@RequestScoped
@Path("/api/consumer")
public class ConsumerEndPoint {
	
	Gson gson;
	
	@Inject
	TokenIssuer tokenIssuer;
	@Inject
	ConsumerService consumerService;
	
	public ConsumerEndPoint() {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
	}
	
	@GET
	@Path("demo2")
	@PermitAll
	public String holamundo() {
		return "Hola todos";
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/login")
	@PermitAll
	public Response login(String body) {
		
		Cliente cliente = gson.fromJson(body, Cliente.class);
		consumerService.login(cliente);
		String requestStatus = consumerService.getStatus();
		
		switch(requestStatus) {
			case SUCCESS:
				return Response.status(200).entity(tokenIssuer.generateUserToken(cliente.usuario)).build();
			
			case BAD_LOGIN:
				return Response.status(401).entity(requestStatus).build();
			
			default:
				return Response.status(500).entity(requestStatus).build();
		}
		
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/signin")
	@PermitAll
	public Response signIn(String body) {
		
		Cliente cliente = gson.fromJson(body, Cliente.class);
		consumerService.registerNewClient(cliente);
		String requestStatus = consumerService.getStatus(); 
		
		
		switch(requestStatus) {
			case SUCCESS:
				return Response.status(200).entity(tokenIssuer.generateUserToken(cliente.usuario)).build();
			
			default:
				return Response.status(500).entity(requestStatus).build();
		}
	}
	
	
	
	@GET
	@Path("/auth-test")
	@RolesAllowed({CLIENTE_DEL_BANCO})
	public Response saludo(){
		return Response.status(200).entity("Autenticación exitosa").build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("demo1")
	@PermitAll
	public Response demo1(String body) {
		Cliente cliente = gson.fromJson(body, Cliente.class);
		Cliente cliente2 = consumerService.buscarClientePorRFC(cliente);
		String requestStatus = consumerService.getStatus();
		
		switch(requestStatus) {
			case SUCCESS:
				return Response.status(200).entity(gson.toJson(cliente2)).build();
			
			case NOT_FOUND:
				return Response.status(404).entity(requestStatus).build();
			
			default:
				return Response.status(500).entity(requestStatus).build();
		}
	}
}


