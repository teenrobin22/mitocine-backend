package com.mitocode.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private IUsuarioService service;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@PostMapping(produces = "application/json", consumes = "application/json")
	private ResponseEntity<Object> registrar(@RequestBody Usuario usuario){		
		usuario.setPassword(bcrypt.encode(usuario.getPassword()));
		service.registrarTransaccional(usuario);
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@PostMapping
	private ResponseEntity<Object> registrar(@RequestPart("usuario")  Usuario usuario , @RequestPart("file") MultipartFile file)		
		throws IOException {
		usuario.setPassword(bcrypt.encode(usuario.getPassword()));
		Cliente cl = usuario.getCliente();
		cl.setFoto(file.getBytes());
		usuario.setCliente(cl);
		service.registrarTransaccional(usuario);
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<Usuario> listar(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public Usuario leer(@PathVariable("id") Integer id) {
		return service.leer(id);
	}
	
	@GetMapping(value = "obtenerFoto/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> obtenerFoto(@PathVariable("id") Integer id) {
		Usuario u = service.leer(id);
		byte[] data = u.getCliente().getFoto();
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	
	@PutMapping
	public Usuario modificar(@RequestPart("usuario") Usuario usuario, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente cl = usuario.getCliente();
		cl.setFoto(file.getBytes());
		usuario.setCliente(cl);
		
		if(usuario.getPassword()!=null)
		{
			usuario.setPassword(bcrypt.encode(usuario.getPassword()));
		}
		else {
			Usuario u = service.leer(usuario.getIdUsuario());
			usuario.setPassword(u.getPassword());
		}
		return service.modificar(usuario);
	}
	
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer idUsuario) {
		service.eliminar(idUsuario);
	}
	
	@GetMapping(value="listarPorUser/{user}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public  ResponseEntity<byte[]> listarPorUser(@PathVariable("user") String userName){
		Usuario u = service.leerPorUser(userName);
		byte[] data = u.getCliente().getFoto();
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	
}
