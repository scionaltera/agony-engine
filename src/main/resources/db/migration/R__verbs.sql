INSERT INTO verb (name, priority, bean)
VALUES ('north', 0, 'northCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('east', 0, 'eastCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('south', 0, 'southCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('west', 0, 'westCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, quoting, bean)
VALUES ('say', 500, TRUE, 'sayCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, quoting=EXCLUDED.quoting, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('help', 500, 'helpCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('look', 500, 'lookCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('score', 500, 'scoreCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;

INSERT INTO verb (name, priority, bean)
VALUES ('who', 500, 'whoCommand') ON CONFLICT (name) DO
UPDATE SET name=EXCLUDED.name, priority=EXCLUDED.priority, bean=EXCLUDED.bean;
