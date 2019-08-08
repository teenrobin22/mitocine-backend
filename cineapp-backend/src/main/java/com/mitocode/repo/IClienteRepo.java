package com.mitocode.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Cliente;

public interface IClienteRepo extends JpaRepository<Cliente, Integer>{
	
	@Modifying
	@Query("UPDATE Cliente set foto = :foto where id = :id")
	void modificarFoto(@Param("id") Integer id, @Param("foto") byte[] foto);
	
}
