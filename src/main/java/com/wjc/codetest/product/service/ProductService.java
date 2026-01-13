package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
/*
 * 문제: [보안] Entity 자체를 리턴하는 것은 보안상 위험.
 * 원인: [코드] return productRepository.save(product);
 * 개선안: Entity가 아닌 dto로 리턴하게끔 코드 개선.
 */
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    /*
     * 문제: [에러 처리] 데이터가 없는 경우 에러 처리 방식 개선 필요.
     * 원인: [설계]  throw new RuntimeException("product not found");
     * 개선안: GlobalExceptionHandler 클래스의 ResponseStatus에 따른 세분화 처리 필요.
     *       - 데이터가 없는 경우 HttpStatus.NO_CONTENT로 처리하여 명확한 응답코드 리턴.
     */
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    /*
     * 문제: [코드] 업데이트한 객체를 굳이 새로운 변수에 할당할 필요 없음.
     * 원인: [코드] Product updatedProduct = productRepository.save(product);
     * 개선안: 메모리 차원에서 생각했을 때 새로운 변수에 할당하여 리턴하기보단
     *       바로 return productRepository.save(product); 하여 개선.
     */
    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;

    }

    /*
     * 문제: [코드] 삭제할 데이터 여부 확인없이 삭제 진행.
     * 원인: [코드] productRepository.delete(product);
     * 개선안: 삭제할 데이터 여부 확인 후 삭제 진행.
     *       만약, 존재하지 않는 데이터라면
     *          GlobalExceptionHandler의 세분화 처리를 통해
     *          해당 문제 인식할 수 있도록 메시지 전달.
     */
    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    /*
     * 문제: [설계] 정렬방식은 가변적이므로, controller단에서 Pageable 전달받아 조회할 수 있도록 변경.
     * 원인: [코드] Sort.by(Sort.Direction.ASC, "category")
     * 개선안: controller에서 Pageable 파라미터 추가. GetProductListRequest 클래스의 page, size 값 삭제.
     *       전달받는 Pageable 값으로 조회하도록 수정.
     */
    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    /*
     * 문제: [에러 처리] 데이터가 없는 경우 에러 처리 방식 개선 필요.
     * 원인: [설계] 데이터가 없을 경우 [] 반환.
     * 개선안: 데이터 여부 확인 후, HttpStatus.NO_CONTENT 리턴하도록 수정.
     */
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}