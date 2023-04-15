package com.jameseng.dscatalog.services;


import com.jameseng.dscatalog.dto.CategoryDTO;
import com.jameseng.dscatalog.entities.Category;
import com.jameseng.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}