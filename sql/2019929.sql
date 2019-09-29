--- 地址管理添加是否默认字段 ----
ALTER TABLE mmall_shipping ADD checked INT (2) DEFAULT 0 COMMENT "是否默认选中地址"