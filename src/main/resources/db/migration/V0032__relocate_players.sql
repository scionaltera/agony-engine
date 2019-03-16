-- Take players out of any rooms they are in, forcing them back to the center of the map.
UPDATE actor SET room_id=NULL WHERE connection_id IS NOT NULL;

-- Delete all rooms that already exist and are not inventories.
-- They will get recreated by players.
DELETE FROM room WHERE x IS NOT NULL AND y IS NOT NULL AND z IS NOT NULL;
