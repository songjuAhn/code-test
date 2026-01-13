package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
 * 문제: [에러] request body JSON parse error
 * 원인: [코드] GetProductListRequest Product에 바인딩하는 과정에서 역직렬화시 기본 생성자 필요. 기본 생성자가 없어 에러 발생 가능성.
 * 개선안: @NoArgsConstructor 사용하여 기본 생성자 생성.
 * */
public class GetProductListRequest {
    private String category;

    /*
     * 문제: [설계] controller단에서 Pageabl을 입력받아 조회할 수 있도록 변경 필요.
     * 원인: [코드] private int page; private int size;
     * 개선안: controller에서 Pageable 파라미터 추가. 해당 변수값 불필요하므로 삭제.
     */
    private int page;
    private int size;
}