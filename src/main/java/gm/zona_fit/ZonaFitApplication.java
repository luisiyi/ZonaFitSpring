package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {


	//Aquí hacemos inyeccion de dependencia del objeto de "Cliente Servicio"
	@Autowired
	private IClienteServicio clienteServicio;

	String nl = System.lineSeparator();

	private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);



	public static void main(String[] args) {
		logger.info("Iniciando la aplicacion");
		//Levantar la fabrica de spring
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("Aplicación finalizada!");
	}

	@Override
	public void run(String... args) throws Exception {
		zonaFitSpring();
	}

	public void zonaFitSpring(){
		Scanner consola = new Scanner(System.in);
		boolean salir = false;
		while(!salir){
			try {
				int opc = mostrarMenu(consola);
				salir = ejecutarOpciones(opc, consola, clienteServicio);
			}catch (Exception e){
				logger.info("Ocurrio un error: " + e.getMessage());
			}finally {
				logger.info("");
			}

		}
	}

	public int mostrarMenu(Scanner consola){
		logger.info("""
				*** Aplicacion Zona Fit (GYM) ***
				1.- Listar Clientes
				2.- Buscar Cliente por Id
				3.- Agregar Cliente
				4.- Actualizar Cliente
				5.- Eliminar Cliente
				6.- Salir
				Eligar una opcion:""");
		return Integer.parseInt(consola.nextLine());
	}

	public boolean ejecutarOpciones(int opc, Scanner consola, IClienteServicio clienteServicio){
		boolean salir = false;
		switch (opc){
			case 1 -> listarClientes(clienteServicio);
			case 2 -> buscarClientePorId(consola, clienteServicio);
			case 3 -> agregarCliente(consola, clienteServicio);
			case 4 -> actualizarCliente(consola, clienteServicio);
			case 5 -> eliminarCliente(consola, clienteServicio);
			case 6 -> {
				logger.info("Hasta pronto!");
				salir = true;
			}
			default -> logger.info("Opcion no reconocida: " + opc + nl);

		}
		return salir;
	}

	private void listarClientes(IClienteServicio clienteServicio){
		List<Cliente> clientes = clienteServicio.listarClientes();
		clientes.forEach(cliente -> logger.info(cliente.toString()));
	}

	private void buscarClientePorId(Scanner consola, IClienteServicio clienteServicio){
		logger.info("Ingresa el id del cliente a buscar: ");
		Integer idCliente = Integer.parseInt(consola.nextLine());
		Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
		if (cliente != null)
			logger.info(cliente.toString());
		else
			logger.error("--- Id del cliente no encontrado ---");
	}

	private void agregarCliente(Scanner consola, IClienteServicio clienteServicio){
		logger.info("--- Agregando nuevo Cliente ---");
		Cliente cliente = new Cliente();
		logger.info("Nombre: ");
		cliente.setNombre(consola.nextLine());
		logger.info("Apellido: ");
		cliente.setApellido(consola.nextLine());
		logger.info("Membresia: ");
		cliente.setMembresia(Integer.parseInt(consola.nextLine()));
		clienteServicio.guardarCliente(cliente);
		logger.info("Cliente agregado: " + cliente);
	}

	private void actualizarCliente(Scanner consola, IClienteServicio clienteServicio){
		logger.info("--- Actualizando Cliente ---");
		logger.info("Ingrese el id del Cliente a actualizar: ");
		Integer idCliente = Integer.parseInt(consola.nextLine());
		Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
		if (cliente != null){
			logger.info("Nombre: ");
			cliente.setNombre(consola.nextLine());
			logger.info("Apellido: ");
			cliente.setApellido(consola.nextLine());
			logger.info("Membresia: ");
			cliente.setMembresia(Integer.parseInt(consola.nextLine()));
			clienteServicio.guardarCliente(cliente);
			logger.info("--- Cliente actualizado!");
		}else{
			logger.info("Id del cliente no encontrado, no se actualizara");
		}
	}

	private void eliminarCliente(Scanner consola, IClienteServicio clienteServicio){
		logger.info("--- Eliminando Cliente ---");
		logger.info("Ingrese el id del cliente a eliminar: ");
		Integer idCliente = Integer.parseInt(consola.nextLine());
		Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
		if (cliente != null){
			logger.info(cliente.toString());
			clienteServicio.eliminarCliente(cliente);
			logger.info("Cliente eliminado exitosamente!");
		}else {
			logger.info("--- Id del cliente no encontrado, no se eliminara ningun cliente");
		}
	}

}
