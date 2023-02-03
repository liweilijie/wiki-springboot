drop table if exists `test`;
create table `test` (
    `id` bigint not null comment 'id',
    `name` varchar(255) comment '名称',
    `password` varchar(255) comment '密码',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `test` (`id`, `name`, `password`) values (1, '小家伙', '123456');
insert into `test` (`id`, `name`, `password`) values (2, '小矮人', '20211022');
