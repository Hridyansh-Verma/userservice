ALTER TABLE `role`
    ADD is_deleted BIT(1) NULL;

ALTER TABLE token
    ADD is_deleted BIT(1) NULL;

ALTER TABLE user
    ADD is_deleted BIT(1) NULL;