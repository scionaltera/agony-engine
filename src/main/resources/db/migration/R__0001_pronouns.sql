INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
  VALUES ('he', 'him', 'his', 'his', 'himself')
ON DUPLICATE KEY
  UPDATE subject=VALUES(subject), object=VALUES(object), possessive=VALUES(possessive), possessive_pronoun=VALUES(possessive_pronoun), reflexive=VALUES(reflexive);

INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
VALUES ('she', 'her', 'her', 'hers', 'herself')
ON DUPLICATE KEY
  UPDATE subject=VALUES(subject), object=VALUES(object), possessive=VALUES(possessive), possessive_pronoun=VALUES(possessive_pronoun), reflexive=VALUES(reflexive);

INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
VALUES ('they', 'them', 'their', 'theirs', 'themself')
ON DUPLICATE KEY
  UPDATE subject=VALUES(subject), object=VALUES(object), possessive=VALUES(possessive), possessive_pronoun=VALUES(possessive_pronoun), reflexive=VALUES(reflexive);

INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
VALUES ('it', 'it', 'its', 'its', 'itself')
ON DUPLICATE KEY
  UPDATE subject=VALUES(subject), object=VALUES(object), possessive=VALUES(possessive), possessive_pronoun=VALUES(possessive_pronoun), reflexive=VALUES(reflexive);

INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
VALUES ('one', 'one', 'one''s', 'one''s', 'oneself')
ON DUPLICATE KEY
  UPDATE subject=VALUES(subject), object=VALUES(object), possessive=VALUES(possessive), possessive_pronoun=VALUES(possessive_pronoun), reflexive=VALUES(reflexive);
