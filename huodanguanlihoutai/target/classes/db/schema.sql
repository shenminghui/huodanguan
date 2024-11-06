-- 删除数据库如果存在
DROP DATABASE IF EXISTS huodan;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS huodan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用数据库
USE huodan;

-- 货单管理表
CREATE TABLE IF NOT EXISTS delivery_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '序号',
    order_date VARCHAR(20) NOT NULL COMMENT '日期',
    project_name VARCHAR(100) NOT NULL COMMENT '工程名称',
    pouring_position VARCHAR(100) COMMENT '浇筑部位',
    receiving_unit VARCHAR(100) COMMENT '收货单位',
    vehicle_number VARCHAR(20) COMMENT '车号',
    driver_name VARCHAR(50) COMMENT '司机姓名',
    -- 供货信息
    supply_grade VARCHAR(50) COMMENT '供货标号',
    supply_volume DECIMAL(10,2) COMMENT '供货方量',
    supply_trips INT COMMENT '供货车次',
    supply_unit_price DECIMAL(10,2) COMMENT '供货单价',
    supply_amount DECIMAL(10,2) COMMENT '供货金额',
    supply_overtime_cost DECIMAL(10,2) COMMENT '供货超时费',
    extra_freight_cost DECIMAL(10,2) COMMENT '补运费',
    supply_reconciliation_status VARCHAR(20) DEFAULT '未对账' COMMENT '供货对账标识',
    supply_payment_status VARCHAR(20) DEFAULT '未收款' COMMENT '供货收款标识',
    supply_image VARCHAR(255) COMMENT '供货单图片',
    -- 商混站信息
    mixing_station VARCHAR(100) COMMENT '商混站名字',
    freight_cost DECIMAL(10,2) COMMENT '运费',
    -- 出厂信息
    factory_grade VARCHAR(50) COMMENT '出厂标号',
    factory_volume DECIMAL(10,2) COMMENT '出厂方量',
    factory_unit_price DECIMAL(10,2) COMMENT '出厂单价',
    factory_amount DECIMAL(10,2) COMMENT '出厂金额',
    factory_overtime_cost DECIMAL(10,2) COMMENT '出厂超时费',
    factory_reconciliation_status VARCHAR(20) DEFAULT '未对账' COMMENT '出厂对账标识',
    factory_payment_status VARCHAR(20) DEFAULT '未付款' COMMENT '出厂付款标识',
    factory_image VARCHAR(255) COMMENT '出厂单图片',
    -- 税费信息
    tax_cost DECIMAL(10,2) COMMENT '税费',
    -- 差异信息
    volume_difference DECIMAL(10,2) COMMENT '方量差异',
    profit DECIMAL(10,2) COMMENT '利润',
    -- 其他信息
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='货单管理表';

-- 建筑材料明细管理表
CREATE TABLE IF NOT EXISTS material_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '序号',
    detail_date DATE NOT NULL COMMENT '日期',
    receiving_location VARCHAR(100) COMMENT '收货地',
    material_type VARCHAR(50) COMMENT '材料类型',
    trips INT COMMENT '车次',
    vehicle_number VARCHAR(20) COMMENT '车号',
    supply_tonnage DECIMAL(10,2) COMMENT '供货吨数',
    supply_unit_price DECIMAL(10,2) COMMENT '供货单价',
    supply_amount DECIMAL(10,2) COMMENT '供货金额',
    supply_reconciliation_status VARCHAR(20) DEFAULT '未对账' COMMENT '供货对账标识',
    supply_payment_status VARCHAR(20) DEFAULT '未收款' COMMENT '供货收款标识',
    supply_image VARCHAR(255) COMMENT '供货单图片',
    factory_tonnage DECIMAL(10,2) COMMENT '出厂吨数',
    factory_unit_price DECIMAL(10,2) COMMENT '出厂单价',
    factory_amount DECIMAL(10,2) COMMENT '出厂金额',
    factory_reconciliation_status VARCHAR(20) DEFAULT '未对账' COMMENT '出厂对账标识',
    factory_payment_status VARCHAR(20) DEFAULT '未收款' COMMENT '出厂收款标识',
    factory_image VARCHAR(255) COMMENT '出厂单图片',
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建筑材料明细管理表';

-- 收支明细表
CREATE TABLE IF NOT EXISTS financial_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '序号',
    record_date DATE NOT NULL COMMENT '日期',
    person_name VARCHAR(50) NOT NULL COMMENT '收支姓名',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    transaction_type VARCHAR(20) DEFAULT '支出' COMMENT '收支类型',
    payment_method VARCHAR(20) COMMENT '收支方式',
    voucher_image VARCHAR(255) COMMENT '收支凭证图片',
    verification_status VARCHAR(20) DEFAULT '未核对' COMMENT '收支核对标识',
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收支明细表';

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入默认管理员账号 (用户名: admin, 密码: 123456)
INSERT INTO sys_user (username, password) VALUES ('admin', '123456');