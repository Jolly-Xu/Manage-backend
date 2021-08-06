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
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Url implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * url编号
     */
    private Integer id;

    /**
     * url地址
     */
    private String url;

    /**
     * 权限id
     */
    private Integer roleId;


}
