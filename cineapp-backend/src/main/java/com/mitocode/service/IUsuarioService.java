package com.mitocode.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Usuario;

public interface IUsuarioService extends ICRUD<Usuario>{

	Usuario registrarTransaccional(Usuario us);
	
	Usuario leerPorUser(String user);
}
