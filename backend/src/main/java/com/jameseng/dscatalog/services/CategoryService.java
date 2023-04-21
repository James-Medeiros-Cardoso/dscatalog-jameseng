package com.jameseng.dscatalog.services;


import com.jameseng.dscatalog.dto.CategoryDTO;
import com.jameseng.dscatalog.entities.Category;
import com.jameseng.dscatalog.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true) // evitar looking no DB
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();

        /*List<CategoryDTO> listDto = new ArrayList<>();
        for (Category category : list) {
            listDto.add(new CategoryDTO(category));
        }
        return listDto;*/

        return list.stream().map(category -> new CategoryDTO(category)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> pageCategory = categoryRepository.findAll(pageRequest);
        return pageCategory.map(category -> new CategoryDTO(category));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> object = categoryRepository.findById(id); // optional = pode ser nulo ou não
        Category category = object.orElseThrow(() -> new ResourceNotFoundException("CategoryService/Entity not found for id = " + id + "."));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDto) {
        try {
            Category category = categoryRepository.getOne(id);
            category.setName(categoryDto.getName());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("CategoryService/Entity not found for id = " + id + ".");
        }
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("CategoryService/Entity not found for id = " + id + ".");
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("CategoryService/Database Integrity Violation/Id " + id + " can't be deleted.");
        }
    }
}