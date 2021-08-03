package com.xujialin.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XuJiaLin
 * @date 2021/7/29 11:25
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String username;

    private String password;

    private String checked;
}
