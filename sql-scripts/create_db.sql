create database wiki default character set utf8mb4 collate utf8mb4_general_ci;

drop table if exists `test`;
create table `test` (
    `id` bigint not null comment 'id',
    `name` varchar(255) comment '名称',
    `password` varchar(255) comment '密码',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `test` (`id`, `name`, `password`) values (1, '小家伙', '123456');
insert into `test` (`id`, `name`, `password`) values (2, '小矮人', '20211022');

drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment 'id',
                        `name` varchar(255) comment 'demo名称',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='demo';

insert into `demo` (`id`, `name`) values (1, 'demo测试名字');