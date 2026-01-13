package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
 * 문제: [에러] request body JSON parse error
 * 원인: [코드] UpdateProductRequest Product에 바인딩하는 과정에서 역직렬화시 기본 생성자 필요. 기본 생성자가 없어 에러 발생.
 * 개선안: @NoArgsConstructor 사용하여 기본 생성자 생성.
 * */
public class UpdateProductRequest {
    private Long id;
    private String category;
    private String name;

    public UpdateProductRequest(Long id) {
        this.id = id;
    }

    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    /*
     * 문제: [코드] 생성자 선언의 필요성
     * 원인: [코드] public UpdateProductRequest(Long id, String category, String name) 선언부
     * 개선안: @AllArgsConstructor 사용
     * */
    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}

