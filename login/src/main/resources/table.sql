# 1. 用户信息表

create table l_user_info
(
    id       varchar(100) not null primary key comment '主键id，由系统生成，不使用自增',
    account  varchar(100) not null unique comment '账户名',
    phone    varchar(100) comment '手机号',
    email    varchar(100) comment '邮箱',
    open_id  varchar(100) not null comment 'openid，每个账号对应一个，账号可修改，openid不可修改',
    password varchar(100) not null comment '密码'
)


# 2. 修改记录表