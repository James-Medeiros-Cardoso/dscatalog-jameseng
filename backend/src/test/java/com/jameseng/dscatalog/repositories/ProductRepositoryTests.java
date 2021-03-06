package com.jameseng.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.jameseng.dscatalog.entities.Product;
import com.jameseng.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	//Inicializadas as variáveis de testes (vaor será inicializado no @BeforeEach)
	private long existingId;
	private long noExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId=1L;
		noExistingId=1000L;
		countTotalProducts=25L;
	}
	
	@Test
	public void deleteShouldDeleteWhenIdExists() {
		
		//Arrange
		//long existingId=1L;
		
		//Act
		repository.deleteById(existingId);
		
		//Assert = deve voltar vazio, pois foi deletado no comando antecedente
		Optional<Product> result=repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product=Factory.createProduct();
		product.setId(null);
		
		product=repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShoudThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		//long noExistingId=1000L;
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(noExistingId);
		});
	}
	
	@Test //FEITO POR JAMES - Exercício: Teste de repository
	public void findByIdShouldReturnNoEmptyOptionalWhenIdExists() {
		
		Optional<Product> result=repository.findById(existingId);
		
		//para ver se tem um Optional<Product> na variável "result"
		Assertions.assertTrue(result.isPresent());
		//result.isPresent() = testa se existe um produto dentro de "result"
	}
	
	@Test //FEITO POR JAMES - Exercício: Teste de repository
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		
		Optional<Product> result=repository.findById(noExistingId);
		Assertions.assertFalse(result.isPresent()); //Acrescentado por James
		Assertions.assertTrue(result.isEmpty());
		// result.isEmpty() = testa se o optional está vazio
	}
}