package com.jameseng.dscatalog.services;


import com.jameseng.dscatalog.dto.ProductDTO;
import com.jameseng.dscatalog.entities.Product;
import com.jameseng.dscatalog.repositories.ProductRepository;
import com.jameseng.dscatalog.services.exceptions.DatabaseException;
import com.jameseng.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /*@Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAll();
        return list.stream().map(product -> new ProductDTO(product)).collect(Collectors.toList());
    }*/

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> pageProduct = productRepository.findAll(pageRequest);
        return pageProduct.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> object = productRepository.findById(id);
        Product product = object.orElseThrow(() -> new ResourceNotFoundException("ProductService/Entity not found for id = " + id + "."));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDto) {
        Product product = new Product();
        // product.setName(productDto.getName());
        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDto) {
        try {
            Product product = productRepository.getOne(id);
            // product.setName(productDto.getName());
            product = productRepository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("ProductService/Entity not found for id = " + id + ".");
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("ProductService/Entity not found for id = " + id + ".");
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("ProductService/Database Integrity Violation/Id " + id + " can't be deleted.");
        }
    }
}