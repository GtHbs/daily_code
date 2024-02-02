# 1. 用户信息表

create table l_user_info
(
    id                varchar(100) not null primary key comment '主键id，由系统生成，不使用自增',
    user_id           varchar(100) not null unique comment 'userid，每个账号对应一个，账号可修改，userid不可修改',
    account           varchar(100) not null unique comment '账户名',
    avatar            varchar(255) COMMENT '头像图片地址',
    latest_login_time timestamp    not null default CURRENT_TIMESTAMP comment '最后登录时间',
    register_time     timestamp    not null default CURRENT_TIMESTAMP comment '注册时间',
    create_time       timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    modify_time       timestamp    not null default CURRENT_TIMESTAMP comment '修改时间',
    deleted           tinyint(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除'
) engine innodb
  charset utf8mb4
    comment '用户基础信息表';


# 2. 登录关联表

create table l_user_info_extra
(
    id            varchar(100) not null primary key comment '主键id，由系统生成，不使用自增',
    user_id       varchar(100) not null comment '用户id，对应l_user_info(user_id)',
    identify_type varchar(100) not null comment '登录类别'
)

