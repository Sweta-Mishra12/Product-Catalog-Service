package com.bitsnbytes.product.service;

import com.bitsnbytes.product.dto.ProductDTO;
import com.bitsnbytes.product.entity.Category;
import com.bitsnbytes.product.entity.Product;
import com.bitsnbytes.product.exception.CategoryNotFoundException;
import com.bitsnbytes.product.mapper.CategoryMapper;
import com.bitsnbytes.product.mapper.ProductMapper;
import com.bitsnbytes.product.repository.CategoryRepository;
import com.bitsnbytes.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    public ProductDTO createProduct(ProductDTO productDTO){
        //id, name, description, price, categoryId
     Category category=   categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(()->new CategoryNotFoundException("Category id "+productDTO.getCategoryId()+" not found"));
        Product product= ProductMapper.toProductEntity(productDTO, category);
        product=productRepository.save(product);
        return ProductMapper.toProductDTO(product);

    }
    //getAll products
    public List<ProductDTO> getAllProducts(){
        return productRepository.findAll().stream().map(ProductMapper::toProductDTO).toList();
    }
    //getById product
    public ProductDTO getProductById(Long id){
        Product product=productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        return ProductMapper.toProductDTO(product);
    }
    //Update product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO){
        Product product=productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        Category category=categoryRepository.findById(productDTO.getId()).orElseThrow(()->new RuntimeException("Category id not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);
        productRepository.save(product);
        return ProductMapper.toProductDTO(product);

    }
    //Delete product

    public String deleteProduct(Long id){
        productRepository.deleteById(id);
        return "product "+id +"has been deleted!";
    }
}
