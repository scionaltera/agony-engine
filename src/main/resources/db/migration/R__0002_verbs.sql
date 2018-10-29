INSERT INTO verb (name, priority, bean)
VALUES ('north', 0, 'northCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('east', 0, 'eastCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('south', 0, 'southCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('west', 0, 'westCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, quoting, bean)
VALUES ('say', 500, TRUE, 'sayCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('help', 500, 'helpCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('look', 500, 'lookCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('score', 500, 'scoreCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('who', 500, 'whoCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, quoting, bean)
VALUES ('create', 500, TRUE, 'createCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), quoting=VALUES(quoting), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('purge', 500, 'purgeCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('get', 500, 'getCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('drop', 500, 'dropCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('wear', 500, 'wearCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('remove', 500, 'removeCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('inventory', 500, 'inventoryCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('equipment', 500, 'equipmentCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);

INSERT INTO verb (name, priority, bean)
VALUES ('quit', 1000, 'quitCommand') ON DUPLICATE KEY
  UPDATE name=VALUES(name), priority=VALUES(priority), bean=VALUES(bean);
