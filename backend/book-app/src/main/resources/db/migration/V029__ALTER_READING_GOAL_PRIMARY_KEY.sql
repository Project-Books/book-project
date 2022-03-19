CREATE SEQUENCE reading_goal_id_seq;
ALTER TABLE reading_goal ALTER COLUMN id SET DEFAULT nextval('reading_goal_id_seq');
ALTER SEQUENCE reading_goal_id_seq OWNED BY reading_goal.id;
SELECT setval('reading_goal_id_seq', COALESCE(max(id), 1)) FROM reading_goal;

-- SOURCE -> https://dba.stackexchange.com/questions/194383/how-can-i-change-an-existing-type-from-bigint-to-bigserial