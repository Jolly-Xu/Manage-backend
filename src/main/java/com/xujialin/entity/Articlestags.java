package com.xujialin.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author XujialinDashuaige
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Articlestags implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagname;


}
