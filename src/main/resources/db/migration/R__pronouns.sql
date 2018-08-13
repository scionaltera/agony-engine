INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
  VALUES ('he', 'him', 'his', 'his', 'himself')
ON CONFLICT (subject) DO
UPDATE SET subject=EXCLUDED.subject, object=EXCLUDED.object, possessive=EXCLUDED.possessive, possessive_pronoun=EXCLUDED.possessive_pronoun, reflexive=EXCLUDED.reflexive;

INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
VALUES ('she', 'her', 'her', 'hers', 'herself')
ON CONFLICT (subject) DO
UPDATE SET subject=EXCLUDED.subject, object=EXCLUDED.object, possessive=EXCLUDED.possessive, possessive_pronoun=EXCLUDED.possessive_pronoun, reflexive=EXCLUDED.reflexive;

INSERT INTO pronoun (subject, object, possessive, possessive_pronoun, reflexive)
VALUES ('they', 'them', 'their', 'theirs', 'themself')
ON CONFLICT (subject) DO
UPDATE SET subject=EXCLUDED.subject, object=EXCLUDED.object, possessive=EXCLUDED.possessive, possessive_pronoun=EXCLUDED.possessive_pronoun, reflexive=EXCLUDED.reflexive;
