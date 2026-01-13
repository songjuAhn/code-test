package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author : 변영우 byw1666@wjcompass.com
 * @since : 2025-10-27
 */
@Getter
@Setter
public class ProductListResponse {
    private List<Product> products;
    /*
     * 문제: [설계] 페이지관련 파라미터들은 공통 클래스로 관리 필요.
     * 원인: [설계] 코드 공통화 필요.
     * 개선안: 페이지 관련 파라미터(totalPages, totalElements, page) 공통 클래스로 분리시켜 관리.
     *       ProductListResponse는 공통 Response 클래스를 상속받아 구현.
     */
    private int totalPages;
    private long totalElements;
    private int page;

    /*
     * 문제: [코드] 생성자 선언의 필요성
     * 원인: [코드] public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) 선언부
     * 개선안: @AllArgsConstructor 사용
     * */
    public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) {
        this.products = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
