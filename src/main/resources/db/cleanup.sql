-- Delete duplicate users keeping only the ones with the lowest ID
DELETE FROM users
WHERE id IN (
    SELECT id
    FROM (
        SELECT id,
               ROW_NUMBER() OVER (PARTITION BY username ORDER BY id) as rn
        FROM users
    ) t
    WHERE t.rn > 1
); 