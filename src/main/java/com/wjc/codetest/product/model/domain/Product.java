package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

    /*
     * 문제: [코드] 생성자 선언의 필요성
     *      [코드] get메소드 선언의 필요성
     * 원인: [코드] protected Product() / public Product(String category, String name) 선언부
     *      [코드] getCategory(), getName() 선언부
     * 개선안: @NoArgsConstructor(access = AccessLevel.PROTECTED) / @AllArgsConstructor 사용
     *       @Getter 가 있으므로 해당 메소드 부분 불필요 -> 삭제
     * */
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
