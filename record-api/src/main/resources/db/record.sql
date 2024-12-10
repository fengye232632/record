CREATE DATABASE IF NOT EXISTS record DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

USE record;

DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(30) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(30) DEFAULT NULL COMMENT '昵称',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(11) DEFAULT NULL COMMENT '手机号',
    status TINYINT DEFAULT 0 COMMENT '状态 0:正常 1:停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uniq_username (username)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='用户表';

-- 插入一条测试数据，密码为123456
INSERT INTO sys_user(username, password, nickname) VALUES('admin', '123456', '管理员'); 