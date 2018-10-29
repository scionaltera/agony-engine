DELIMITER $$
CREATE FUNCTION uuid_bin(uuid_hex CHAR(36)) RETURNS BINARY(16) DETERMINISTIC
  BEGIN
    RETURN unhex(replace(uuid_hex, '-', ''));
  END $$

CREATE FUNCTION uuid_hex(uuid_bin BINARY(16)) RETURNS CHAR(36) DETERMINISTIC
  BEGIN
    DECLARE uuid_hex CHAR(32);
    SELECT hex(uuid_bin) INTO uuid_hex;
    RETURN LOWER(CONCAT(SUBSTRING(uuid_hex, 1, 8), '-', SUBSTRING(uuid_hex, 9, 4), '-', SUBSTRING(uuid_hex, 13, 4), '-', SUBSTRING(uuid_hex, 17, 4), '-', SUBSTRING(uuid_hex, 21, 12)));
  END $$
DELIMITER ;
