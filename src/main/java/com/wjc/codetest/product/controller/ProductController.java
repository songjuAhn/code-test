package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
/*
 * 문제: [보안] Entity 자체를 리턴하는 것은 보안상 위험.
 *      [설계] Response 데이터 포맷 공통화 필요.
 *      [설계] URI 규칙 개선.
 * 원인: [코드] return ResponseEntity.ok(product);
 *      [설계] 각 API별 Response 데이터 포맷이 다름.
 *      [설계] 각 API별 Mapping URI 규칙적이지 않음.
 * 개선안: service단에서 Entity가 아닌 dto로 리턴하게끔 코드 개선.
 *       공통 응답 DTO 정의하여 포맷 공통화.
 *          ex. { "response_code" : 200 , "data" : { "dtoName" : [{}] } }
 *              [가독성] response_code의 경우 enum으로 코드값 관리하도록 관리하면 좋음.
 *       컨트롤러 상단 @RequestMapping(value = "/products")로 지정.
 *       HTTP 메소드 타입을 활용하여 해당 작업을 정의할 수 있도록 URI 수정.
 *          ex.@PostMapping(value = "/delete/product/{productId}") -> @DeleteMapping(value = "/{productId}")
 */
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /*
     * 문제: [설계] create 기능이므로 명확한 응답코드 리턴하도록 개선 필요.
     * 원인: [코드] ResponseEntity.ok(product);
     * 개선안: ResponseEntity.create 사용하여 명확한 응답코드 리턴.
     */
    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    /*
     * 문제: [설계] 기존 데이터를 delete하는 기능이므로 POST가 아닌 DELETE 타입으로 해야함.
     * 원인: [코드] @PostMapping
     * 개선안: @DeleteMapping 변경 필요.
     */
    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    /*
     * 문제: [설계] 기존 데이터를 update하는 기능이므로 POST가 아닌 PUT 타입으로 해야함.
     * 원인: [코드] @PostMapping
     * 개선안: @PutMapping 변경 필요.
     */
    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    /*
     * 문제: [설계] 데이터를 Get하는 기능이므로 POST가 아닌 GET 메소드타입으로 해야함.
     *      Page 설정 값은 dto 파라미터로 받지말고 Pageable 파라미터를 별도로 받아야 유지보수성 측면에서 용이.
     * 원인: [코드] @PostMapping
     * 개선안: @GetMapping 변경 필요.
     *       Pageable 파라미터 추가. @PageableDefault 활용하여 디폴트 값 설정.
     */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}